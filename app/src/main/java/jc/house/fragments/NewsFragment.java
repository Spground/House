package jc.house.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.utils.LogUtils;
import jc.house.utils.ParseJson;
import jc.house.views.CircleView;

public class NewsFragment extends BaseNetFragment implements CircleView.CircleViewOnClickListener {
    private static final int[] imageReIds = {R.drawable.temp_house_a,
            R.drawable.temp_house_b, R.drawable.temp_house_a};
//	private static final String[] imageUrls = {"123", "456"};
	private static final String TAG = "NewsFragment";
    private static final int PAGE_SIZE = 8;
//    private List<News> newses;
//    private ListAdapter adapter;
    protected String URL = Constants.SERVER_URL + "news2/news";
    public NewsFragment() {
        super();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        datas = new ArrayList<>();
        CircleView circleView = new CircleView(this.getActivity());
        circleView.setAutoPlay(true);
        circleView.setTimeInterval(3.6f);
        circleView.setImageReIds(imageReIds);
        circleView.setOnCircleViewItemClickListener(this);

        if (DEBUG) {
            datas.add(new News(1, "" + R.drawable.temp_zhaotong, "心系昭通 情献灾区", "管理员", "2015/11/18"));
            datas.add(new News(1, "" + R.drawable.temp_jianzhu, "创新营销 挑战逆境 创回款年度新", "管理员", "2015/11/18"));
            datas.add(new News(1, "" + R.drawable.temp_xiaofang, "大连金宸集团举办2013年消防知识宣传培训活动", "管理员", "2015/11/18"));
            datas.add(new News(1, "" + R.drawable.temp_dongshizhanghuojiang, "金宸集团董事长马国君先生再次荣获大连市慈善", "管理员", "2015/11/18"));
            datas.add(new News(1, "" + R.drawable.temp_xiaofang, "大连金宸集团举办2013年消防知识宣传培训活动", "管理员", "2015/11/18"));
            datas.add(new News(1, "" + R.drawable.temp_zhaotong, "心系昭通 情献灾区", "管理员", "2015/11/18"));
        } else {
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
        }
        this.adapter = new ListAdapter(this.getActivity(), datas, ModelType.NEWS, circleView);
        this.xlistView = (XListView) view.findViewById(R.id.list);
//        this.xlistView.setAdapter(adapter);
//        this.xlistView.setXListViewListener(this);
//        this.xlistView.setPullLoadEnable(true);
//        this.xlistView.setPullRefreshEnable(false);
        initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.debug(TAG, "position is " + position);
                if (position >= 2 && position <= datas.size() + 1) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    if (DEBUG) {
                        intent.putExtra("url", "http://mp.weixin.qq.com/s?__biz=MzI4NzA2MjkwMw==&mid=433484939&idx=1&sn=15443d235a498a1257ab5e941590db0b&scene=23&srcid=1208j8pMKKfumqwJxxyDQQe2#rd");
                    } else {
                        intent.putExtra("url", Constants.SERVER_URL + "news2/mobile&id=" + datas.get(position - 2).getID());
                    }
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_news, container, false);
//        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
//        StoreHouseHeader header = new StoreHouseHeader(getContext());
//        header.setPadding(0, 20, 0, 20);
//        header.initWithString("JIN CHEN");
//        header.setTextColor(Color.RED);
//        ptrFrameLayout.setDurationToCloseHeader(1500);
//        ptrFrameLayout.setHeaderView(header);
//        ptrFrameLayout.addPtrUIHandler(header);
//        ptrFrameLayout.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                ptrFrameLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ptrFrameLayout.refreshComplete();
//                    }
//                }, 1500);
//            }
//        });
        setHeader();
        return this.view;
    }

//    @Override
//    protected void handleResponse(int statusCode, JSONObject response, FetchType fetchType) {
//        resetXListView();
//        if (200 == statusCode && null != response) {
//            try {
//                int code = response.getInt("code");
//                if (Constants.CODE_SUCCESS == code) {
//                    JSONArray array = response.getJSONArray("result");
//                    List<News> lists = ParseJson.parseNews(array);
//                    updateListView(lists, fetchType);
//                } else {
//                    handleCode(code, TAG);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void updateListView(List<News> lists, final FetchType fetchType) {
        if (null != lists && lists.size() > 0) {
            if (fetchType == FetchType.FETCH_TYPE_REFRESH) {
                this.datas.clear();
                this.isOver = false;
            } else {
                if (lists.size() < PAGE_SIZE) {
                    this.isOver = true;
                }
            }
            this.datas.addAll(lists);
            adapter.notifyDataSetChanged();
            if (fetchType == FetchType.FETCH_TYPE_REFRESH) {
                this.xlistView.smoothScrollToPosition(0);
            }
        } else if (null != lists && lists.size() == 0) {
            isOver = true;
            toastNoMoreData();
        }
    }

    @Override
    protected void handleResponse(JSONArray array, FetchType fetchType) {
        List<News> lists = ParseJson.parseNews(array);
        updateListView(lists, fetchType);
    }

    @Override
    protected void fetchDataFromServer(FetchType fetchType) {
        fetchDataFromServer(fetchType, URL, getParams(fetchType, PAGE_SIZE));
    }

    //    @Override
//    protected void fetchDataFromServer(final FetchType fetchtype) {
//        Map<String, String> params = new HashMap<>();
//        params.put("pageSize", String.valueOf(PAGE_SIZE));
//        if (FetchType.FETCH_TYPE_LOAD_MORE == fetchtype) {
//            if (!this.isOver) {
//                if (datas.size() > 0) {
//                    params.put("id", String.valueOf(datas.get(datas.size() - 1).getID()));
//                }
//            } else {
//                toastNoMoreData();
//                resetXListView();
//                return;
//            }
//        }
//        this.client.post(URL, new RequestParams(params), new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                handleResponse(statusCode, response, fetchtype);
//                LogUtils.debug(TAG, "statusCode is " + statusCode + response.toString());
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                LogUtils.debug(TAG, "statusCode is " + statusCode);
//                resetXListView();
//                handleFailure();
//            }
//        });
//    }

    @Override
    public void onCircleViewItemClick(View v, int index) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url", "http://mp.weixin.qq.com/s?__biz=MzI4NzA2MjkwMw==&mid=433484939&idx=1&sn=15443d235a498a1257ab5e941590db0b&scene=23&srcid=1208j8pMKKfumqwJxxyDQQe2#rd");
        startActivity(intent);
    }
}
