package jc.house.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.async.MThreadPool;
import jc.house.async.ParseTask;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.global.RequestType;
import jc.house.global.ServerResultType;
import jc.house.models.BaseModel;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.models.ServerResult;
import jc.house.models.Slideshow;
import jc.house.utils.LogUtils;
import jc.house.utils.ServerUtils;
import jc.house.views.CircleView;

public class NewsFragment extends BaseNetFragment implements CircleView.CircleViewOnClickListener {
    private static final int[] imageReIds = {R.drawable.home01,
            R.drawable.home02, R.drawable.home03};
    private static final String TAG = "NewsFragment";
    private static final int PAGE_SIZE = 8;
    private CircleView circleView;
    private boolean loadSlideSuccess;
    private List<Slideshow> slideshows;

    public NewsFragment() {
        super();
        this.pageSize = PAGE_SIZE;
        this.url = Constants.NEWS_URL;
        this.tag = TAG;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        circleView = new CircleView(this.getActivity());
        circleView.setAutoPlay(true);
        circleView.setTimeInterval(2);
        circleView.setOnCircleViewItemClickListener(this);
        this.loadSlideSuccess = false;
        if (PRODUCT) {
            circleView.setImageReIds(imageReIds);
            dataSet.add(new News(1, "" + R.drawable.temp_zhaotong, "心系昭通 情献灾区", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_jianzhu, "创新营销 挑战逆境 创回款年度新", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_xiaofang, "大连金宸集团举办2013年消防知识宣传培训活动", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_dongshizhanghuojiang, "金宸集团董事长马国君先生再次荣获大连市慈善", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_xiaofang, "大连金宸集团举办2013年消防知识宣传培训活动", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_zhaotong, "心系昭通 情献灾区", "管理员", "2015/11/18"));
        } else {
            showDialog();
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
            this.fetchSlideshows();
        }
        this.adapter = new ListAdapter(this.getActivity(), dataSet, ModelType.NEWS, circleView);
        initListView();
        if (!PRODUCT) {
            this.xlistView.setPullLoadEnable(false);
        }
    }

    @Override
    protected void initListView() {
        this.xlistView = (XListView) view.findViewById(R.id.list);
        super.initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.debug(TAG, "position is " + position);
                if (position >= 2 && position <= dataSet.size() + 1) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    if (PRODUCT) {
                        intent.putExtra(WebActivity.FLAG_URL, Constants.NEWS_MOBILE_URL + "12");
                    } else {
                        intent.putExtra(WebActivity.FLAG_URL, Constants.NEWS_MOBILE_URL + ((News) dataSet.get(position - 2)).id);
                    }
                    intent.putExtra(WebActivity.FLAG_TITLE, "新闻详情");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_news, container, false);
        setHeader();
        return this.view;
    }

    @Override
    protected Class<? extends BaseModel> getModelClass() {
        return News.class;
    }

    @Override
    protected Map<String, String> getParams(FetchType fetchType) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAGE_SIZE, String.valueOf(PAGE_SIZE));
        if (FetchType.FETCH_TYPE_LOAD_MORE == fetchType) {
            if (dataSet.size() > 0) {
                params.put(PARAM_ID, String.valueOf(((News) dataSet.get(dataSet.size() - 1)).id));
            }
        }
        return params;
    }

    @Override
    protected void fetchDataFromServer(FetchType fetchType) {
        fetchDataFromServer(fetchType, RequestType.POST);
    }

    private void fetchSlideshows() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAGE_SIZE, "3");
        this.client.post(Constants.SLIDE_URL, new RequestParams(params), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.debug(TAG, response.toString());
                handleSlideshows(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.debug(TAG, responseString.toString());
            }
        });
    }

    private void setSlideshows(List<Slideshow> models) {
        if (null != models && models.size() > 0) {
            String[] urls = new String[models.size()];
            int i = 0;
            for (Slideshow slide : models) {
                urls[i++] = slide.getPicUrl();
            }
            circleView.setImageUrls(urls);
            this.slideshows = models;
            this.loadSlideSuccess = true;
        }
    }

    private void handleSlideshows(int statusCode, JSONObject response) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            ServerResult result = ServerUtils.parseServerResponse(response, ServerResultType.Array);
            if (result.isSuccess) {
                MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, Slideshow.class) {
                    @Override
                    public void onSuccess(List<? extends BaseModel> models) {
                        setSlideshows((List<Slideshow>)models);
                    }
                });
            } else {
                circleView.setImageReIds(imageReIds);
            }
        } else {
            circleView.setImageReIds(imageReIds);
        }
    }

    @Override
    public void onCircleViewItemClick(View v, int index) {
        if (this.loadSlideSuccess) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra(WebActivity.FLAG_TITLE, "详情");
            intent.putExtra(WebActivity.FLAG_URL, Constants.SLIDE_MOBILE_URL + this.slideshows.get(index).getId());
            startActivity(intent);
        }
    }

    @Override
    protected void updateListView(List<BaseModel> dataSet, FetchType fetchType) {
        super.updateListView(dataSet, fetchType);
        this.xlistView.setPullLoadEnable(true);
    }
}
