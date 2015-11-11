package jc.house.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import jc.house.R;
import jc.house.activities.MapActivity;
import jc.house.activities.NewsDetailActivity;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.chat.ChatActivity;
import jc.house.chat.event.NewMessageEvent;
import jc.house.models.ChatUser;
import jc.house.models.ModelType;
import jc.house.utils.LogUtils;
import jc.house.utils.ToastUtils;
import jc.house.xListView.XListView;

public class ChatFragment extends JCNetFragment implements XListView.XListViewListener {
	public static final String TAG = "ChatFragment";
	private boolean isEventBusRegister = false;

	private OnNewMessageReceivedListener newMessageCallBack;

	public interface OnNewMessageReceivedListener{
		void onNewMessageReceived();
	}

	public ChatFragment() {
		super();
		LogUtils.debug(TAG, "ChatFragment's constructor is invoked!");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.common_list, container, false);
		LogUtils.debug(TAG, "onCreateView() is invoked!");
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
		newMessageCallBack = (OnNewMessageReceivedListener)getActivity();
		/**register event bus**/
		registerEventBus();
		LogUtils.debug(TAG, "onActivityCreated() is invoked!");
		xlistView = (XListView) view.findViewById(R.id.list);
		List<ChatUser> chatUsers = new ArrayList<>();
		chatUsers.add(new ChatUser(1, "发现", "我发现一个比较好玩的地方", "",
				"10:20"));
		chatUsers.add(new ChatUser(2, "地图", "点击我可以看见附件的楼盘信息", "",
				"13:29"));
		chatUsers.add(new ChatUser(3, "活动宣传", "点击我可以看见公司最新的活动详情", "",
				"19:23"));
		chatUsers.add(new ChatUser(4, "客服聊天", "点击我可以向公司的客户直接沟通", "",
				"21:15"));
		xlistView
				.setAdapter(new ListAdapter<>(this.getActivity(), chatUsers, ModelType.CHAT_USER));
		this.xlistView.setxListener(this);
		/*
		this.xListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int pos, long id) {
						Toast.makeText(getActivity(),
								"long click item at " + pos, Toast.LENGTH_SHORT)
								.show();
						return true;
					}

				});
				*/
		
		this.xlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
									long id) {
				if (1 == pos) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), NewsDetailActivity.class);
					startActivity(intent);
				} else if (2 == pos) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), MapActivity.class);
					startActivity(intent);
				} else if (3 == pos) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), WebActivity.class);
					startActivity(intent);
				} else {
					/**聊天Activity**/
					Intent intent = new Intent();
					intent.putExtra("toChatUserName", "admin");
					intent.setClass(getActivity(), ChatActivity.class);
					startActivity(intent);
				}
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

	@Override
	public void loadMore() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				xlistView.stopLoadMore();
			}

		}, 2000);
	}

	@Override
	public void refreshing() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				xlistView.stopRefresh();
			}

		}, 2000);
	}

	/**
	 * called when new message is coming!
	 * @param event new message event
	 */
	public void onEventMainThread(NewMessageEvent event){
		LogUtils.debug(TAG, "Receive new message event");
		Intent intent = event.getIntent();
		String msgId = intent.getStringExtra("msgid");
		String from = intent.getStringExtra("from");
		//if user is in the ChatActivity do nothing just return;
		if(ChatActivity.instance != null)
			return;
		ToastUtils.show(getActivity(), "收到来自" + from + "的消息，请你查收！");
		//callback HomeActivity to update little red dot
		if(newMessageCallBack != null)
			newMessageCallBack.onNewMessageReceived();
	}

	private void registerEventBus(){
		if(!isEventBusRegister){
			EventBus.getDefault().register(this);
			isEventBusRegister = true;
		}
	}

	private void unregisterEventBus(){
		if(isEventBusRegister){
			EventBus.getDefault().unregister(this);
			isEventBusRegister = false;
		}
	}
}
