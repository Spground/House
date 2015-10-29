package jc.house.fragments;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.adapters.ListAdapter;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.views.CircleView;
import jc.house.xListView.XListView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFragment extends JCBaseFragment {
	private static final int[] imageReIds = { R.drawable.caodi,
		R.drawable.chengbao, R.drawable.caodi };
//	private static final String[] imageUrls = {"123", "456"};
	private XListView xlistView;
	
	public NewsFragment(int viewResId) {
		super(viewResId);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.xlistView = (XListView)view.findViewById(R.id.list);
		List<News> news = new ArrayList<News>();
		news.add(new News(1, " ", "hello world", "哈哈哈哈哈哈", "2015/10/23"));
		news.add(new News(2, " ", "hello world, hahahha", "哈哈哈哈哈哈哈", "2015/10/24"));
		news.add(new News(3, " ", "hello world", "哈哈哈哈哈哈哈", "2015/10/24"));
		CircleView circleView = new CircleView(this.getActivity());
		circleView.setAutoPlay(true);
		circleView.setTimeInterval(3.6f);
		circleView.setImageReIds(imageReIds);
		ListAdapter<News> adapter = new ListAdapter<News>(this.getActivity(), news, ModelType.NEWS, circleView);
		this.xlistView.setAdapter(adapter);
		
		/*
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("https://www.google.com", new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
			}
			
		});
		client.get("https://www.google.com", new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRetry(int retryNo) {
				// TODO Auto-generated method stub
				super.onRetry(retryNo);
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}
			
		});
		*/
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = (View)inflater.inflate(this.viewResId, container, false); 
		return this.view;
	}
	
	

}
