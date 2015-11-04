package jc.house.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jc.house.R;
import jc.house.adapters.ListAdapter;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.views.CircleView;
import jc.house.xListView.XListView;

public class NewsFragment extends JCNetFragment {
    private static final int[] imageReIds = {R.drawable.caodi,
            R.drawable.chengbao, R.drawable.caodi};
//	private static final String[] imageUrls = {"123", "456"};

    public NewsFragment() {
        super();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.xlistView = (XListView) view.findViewById(R.id.list);
        List<News> news = new ArrayList<News>();
        news.add(new News(1, " ", "今天是1015年10月29，北京房价又涨价了，来自某网站信息。", "楠楠", "2015/10/23"));
        news.add(new News(2, " ", "hello world, hahahha", "楠楠", "2015/10/24"));
        news.add(new News(3, " ", "hello world", "楠楠", "2015/10/24"));
        news.get(0).toString();
        CircleView circleView = new CircleView(this.getActivity());
        circleView.setAutoPlay(true);
        circleView.setTimeInterval(3.6f);
        circleView.setImageReIds(imageReIds);
        ListAdapter<News> adapter = new ListAdapter<News>(this.getActivity(), news, ModelType.NEWS, circleView);
        this.xlistView.setAdapter(adapter);

        Log.i("NewsFragment", "onActivityCreated!");

		/*
        client.post("", new RequestParams(new HashMap<String, String>()), new ResponseHandlerInterface() {
			@Override
			public void sendResponseMessage(HttpResponse response) throws IOException {

			}

			@Override
			public void sendStartMessage() {

			}

			@Override
			public void sendFinishMessage() {

			}

			@Override
			public void sendProgressMessage(long bytesWritten, long bytesTotal) {

			}

			@Override
			public void sendCancelMessage() {

			}

			@Override
			public void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBody) {

			}

			@Override
			public void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}

			@Override
			public void sendRetryMessage(int retryNo) {

			}

			@Override
			public URI getRequestURI() {
				return null;
			}

			@Override
			public void setRequestURI(URI requestURI) {

			}

			@Override
			public Header[] getRequestHeaders() {
				return new Header[0];
			}

			@Override
			public void setRequestHeaders(Header[] requestHeaders) {

			}

			@Override
			public boolean getUseSynchronousMode() {
				return false;
			}

			@Override
			public void setUseSynchronousMode(boolean useSynchronousMode) {

			}

			@Override
			public boolean getUsePoolThread() {
				return false;
			}

			@Override
			public void setUsePoolThread(boolean usePoolThread) {

			}

			@Override
			public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

			}

			@Override
			public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

			}

			@Override
			public Object getTag() {
				return null;
			}

			@Override
			public void setTag(Object TAG) {

			}
		});
		*/
        client.get("http://192.168.9.72/mn/web/index.php?r=test%2Fjsons", new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONArray response) {
                Log.i("jsonArray", response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        int id = item.getInt("id");
                        String name = item.getString("name");
                        Log.i("user" + i, "id is " + id + " name is " + name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                Log.i("jsonObject", response.toString());
                super.onSuccess(statusCode, headers, response);
            }
        });
		/*
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
        this.view = (View) inflater.inflate(R.layout.common_list, container, false);
        return this.view;
    }


}
