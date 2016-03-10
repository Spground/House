package jc.house.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import in.srain.cube.views.ptr.PtrFrameLayout;
import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.async.MThreadPool;
import jc.house.async.ParseTask;
import jc.house.chat.ChatActivity;
import jc.house.chat.adapter.ConversationListAdapter;
import jc.house.chat.event.NewMessageEvent;
import jc.house.global.Constants;
import jc.house.global.MApplication;
import jc.house.interfaces.IRefresh;
import jc.house.models.BaseModel;
import jc.house.models.CustomerHelper;
import jc.house.models.ServerResult;
import jc.house.utils.LogUtils;
import jc.house.utils.SP;
import jc.house.utils.ServerUtils;

public class ChatFragment extends BaseFragment implements IRefresh, BaseFragment.OnPullToRefreshBeginListener {
    public static final String TAG = "ChatFragment";
    private boolean isEventBusRegister = false;
    private List<EMConversation> conversationList;
    private OnNewMessageReceivedListener newMessageCallBack;
    private XListView xlistView;
    private BaseAdapter conversationListAdapter;

    public interface OnNewMessageReceivedListener {
        void onNewMessageReceived();
    }

    public ChatFragment() {
        super();
        conversationList = new ArrayList<>();
        setOnRefreshBeginListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        setHeader();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.debug(TAG, "onStart() is invoked!");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.debug(TAG, "onResume() is invoked!");
        refreshHistoryConversationList();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.debug(TAG, "onPause() is invoked!");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.debug(TAG, "onStop() is invoked!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.debug(TAG, "onDestroy() is invoked!");
        unregisterEventBus();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newMessageCallBack = (OnNewMessageReceivedListener) getActivity();
        /**register event bus**/
        registerEventBus();
        LogUtils.debug(TAG, "onActivityCreated() is invoked!");
        xlistView = (XListView) view.findViewById(R.id.list);
        this.conversationList.addAll(loadHistoryConversationDataSource());
        this.conversationListAdapter = new ConversationListAdapter(this.getActivity(), this.conversationList);
        xlistView.setAdapter(this.conversationListAdapter);
        this.xlistView.setPullRefreshEnable(false);
        this.xlistView.setPullLoadEnable(false);
        this.xlistView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                /**聊天Activity**/
                String toChatUserName = ((ConversationListAdapter.ViewHolder) view.getTag()).huanxinid.toString();
                String nickName = ((ConversationListAdapter.ViewHolder) view.getTag()).name.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("toChatUserName", toChatUserName);
                intent.putExtra("nickName", nickName);
                intent.setClass(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        //长按删除会话
        this.xlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteConversion(((ConversationListAdapter.ViewHolder) view.getTag()).huanxinid.toString());
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.debug(TAG, "onDestroyView() is invoked!");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.debug(TAG, "onAttach() is invoked!");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.debug(TAG, "onCreate() is invoked!");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.debug(TAG, "onViewCreated() is invoked!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.debug(TAG, "onDetach() is invoked!");
    }

    /**
     * load data source to memory
     */
    private List<EMConversation> loadHistoryConversationDataSource() {
        // get all conversation, including stranger
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // filter conversation whose messages size is 0
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> sortList) {
        Collections.sort(sortList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * refresh UI
     */
    private void refreshHistoryConversationList() {
        this.conversationList.clear();
        this.conversationList.addAll(loadHistoryConversationDataSource());
        this.conversationListAdapter.notifyDataSetChanged();
    }

    /**
     * called when new message is coming!
     *
     * @param event new message event
     */
    public void onEventMainThread(NewMessageEvent event) {
        LogUtils.debug(TAG, "Receive new message event");
        Intent intent = event.getIntent();
        String msgId = intent.getStringExtra("msgid");
        String from = intent.getStringExtra("from");
        //if user is in the ChatActivity do nothing just return;
        if (ChatActivity.instance != null)
            return;
        Toast.makeText(this.getActivity(), "收到来自" + from + "的消息，请你查收！", Toast.LENGTH_SHORT).show();
        //callback HomeActivity to update little red dot
        if (newMessageCallBack != null)
            newMessageCallBack.onNewMessageReceived();
        refreshHistoryConversationList();
    }

    private void registerEventBus() {
        if (!isEventBusRegister) {
            EventBus.getDefault().register(this);
            isEventBusRegister = true;
        }
    }

    private void unregisterEventBus() {
        if (isEventBusRegister) {
            EventBus.getDefault().unregister(this);
            isEventBusRegister = false;
        }
    }

    @Override
    public void refresh() {
        LogUtils.debug(TAG, "refresh() is invoked!");
        refreshHistoryConversationList();
    }

    /**
     * 删除会话
     * @param huanxinID
     */
    private void deleteConversion(final String huanxinID) {
        android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(getActivity())
                .setTitle("删除对话")
                .setMessage("确认删除此对话吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EMChatManager.getInstance().deleteConversation(huanxinID);
                        refreshHistoryConversationList();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dlg.show();
    }

    @Override
    public void onPullToRefreshBegin(final PtrFrameLayout ptrFrameLayout) {
        //聊天界面刷新
        refresh();
        getCustomerHelperNickName();
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.refreshComplete();
            }
        }, 1500);
    }


    /**
     * 获取客服==>环信ID名称映射规则
     */
    private void getCustomerHelperNickName() {
        LogUtils.debug(TAG, "getCustomerHelperNickName");
        AsyncHttpClient client = new AsyncHttpClient();
        //get cache first
        client.post(Constants.CUSTOMER_HELPER_NAME_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.debug(TAG, "onSuccess");
                if (!ServerUtils.isConnectServerSuccess(statusCode, response))
                    return;
                ServerResult result = ServerUtils.parseServerResponse(response, ServerResult.Type.Array);
                if (!result.isSuccess)
                    return;
                //cache to local
                saveToLocal(result.array.toString(), CustomerHelper.class);
                MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, CustomerHelper.class) {
                    @Override
                    public void onSuccess(List<? extends BaseModel> models) {
                        super.onSuccess(models);
                        //populate the customer helper mapping
                        for (BaseModel model : models) {
                            CustomerHelper c = (CustomerHelper) model;
                            ((MApplication) getActivity().getApplication())
                                    .customerHelperNameMapping.put(c.getHxID(), c);
                        }
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.debug(TAG, statusCode + responseString);
            }
        });
    }

    /**
     * 将jsonStr缓存到本地
     *
     * @param jsonStr
     * @param cls
     */
    private void saveToLocal(String jsonStr, Class<? extends BaseModel> cls) {
        LogUtils.debug("===jsonStr===", jsonStr);
        SP.with(this.getActivity()).saveJsonString(jsonStr, cls);
    }
}
