package jc.house.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.HouseDetailActivity;
import jc.house.activities.MapActivity;
import jc.house.adapters.ListAdapter;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.global.RequestType;
import jc.house.models.BaseModel;
import jc.house.models.House;
import jc.house.models.ModelType;
import jc.house.utils.LogUtils;
import jc.house.utils.ParseJson;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFragment extends BaseNetFragment implements View.OnClickListener {
    private static final int PAGE_SIZE = 1;
    private static final String TAG = "HouseFragment";
    private static final String URL = Constants.SERVER_URL + "house/houses";

    private ImageButton mapBtn;

    public HouseFragment() {
        super();
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
        this.mapBtn = (ImageButton)this.view.findViewById(R.id.id_map_btn);
        this.mapBtn.setOnClickListener(this);
        initListView();

        if (PRODUCT) {
            this.dataSet.add(new House(1, "", "金宸.蓝郡一期", "沙河口 小户型 南北通透 双卫 ",
                    "0411-86536589", 112, 125));
            this.dataSet.add(new House(1, "", "连大.文润金宸三期", "高新园区 花园洋房 低密度 品牌地产",
                    "0411-86536589", 112, 125));
            this.dataSet.add(new House(1, "", "金宸.蓝郡三期", "沙河口区 小户型 板楼 双卫",
                    "0411-86536589", 112, 125));
            this.dataSet.add(new House(1, "", "连大.文润金宸二期", "沙河口 小户型 南北通透 双卫",
                    "0411-86536589", 112, 125));
            this.dataSet.add(new House(1, "", "连大.文润金宸一期", "甘井子区 板楼 南北通透 海景地产",
                    "0411-86536589", 112, 125));
            this.dataSet.add(new House(1, "", "金宸.蓝郡三期", "沙河口 小户型 南北通透 双卫",
                    "0411-86536589", 112, 125));
            this.dataSet.add(new House(1, "", "金宸.蓝郡四期", "甘井子-机场新区 小户型 普通住宅 双卫",
                    "0411-86536589", 112, 125));
        } else {
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
        }
    }

    @Override
    protected void initListView() {
        this.xlistView = (XListView) this.view.findViewById(R.id.list);
        this.adapter = new ListAdapter(this.getActivity(), this.dataSet, ModelType.HOUSE);
        super.initListView();
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
                startActivity(intent);
                LogUtils.debug(TAG, "pos is " + position);
            }
        });
    }

    @Override
    protected Map<String, String> getParams(FetchType fetchType) {
        Map<String, String> params = super.getParams(fetchType);
        if (null != params) {
            params.put("pageSize", String.valueOf(PAGE_SIZE));
            if (FetchType.FETCH_TYPE_LOAD_MORE == fetchType) {
                if (dataSet.size() > 0) {
                    params.put("id", String.valueOf(((House) dataSet.get(dataSet.size() - 1)).id));
                }
            }
        }
        return params;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.id_map_btn){
            startActivity(new Intent(getActivity(), MapActivity.class));
        }
    }

    @Override
    protected void handleResponse(JSONArray array, FetchType fetchType) {
        List<BaseModel> lists = ParseJson.jsonArray2ModelList(array, House.class);
        updateListView(lists, fetchType, PAGE_SIZE);
    }

    @Override
    protected void fetchDataFromServer(FetchType fetchType) {
        super.fetchDataFromServer(fetchType, RequestType.POST, URL, getParams(fetchType));
    }
}
