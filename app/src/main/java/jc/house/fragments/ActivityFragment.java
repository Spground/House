package jc.house.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.HomeActivity;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.global.RequestType;
import jc.house.models.BaseModel;
import jc.house.models.JCActivity;
import jc.house.models.ModelType;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends BaseNetFragment {

    private final int PAGE_SIZE = 10;
    private static final String TAG = "ActivityFragment";

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.adapter = new ListAdapter(this.getActivity(),
                this.dataSet, ModelType.ACTIVITY);
        initListView();
        if (PRODUCT) {
            this.dataSet.add(new JCActivity(1, "", "金宸•蓝郡一期"));
            this.dataSet.add(new JCActivity(2, "", "连大•文润金宸三期"));
            this.dataSet.add(new JCActivity(3, "", "金宸•蓝郡二期"));
            this.dataSet.add(new JCActivity(4, "", "连大•文润金宸三期"));
            this.dataSet.add(new JCActivity(5, "", "金宸•蓝郡三期"));
        } else {
            //init data set
            if (HomeActivity.isNetAvailable) {
                fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
            } else {
                //load cache
            }
        }
    }

    @Override
    protected void initListView() {
        this.adapter = new ListAdapter(this.getActivity(), this.dataSet, ModelType.ACTIVITY);
        this.xlistView = (XListView) view.findViewById(R.id.list);
        super.initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                if (PRODUCT) {
                    if (position == 1) {
                        intent.putExtra("url", "http://fangchanxiaozha.flzhan.com/index.html?rd=0.855881774565205");
                    } else if (position == 2) {
                        intent.putExtra("url", "http://fangchanxiaozha.flzhan.com/index.html?rd=0.855881774565205");
                    } else if (position == 3) {
                        intent.putExtra("url", "http://fangchanxiaozha.flzhan.com/index.html?rd=0.855881774565205");
                    }
                } else {
                    intent.putExtra("url", Constants.ACTIVITY_SHOW_URL + "&id=" + view.getId());
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected Class<? extends BaseModel> getModelClass() {
        return JCActivity.class;
    }

    @Override
    protected void fetchDataFromServer(FetchType fetchtype) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("pageSize", String.valueOf(pageSize));
        fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH, RequestType.GET, reqParams);
    }

    private List<? extends BaseModel> loadModelDiskCache(int NUMBER) {
        // if network is unavailable, get cached model byte stream from disk
        return null;
    }
}
