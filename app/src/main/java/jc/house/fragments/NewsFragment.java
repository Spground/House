package jc.house.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jc.house.R;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.async.FetchLocal;
import jc.house.async.FetchServer;
import jc.house.async.ModelsTask;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.global.MApplication;
import jc.house.models.BaseModel;
import jc.house.models.ModelType;
import jc.house.models.News;
import jc.house.models.ServerArrayResult;
import jc.house.models.Slideshow;
import jc.house.utils.ListUtils;
import jc.house.utils.LogUtils;
import jc.house.utils.SP;
import jc.house.utils.StringUtils;
import jc.house.views.CircleView;

public class NewsFragment extends BaseNetFragment implements CircleView.CircleViewOnClickListener {
    private static final int[] imageReIds = {R.drawable.home01,
            R.drawable.home02, R.drawable.home03};
    private static final String TAG = "NewsFragment";
    private static final int PAGE_SIZE = 8;
    private static final String SLIDE_PAGE_SIZE = "3";
    private CircleView circleView;
    private boolean loadSlideSuccess;
    private List<Slideshow> slideShows;

    public NewsFragment() {
        super();
        this.pageSize = PAGE_SIZE;
        this.url = Constants.NEWS_URL;
        this.tag = TAG;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mApplication = (MApplication) this.getActivity().getApplication();
        circleView = new CircleView(this.getActivity());
        circleView.setTimeInterval(5.0f);
        circleView.setOnCircleViewItemClickListener(this);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                circleView.setAutoPlay(true);
            }
        }, 2000);
        this.loadSlideSuccess = false;
        this.adapter = new ListAdapter(this.getActivity(), dataSet, ModelType.NEWS, circleView);
        initListView();
        if (PRODUCING) {
            this.xlistView.setPullLoadEnable(true);
        }
        if (PRODUCING) {
            circleView.setImageReIds(imageReIds);
            dataSet.add(new News(1, "" + R.drawable.temp_zhaotong, "心系昭通 情献灾区", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_jianzhu, "创新营销 挑战逆境 创回款年度新", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_xiaofang, "大连金宸集团举办2013年消防知识宣传培训活动", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_dongshizhanghuojiang, "金宸集团董事长马国君先生再次荣获大连市慈善", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_xiaofang, "大连金宸集团举办2013年消防知识宣传培训活动", "管理员", "2015/11/18"));
            dataSet.add(new News(1, "" + R.drawable.temp_zhaotong, "心系昭通 情献灾区", "管理员", "2015/11/18"));
        } else {
            showDialog();
            loadLocalData();
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
            this.fetchSlideshows();
        }
    }

    @Override
    protected void initListView() {
        super.initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 2 && position <= dataSet.size() + 1) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    if (PRODUCING) {
                        intent.putExtra(WebActivity.FLAG_URL, Constants.NEWS_MOBILE_URL + "12");
                    } else {
                        intent.putExtra(WebActivity.FLAG_URL, Constants.NEWS_MOBILE_URL + ((News) dataSet.get(position - 2)).id);
                    }
                    intent.putExtra(WebActivity.FLAG_TITLE, "新闻详情");
                    startActivity(intent);
                }
                LogUtils.debug(TAG, "Click position is " + position);
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
    public void onDetach() {
        this.circleView.cancelTimer();
        super.onDetach();
    }

    @Override
    protected Class<? extends BaseModel> getModelClass() {
        return News.class;
    }

    private void fetchSlideshows() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAGE_SIZE, SLIDE_PAGE_SIZE);
        FetchServer.share().postModelsFromServer(Constants.SLIDE_URL, params, Slideshow.class,new ModelsTask() {
            @Override
            public void onSuccess(List<? extends BaseModel> models, ServerArrayResult result) {
                setSlideshows((List<Slideshow>) models);
                saveToLocal(result.array.toString(), Slideshow.class);
            }

            @Override
            public void onFail(String msg) {
                super.onFail(msg);
                setDefaultCircleView();
            }

            @Override
            public void onCode(int code) {
                handleCode(code, "Slideshow");
                setDefaultCircleView();
            }
        });
    }

    private void setSlideshows(List<Slideshow> models) {
        if (ListUtils.isValid(models)) {
            return;
        }
        String[] urls = new String[models.size()];
        int i = 0;
        for (Slideshow slide : models) {
            urls[i++] = Constants.IMAGE_URL_ORIGIN + slide.getPicUrl();
        }
        circleView.setImageUrls(urls);
        this.slideShows = models;
        this.loadSlideSuccess = true;
        this.circleView.setOnCircleViewItemClickListener(this);
    }

    private void setDefaultCircleView() {
        this.circleView.setImageReIds(imageReIds);
        this.circleView.setOnCircleViewItemClickListener(null);
    }

    @Override
    public void onCircleViewItemClick(View v, int index) {
        if (this.loadSlideSuccess && index < this.slideShows.size()) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra(WebActivity.FLAG_TITLE, "详情");
            intent.putExtra(WebActivity.FLAG_URL, Constants.SLIDE_MOBILE_URL + this.slideShows.get(index).getId());
            startActivity(intent);
        }
    }

    @Override
    protected void loadLocalData() {
        super.loadLocalData();
        String slides = SP.with(getActivity()).getJsonString(Slideshow.class);
        if (StringUtils.strEmpty(slides)) {
            this.setDefaultCircleView();
        } else {
            FetchLocal.share(getActivity()).fetchModelsFromLocal(Slideshow.class, new ModelsTask() {
                @Override
                public void onSuccess(List<? extends BaseModel> models, ServerArrayResult result) {
                    setSlideshows((List<Slideshow>) models);
                }

                @Override
                public void onCode(int code) {
                }
            });
        }
    }
}
