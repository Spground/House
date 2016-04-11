package jc.house.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;
import java.util.Map;

import jc.house.R;
import jc.house.activities.HomeActivity;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.global.MApplication;
import jc.house.models.BaseModel;
import jc.house.models.JCActivity;
import jc.house.models.ModelType;
import jc.house.utils.SP;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends BaseNetFragment {

    private final int PAGE_SIZE = 5;
    private static final String TAG = "ActivityFragment";
    private static final String JCACTIVITY_ID = "activity_id";
    private boolean firstShow;
    private static final String PARAM_HAS_NUM = "hasNum";

    public ActivityFragment() {
        super();
        this.pageSize = PAGE_SIZE;
        this.url = Constants.ACTIVITY_URL;
        this.tag = TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_activity, container, false);
        setHeader();
        return view;
    }

    @Override
    protected Map<String, String> getParams(FetchType fetchType) {
        Map<String, String> params = super.getParams(fetchType);
        params.remove(PARAM_ID);
        params.put(PARAM_HAS_NUM, this.dataSet.size() + "");
        return params;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mApplication = (MApplication)this.getActivity().getApplication();
        this.adapter = new ListAdapter(this.getActivity(), this.dataSet, ModelType.ACTIVITY);
        initListView();
        if (PRODUCING) {
            this.dataSet.add(new JCActivity(1, "", "金宸•蓝郡一期"));
            this.dataSet.add(new JCActivity(2, "", "连大•文润金宸三期"));
            this.dataSet.add(new JCActivity(3, "", "金宸•蓝郡二期"));
            this.dataSet.add(new JCActivity(4, "", "连大•文润金宸三期"));
            this.dataSet.add(new JCActivity(5, "", "金宸•蓝郡三期"));
        } else {
            showDialog();
            loadLocalData();
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
        }
        this.firstShow = true;
    }

    @Override
    protected void updateListView(List<BaseModel> dataSet, FetchType fetchType) {
        //判断是否出现新活动,显示小红点
        if(this.dataSet != null && this.dataSet.size() >= 1) {
            JCActivity newest = (JCActivity)this.dataSet.get(0);
            int newestID = newest.getId();
            String val = SP.with(this.getActivity()).getString(JCACTIVITY_ID);
            int lastID = val.isEmpty() ? -1 : Integer.valueOf(val.trim());
            if(newestID > lastID) {
                ((HomeActivity)this.getActivity()).showLittleRedDot(2);
                SP.with(this.getActivity()).saveString(JCACTIVITY_ID, String.valueOf(newestID));
            }
        }
        super.updateListView(dataSet, fetchType);
    }

    @Override
    protected void initListView() {
        super.initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                if (PRODUCING) {
                    if (position == 1) {
                        intent.putExtra(WebActivity.FLAG_URL, Constants.NEWS_MOBILE_URL + "12");
                    } else if (position == 2) {
                        intent.putExtra(WebActivity.FLAG_URL, "http://fangchanxiaozha.flzhan.com/index.html?rd=0.855881774565205");
                    } else if (position == 3) {
                        intent.putExtra(WebActivity.FLAG_URL, "http://fangchanxiaozha.flzhan.com/index.html?rd=0.855881774565205");
                    }
                } else {
                    intent.putExtra(WebActivity.FLAG_URL, Constants.ACTIVITY_SHOW_URL + view.getId());
                }
                intent.putExtra(WebActivity.FLAG_TITLE, "活动详情");
                startActivity(intent);
            }
        });
    }

    @Override
    protected Class<? extends BaseModel> getModelClass() {
        return JCActivity.class;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
            firstShow = false;
    }
}
