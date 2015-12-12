package jc.house.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.async.IParseData;
import jc.house.async.MThreadPool;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.models.BaseModel;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.utils.LogUtils;
import jc.house.views.CircleView;

public class NewsFragment extends BaseNetFragment implements CircleView.CircleViewOnClickListener {
    private static final int[] imageReIds = {R.drawable.temp_house_a,
            R.drawable.temp_house_b, R.drawable.temp_house_a};
//	private static final String[] imageUrls = {"123", "456"};
	private static final String TAG = "NewsFragment";
    private static final int PAGE_SIZE = 8;
    protected String URL = Constants.SERVER_URL + "news2/news";
    public NewsFragment() {
        super();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        initListView();
    }

    @Override
    protected void initListView() {
        this.xlistView = (XListView) view.findViewById(R.id.list);
        super.initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.debug(TAG, "position is " + position);
                if (position >= 2 && position <= datas.size() + 1) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    if (DEBUG) {
                        intent.putExtra("url", "http://192.168.9.72/house/web/index.php?r=news2%2Fmobile&id=13");
                    } else {
                        intent.putExtra("url", Constants.SERVER_URL + "news2/mobile&id=" + ((News)datas.get(position - 2)).id);
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
        setHeader();
        return this.view;
    }


    @Override
    protected void handleResponse(JSONArray array, FetchType fetchType) {
        MThreadPool.getInstance().submitParseDataTask(array, News.class, fetchType, new IParseData() {
            @Override
            public void onTaskCompleted(final List<BaseModel> lists, final FetchType fetchType) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateListView(lists, fetchType, PAGE_SIZE);
                    }
                });
            }
        });
    }

    @Override
    protected Map<String, String> getParams(FetchType fetchType) {
        Map<String, String> params = super.getParams(fetchType);
        if (null != params) {
            params.put("pageSize", String.valueOf(PAGE_SIZE));
            if (FetchType.FETCH_TYPE_LOAD_MORE == fetchType) {
                if (datas.size() > 0) {
                    params.put("id", String.valueOf(((News)datas.get(datas.size() - 1)).id));
                }
            }
        }
        return params;
    }

    @Override
    protected void fetchDataFromServer(FetchType fetchType) {
        fetchDataFromServer(fetchType, URL, getParams(fetchType));
    }

    @Override
    public void onCircleViewItemClick(View v, int index) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url", "http://mp.weixin.qq.com/s?__biz=MzI4NzA2MjkwMw==&mid=433484939&idx=1&sn=15443d235a498a1257ab5e941590db0b&scene=23&srcid=1208j8pMKKfumqwJxxyDQQe2#rd");
        startActivity(intent);
    }
}
