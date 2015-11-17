package jc.house.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import jc.house.R;
import jc.house.adapters.ListAdapter;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.utils.LogUtils;
import jc.house.utils.StringUtils;
import jc.house.views.CircleView;
import jc.house.xListView.XListView;

public class NewsFragment extends JCNetFragment {
    private static final int[] imageReIds = {R.drawable.caodi,
            R.drawable.chengbao, R.drawable.caodi};
//	private static final String[] imageUrls = {"123", "456"};
	private static final String TAG = "NewsFragment";

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

        this.client.setURLEncodingEnabled(true);
        this.client.setAuthenticationPreemptive(true);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "maoni");
		client.post("http://192.168.9.72/mn/web/index.php?r=test/test", new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.debug(TAG, "statusCode is " + statusCode + " responseString is " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.debug(TAG, "JSONObject statusCode is " + statusCode + "result is " + response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.debug(TAG, "JSONArray statusCode is " + statusCode);
            }
        });

        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("name", "maonia");
        client.get("http://192.168.9.72/mn/web/index.php?r=test/test", new RequestParams(params2), new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONArray response) {
                Log.i(TAG, response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        int id = item.getInt("id");
                        String name = item.getString("name");
                        Log.i(TAG + i, "id is " + id + " name is " + name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(TAG, response.toString());
                super.onSuccess(statusCode, headers, response);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = (View) inflater.inflate(R.layout.common_list, container, false);
        return this.view;
    }


}
