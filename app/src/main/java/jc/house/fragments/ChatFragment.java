package jc.house.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.srain.cube.views.ptr.PtrFrameLayout;
import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.chat.ChatActivity;
import jc.house.chat.adapter.ConversationListAdapter;
import jc.house.global.Constants;
import jc.house.global.MApplication;
import jc.house.interfaces.IRefresh;
import jc.house.models.CustomerHelper;
import jc.house.models.HouseHelpers;
import jc.house.utils.LogUtils;

public class ChatFragment extends BaseFragment implements IRefresh, BaseFragment.OnPullToRefreshBeginListener {
    public static final String TAG = "ChatFragment";
    private List<EMConversation> conversationList;
    private Map<Integer, String> positionMap;
    private OnNewMessageReceivedListener newMessageCallBack;
    private BaseAdapter conversationListAdapter;
    private NewMessageBroadcastReceiver msgReceiver;
    private Handler mHandler;
    private boolean stop;
    private int msgSize = -1;
    private boolean hasNew = false;
    private MApplication mApplication;

    public interface OnNewMessageReceivedListener {
        void onNewMessageReceived();
    }

    public ChatFragment() {
        super();
        conversationList = new ArrayList<>();
        positionMap = new HashMap<>();
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
        getActivity().unregisterReceiver(msgReceiver);
        stop = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newMessageCallBack = (OnNewMessageReceivedListener) getActivity();
        msgReceiver = new NewMessageBroadcastReceiver();
        mApplication = (MApplication) (this.getActivity().getApplication());
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        this.getActivity().registerReceiver(msgReceiver, intentFilter);
        XListView xlistView = (XListView) view.findViewById(R.id.list);
        if(Constants.APPINFO.USER_VERSION)
            loadEMConversationList();
        else
            this.conversationList.addAll(loadHistoryConversationDataSource());
        this.conversationListAdapter = new ConversationListAdapter(this.getActivity(), this.conversationList, this.positionMap);
        xlistView.setAdapter(this.conversationListAdapter);
        xlistView.setPullRefreshEnable(false);
        xlistView.setPullLoadEnable(false);
        xlistView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                /**跳转到ChatActivity**/
                String huanxinId = ((ConversationListAdapter.ViewHolder) view.getTag()).huanxinid;
                String nickName = ((ConversationListAdapter.ViewHolder) view.getTag()).name.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("toChatUserName", huanxinId);
                intent.putExtra("nickName", nickName);
                intent.setClass(getActivity(), ChatActivity.class);
                startActivity(intent);
                EMConversation conversation = conversationList.get(pos - 1);
                conversation.resetUnreadMsgCount();
            }
        });
        //长按删除会话
        xlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteConversion(((ConversationListAdapter.ViewHolder) view.getTag()).huanxinid);
                return true;
            }
        });
        mHandler = new Handler(Looper.myLooper());
        stop = false;
        if (MApplication.isLowerVersion) {
            circle();
        }
    }

    /**
     * 循环获取新消息，解决5.0以下版本获取不到环信新消息的广播的BUG
     */
    private void circle() {
        //匿名内部类和非静态类都会持有外部类的一个引用，容易引起内存泄露。post的message包含一个handler（也就是message的target），
        // handler如果是下面MHandler的写法则也会引用到外部类，导致内存泄露
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshHistoryConversationList();
                if (!stop) {
                    circle();
                }
            }
        }, 1200);
    }

    /*
    private class MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    refreshHistoryConversationList();
                    break;
                case 1:
                    break;
            }
        }
    }
    */

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
        int msgSize = 0;
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    msgSize += conversation.getAllMsgCount();
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        if (msgSize != this.msgSize && this.msgSize != -1) {
            hasNew = true;
            this.msgSize = msgSize;
        } else {
            this.msgSize = msgSize;
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

    private void fillList(List<EMConversation> list) {
        if (null == mApplication || null == mApplication.customerHelperNameMapping) {
            return;
        }
        Set<String> sets = new HashSet<>();
        for (EMConversation item : list) {
            sets.add(item.getUserName());
        }
        Iterator<Map.Entry<String, CustomerHelper>> iterator = mApplication.customerHelperNameMapping.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CustomerHelper> item = iterator.next();
            if (sets.contains(item.getKey())) {
                continue;
            }
            list.add(new EMConversation(item.getKey()));
        }
    }

    //用户版才调用此方法
    private void loadEMConversationList() {
        if (!Constants.APPINFO.USER_VERSION)
            return;
        List<EMConversation> list = loadHistoryConversationDataSource();
        if (null == mApplication || null == mApplication.houseHelpersList) {
            this.conversationList.clear();
            this.conversationList.addAll(list);
            return;
        }
        Map<String, EMConversation> map = new HashMap<>(list != null ? list.size() : 0);
        for (EMConversation item : list) {
            map.put(item.getUserName(), item);
        }
        List<EMConversation> temList = new ArrayList<>();
        Map<Integer, String> mapPos = new HashMap<>();
        int count = 0;
        for (HouseHelpers item : mApplication.houseHelpersList) {
            int flag = 0;
            for (CustomerHelper helper : item.getHelpers()) {
                if (map.containsKey(helper.getHxID())) {
                    temList.add(map.get(helper.getHxID()));
                } else {
                    temList.add(new EMConversation(helper.getHxID()));
                }
                if (flag == 0) {
                    mapPos.put(count, item.getName());
                    flag++;
                }
                count++;
            }
        }
        this.conversationList.clear();
        this.conversationList.addAll(temList);
        positionMap.clear();
        positionMap.putAll(mapPos);
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
        if (Constants.APPINFO.USER_VERSION)
            loadEMConversationList();
        else {
            this.conversationList.clear();
            this.conversationList.addAll(loadHistoryConversationDataSource());
        }
        this.conversationListAdapter.notifyDataSetChanged();
        if (newMessageCallBack != null && hasNew) {
            newMessageCallBack.onNewMessageReceived();
            hasNew = false;
        }
    }

    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String from = intent.getStringExtra("from");
            if (ChatActivity.instance != null) {
                if (from.equals(ChatActivity.instance.getToChatUserName())) {
                    LogUtils.debug("I receive message from " + from + " AA--BB");
                    return;
                }
            }
            if (newMessageCallBack != null)
                newMessageCallBack.onNewMessageReceived();
            refresh();
        }
    }


    @Override
    public void refresh() {
        LogUtils.debug(TAG, "refresh() is invoked!");
        refreshHistoryConversationList();
    }

    /**
     * 删除会话
     *
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
        //聊天历史会话界面刷新
        refresh();
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.refreshComplete();
            }
        }, 1500);
    }

}
