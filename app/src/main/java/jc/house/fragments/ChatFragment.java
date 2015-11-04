package jc.house.fragments;

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

import jc.house.R;
import jc.house.activities.ChatActivity;
import jc.house.activities.MapActivity;
import jc.house.activities.NewsDetailActivity;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.models.ChatUser;
import jc.house.models.ModelType;
import jc.house.xListView.XListView;

public class ChatFragment extends JCNetFragment implements XListView.XListViewListener {

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
		chatUsers.add(new ChatUser(1, "楠楠", "哈哈哈哈哈哈哈哈哈", "",
				"1020"));
		chatUsers.add(new ChatUser(2, "grace", "哈哈哈哈哈哈哈哈哈", "",
				"1019"));
		chatUsers.add(new ChatUser(3, "grace", "哈哈哈哈哈哈哈哈", "",
				"1019"));
		chatUsers.add(new ChatUser(4, "grace", "哈哈哈哈哈哈", "",
				"1019"));
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
					Intent intent = new Intent();

					intent.putExtra("toChatUserName","admin");

					intent.setClass(getActivity(), ChatActivity.class);
					startActivity(intent);
				}
			}

		});
	}

	@Override
	public void loadMore() {
		new Handler().postDelayed(new Runnable(){

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
	
	

}
