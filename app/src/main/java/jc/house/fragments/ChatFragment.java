package jc.house.fragments;

import java.util.ArrayList;
import java.util.List;

import jc.house.NewsDetailActivity;
import jc.house.R;
import jc.house.adapters.ListAdapter;
import jc.house.models.ChatUser;
import jc.house.models.ModelType;
import jc.house.xListView.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ChatFragment extends JCBaseFragment implements XListView.XListViewListener {
	private XListView xListView;

	public ChatFragment(int viewResId) {
		super(viewResId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.chat_user_list, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		xListView = (XListView) view.findViewById(R.id.list);
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		chatUsers.add(new ChatUser(1, "楠楠", "哈哈哈哈哈哈哈哈哈", R.drawable.user_mao,
				"1020"));
		chatUsers.add(new ChatUser(2, "grace", "哈哈哈哈哈哈哈哈哈", R.drawable.user_mao,
				"1019"));
		chatUsers.add(new ChatUser(3, "grace", "哈哈哈哈哈哈哈哈", R.drawable.user_mao,
				"1019"));
		chatUsers.add(new ChatUser(4, "grace", "哈哈哈哈哈哈", R.drawable.user_mao,
				"1019"));
//		chatUsers.add(new ChatUser("grace", "�������", R.drawable.user_mao,
//				"10��19��"));
//		chatUsers.add(new ChatUser("grace", "�������", R.drawable.user_mao,
//				"10��19��"));
//		chatUsers.add(new ChatUser("grace", "�������", R.drawable.user_mao,
//				"10��19��"));
		xListView
				.setAdapter(new ListAdapter<ChatUser>(this.getActivity(), chatUsers, ModelType.CHAT_USER));
		this.xListView.setxListener(this);
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
		
		this.xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				if(1 == pos) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), NewsDetailActivity.class);
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
				xListView.stopLoadMore();
			}
			
		}, 2000);
		
	}

	@Override
	public void refreshing() {
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				xListView.stopRefresh();
			}
			
		}, 2000);
		
	}
	
	

}
