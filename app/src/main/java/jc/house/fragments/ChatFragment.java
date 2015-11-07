package jc.house.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.easemob.chat.EMChatManager;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.activities.ChatActivity;
import jc.house.activities.HomeActivity;
import jc.house.activities.MapActivity;
import jc.house.activities.NewsDetailActivity;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.models.ChatUser;
import jc.house.models.ModelType;
import jc.house.utils.ToastUtils;
import jc.house.xListView.XListView;

public class ChatFragment extends JCNetFragment implements XListView.XListViewListener {
	private BroadcastReceiver receiver;
	public ChatFragment() {
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.common_list, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		xlistView = (XListView) view.findViewById(R.id.list);
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		chatUsers.add(new ChatUser(1, "发现", "我发现一个比较好玩的地方", "",
				"10:20"));
		chatUsers.add(new ChatUser(2, "地图", "点击我可以看见附件的楼盘信息", "",
				"13:29"));
		chatUsers.add(new ChatUser(3, "活动宣传", "点击我可以看见公司最新的活动详情", "",
				"19:23"));
		chatUsers.add(new ChatUser(4, "客服聊天", "点击我可以向公司的客户直接沟通", "",
				"21:15"));
		xlistView
				.setAdapter(new ListAdapter<ChatUser>(this.getActivity(), chatUsers, ModelType.CHAT_USER));
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
				if(1 == pos) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), NewsDetailActivity.class);
					startActivity(intent);
				} else if (2 == pos) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), MapActivity.class);
					startActivity(intent);
				} else if(3 == pos) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), WebActivity.class);
					startActivity(intent);
				}
				else{
					/**聊天Activity**/
					Intent intent = new Intent();
					intent.putExtra("toChatUserName","wangzhuo");
					intent.setClass(getActivity(), ChatActivity.class);
					startActivity(intent);
				}
			}

		});

		//register receiver
		registerReceiver();
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
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				xlistView.stopRefresh();
			}
			
		}, 2000);
	}

	/**
	 * 注册新消息广播接收者
	 * TODO
	 */
	private void registerReceiver(){
		if(receiver != null)
			return;
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String msgId = intent.getStringExtra("msgid");
				String from = intent.getStringExtra("from");
				//if user is in the ChatActivity do nothing just return;
				if(ChatActivity.instance != null)
					return;
				ToastUtils.show(getActivity(), "收到来自" + from + "的消息，请你查收！");
				abortBroadcast();
			}
		};
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		getActivity().registerReceiver(receiver,intentFilter );
	}

}
