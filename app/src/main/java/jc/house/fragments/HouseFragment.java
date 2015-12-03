package jc.house.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.HouseDetailActivity;
import jc.house.activities.MapActivity;
import jc.house.adapters.ListAdapter;
import jc.house.global.Constants;
import jc.house.global.FetchType;
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
    private List<House> houses;
    private ListAdapter<House> adapter;
    private boolean isOver;

    private ImageButton mapBtn;

    public HouseFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_house, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.xlistView = (XListView) this.view.findViewById(R.id.list);
        this.mapBtn = (ImageButton)this.view.findViewById(R.id.id_map_btn);
        this.mapBtn.setOnClickListener(this);
        this.isOver = false;
        this.houses = new ArrayList<>();
        this.adapter = new ListAdapter<>(this.getActivity(), this.houses, ModelType.HOUSE);
        this.xlistView.setAdapter(this.adapter);
        this.xlistView.setPullLoadEnable(true);
        this.xlistView.setXListViewListener(this);
//        this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
        this.houses.add(new House(1, "", "金宸.蓝郡一期", "沙河口 小户型 南北通透 双卫 ",
                "0411-86536589", 112, 125));
        this.houses.add(new House(1, "", "连大.文润金宸三期", "高新园区 花园洋房 低密度 品牌地产",
                "0411-86536589", 112, 125));
        this.houses.add(new House(1, "", "金宸.蓝郡三期", "沙河口区 小户型 板楼 双卫",
                "0411-86536589", 112, 125));
        this.houses.add(new House(1, "", "连大.文润金宸二期", "沙河口 小户型 南北通透 双卫",
                "0411-86536589", 112, 125));
        this.houses.add(new House(1, "", "连大.文润金宸一期", "甘井子区 板楼 南北通透 海景地产",
                "0411-86536589", 112, 125));
        this.houses.add(new House(1, "", "金宸.蓝郡三期", "沙河口 小户型 南北通透 双卫",
                "0411-86536589", 112, 125));
        this.houses.add(new House(1, "", "金宸.蓝郡四期", "甘井子-机场新区 小户型 普通住宅 双卫",
                "0411-86536589", 112, 125));
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
    public void refresh() {
        super.refresh();
    }

    @Override
    protected void handleResponse(int statusCode, JSONObject response, final FetchType fetchtype) {
        resetXListView();
        if (200 == statusCode && null != response) {
            try {
                int code = response.getInt("code");
                if (Constants.CODE_SUCCESS == code) {
                    JSONArray array = response.getJSONArray("result");
                    List<House> lists = ParseJson.parseHouse(array);
                    updateListView(lists, fetchtype);
                } else {
                    handleCode(code, TAG);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateListView(List<House> lists, final FetchType fetchtype) {
        if (null != lists && lists.size() > 0) {
            if (FetchType.FETCH_TYPE_REFRESH == fetchtype) {
                houses.clear();
                isOver = false;
            }
            if (lists.size() < PAGE_SIZE) {
                isOver = true;
            }
            houses.addAll(lists);
            adapter.notifyDataSetChanged();
            if (fetchtype == FetchType.FETCH_TYPE_REFRESH) {
                this.xlistView.smoothScrollToPosition(0);
            }
        } else if (null != lists && lists.size() == 0) {
            toastNoMoreData();
        }
    }

    @Override
    protected void fetchDataFromServer(final FetchType fetchtype) {
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", String.valueOf(PAGE_SIZE));
        if (FetchType.FETCH_TYPE_LOAD_MORE == fetchtype) {
            if (!this.isOver) {
                if (null != this.houses && this.houses.size() > 0) {
                    params.put("id", String.valueOf((this.houses.get(houses.size() - 1)).getID()));
                }
            } else {
                resetXListView();
                toastNoMoreData();
                return;
            }
        }
        this.client.post(URL, new RequestParams(params), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handleResponse(statusCode, response, fetchtype);
                LogUtils.debug(TAG, "statusCode is " + statusCode + response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                resetXListView();
                LogUtils.debug(TAG, "statusCode is " + statusCode);
            }

        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.id_map_btn){
            startActivity(new Intent(getActivity(), MapActivity.class));
        }
    }
}
