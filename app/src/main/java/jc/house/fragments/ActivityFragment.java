package jc.house.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.HomeActivity;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.async.IParseData;
import jc.house.async.MThreadPool;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.global.RequestType;
import jc.house.models.BaseModel;
import jc.house.models.JCActivity;
import jc.house.models.ModelType;
import jc.house.utils.GeneralUtils;
import jc.house.utils.LogUtils;
import jc.house.utils.ParseJson;
import jc.house.utils.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends BaseNetFragment {

    private String apiURL = Constants.ACTIVITY_URL;
    private final int PAGE_SIZE = 10;
    public ActivityFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_activity, container, false);
        initHeaderView();
        return view;
    }

    public void initHeaderView() {
        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, 20, 0, 20);
        header.initWithString("JIN CHEN");
        header.setTextColor(Color.RED);
        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //refresh operation goes here
                fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);

                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }

        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.adapter = new ListAdapter(this.getActivity(),
                this.dataSet, ModelType.ACTIVITY);
        initListView();
        if(DEBUG) {
            this.dataSet.add(new JCActivity(1, "", "金宸•蓝郡一期"));
            this.dataSet.add(new JCActivity(2, "", "连大•文润金宸三期"));
            this.dataSet.add(new JCActivity(3, "", "金宸•蓝郡二期"));
            this.dataSet.add(new JCActivity(4, "", "连大•文润金宸三期"));
            this.dataSet.add(new JCActivity(5, "", "金宸•蓝郡三期"));
        } else {
            //init data set
            if(HomeActivity.isNetAvailable) {
                fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
            } else {
                //load cache

            }

        }

    }

    @Override
    protected void initListView() {
        this.adapter = new ListAdapter(this.getActivity(), this.dataSet, ModelType.ACTIVITY);
        this.xlistView = (XListView)view.findViewById(R.id.list);
        super.initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                if (DEBUG) {
                    if (position == 1) {
                        intent.putExtra("url", "http://zhan.qq.com/sites/templates/41428/index.html");
                    } else if (position == 2) {
                        intent.putExtra("url", "http://zhan.qq.com/sites/templates/1068/index.html");
                    } else if (position == 3) {
                        intent.putExtra("url", "http://zhan.qq.com/sites/templates/1060/index.html");
                    }
                } else {
                    intent.putExtra("url", Constants.ACTIVITY_SHOW_URL + "&id=" + view.getId());
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleResponse(JSONArray array, FetchType fetchType) {
        //parse json
        MThreadPool.getInstance().submitParseDataTask(array, JCActivity.class, fetchType,
                new IParseData() {
                    @Override
                    public void onParseDataTaskCompleted(List<BaseModel> dataSet, FetchType fetchType) {
                        updateListView(dataSet, fetchType, PAGE_SIZE);
                        ToastUtils.show(getActivity(), "picurl is " + Constants.IMAGE_URL +
                                ((JCActivity) dataSet.get(0)).getPicUrl());
                        ToastUtils.show(getActivity(), "id is " +
                                Constants.ACTIVITY_SHOW_URL + ((JCActivity) dataSet.get(0)).id);


                    }
        });
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    protected void fetchDataFromServer(FetchType fetchtype) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("pageSize", "10");
        fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH, RequestType.GET,
                apiURL, reqParams);
    }

    private List<? extends BaseModel> loadModelDiskCache(int NUMBER) {
        // if network is unavailable, get cached model byte stream from disk
        return null;
    }
}
