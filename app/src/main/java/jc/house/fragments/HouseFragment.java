package jc.house.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.HouseDetailActivity;
import jc.house.activities.MapActivity;
import jc.house.adapters.ListAdapter;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.global.MApplication;
import jc.house.global.RequestType;
import jc.house.models.BaseModel;
import jc.house.models.House;
import jc.house.models.HouseDetail;
import jc.house.models.ModelType;
import jc.house.utils.LogUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFragment extends BaseNetFragment implements View.OnClickListener {
    private static final int PAGE_SIZE = 6;
    private static final String TAG = "HouseFragment";
    private ImageButton mapBtn;
    private boolean firstShow;

    public HouseFragment() {
        super();
        this.pageSize = PAGE_SIZE;
        this.url = Constants.HOUSE_URL;
        this.tag = TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_house, container, false);
        setHeader();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mApplication = (MApplication) this.getActivity().getApplication();
        this.mapBtn = (ImageButton) this.view.findViewById(R.id.id_map_btn);
        this.mapBtn.setOnClickListener(this);
        this.adapter = new ListAdapter(this.getActivity(), this.dataSet, ModelType.HOUSE);
        initListView();

        if (PRODUCING) {
            this.dataSet.add(new House(1, "", "金宸.蓝郡一期", "沙河口 小户型 南北通透 双卫 ",
                    "0411-86536589", 39.80, 116.435));
            this.dataSet.add(new House(1, "", "连大.文润金宸三期", "高新园区 花园洋房 低密度 品牌地产",
                    "0411-86536589", 39.90, 116.425));
            this.dataSet.add(new House(1, "", "金宸.蓝郡三期", "沙河口区 小户型 板楼 双卫",
                    "0411-86536589", 39.70, 116.445));
            this.dataSet.add(new House(1, "", "连大.文润金宸二期", "沙河口 小户型 南北通透 双卫",
                    "0411-86536589", 39.90, 116.435));
            this.dataSet.add(new House(1, "", "连大.文润金宸一期", "甘井子区 板楼 南北通透 海景地产",
                    "0411-86536589", 38.90, 116.425));
            this.dataSet.add(new House(1, "", "金宸.蓝郡三期", "沙河口 小户型 南北通透 双卫",
                    "0411-86536589", 39.90, 117.425));
            this.dataSet.add(new House(1, "", "金宸.蓝郡四期", "甘井子-机场新区 小户型 普通住宅 双卫",
                    "0411-86536589", 39.30, 116.425));
        } else {
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
            loadLocalData();
        }
        this.firstShow = true;
    }

    @Override
    protected void initListView() {
        super.initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                if (pos >= 1 && pos <= dataSet.size()) {
                    Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
                    HouseDetail house = (HouseDetail) (dataSet.get(pos - 1));
                    intent.putExtra(HouseDetailActivity.FLAG_HOUSE_DETAIL, house);
                    if (null != house.getHelper()) {
                        intent.putExtra(HouseDetailActivity.FLAG_HELPER_NAME, house.getHelper().getName());
                        intent.putExtra(HouseDetailActivity.FLAG_HELPER_ID, house.getHelper().getHxID());
                    }
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_map_btn) {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            ArrayList<HouseDetail> houses = new ArrayList<>();
            for (BaseModel model : dataSet) {
                houses.add((HouseDetail) model);
            }
            intent.putParcelableArrayListExtra(MapActivity.FLAG_HOUSES, houses);
            intent.putExtra(MapActivity.FLAG_IsSingleMarker, false);
            startActivity(intent);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && firstShow) {
            if (!hasLocalRes) {
                showDialog();
            }
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
            firstShow = false;
        }
    }

    @Override
    protected Class<? extends BaseModel> getModelClass() {
        return HouseDetail.class;
    }
}
