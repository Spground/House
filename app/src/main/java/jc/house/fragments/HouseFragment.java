package jc.house.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
import jc.house.R;
import jc.house.adapters.ListAdapter;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.models.House;
import jc.house.models.ModelType;
import jc.house.utils.LogUtils;
import jc.house.utils.ParseJson;
import jc.house.utils.StringUtils;
import jc.house.xListView.XListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFragment extends JCNetFragment implements XListView.XListViewListener{
    private List<House> houses;
    private static final int PAGE_SIZE = 10;
    private static final String TAG = "HouseFragment";
    private static final String URL = Constants.SERVER_URL + "house/houses";
    private ListAdapter<House> adapter;
    private boolean isOver;

    public HouseFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.common_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.xlistView = (XListView) this.view.findViewById(R.id.list);
        this.isOver = false;
        this.houses = new ArrayList<House>();
        this.adapter = new ListAdapter<House>(this.getActivity(), this.houses, ModelType.HOUSE);
        this.xlistView.setAdapter(this.adapter);
        this.xlistView.setxListener(this);
        this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    public void refreshing() {
        this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
    }

    @Override
    public void loadMore() {
        this.fetchDataFromServer(FetchType.FETCH_TYPE_LOAD_MORE);
    }

    private void fetchDataFromServer(final FetchType fetchtype) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageSize", String.valueOf(PAGE_SIZE));
        if (FetchType.FETCH_TYPE_LOAD_MORE == fetchtype) {
            if (!this.isOver) {
                if (null != this.houses && this.houses.size() > 0) {
                    params.put("id", String.valueOf(((House) this.houses.get(0)).getID()));
                }
            } else {
                resetXListView();
                return;
            }
        }
        this.client.post(URL, new RequestParams(params), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (200 == statusCode && null != response) {
                    try {
                        int code = response.getInt("code");
                        if (Constants.CODE_SUCCESS == code) {
                            JSONArray array = response.getJSONArray("result");
                            List<House> lists = ParseJson.parseHouse(array);
                            if (null != lists && lists.size() > 0) {
                                if (FetchType.FETCH_TYPE_REFRESH == fetchtype) {
                                    houses.clear();
                                    isOver = false;
                                }
                                if (lists.size() < PAGE_SIZE) {
                                    isOver = true;
                                }
                            }
                            houses.addAll(lists);
                            adapter.notifyDataSetChanged();
                        } else {
                            handleCode(code, TAG);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                resetXListView();
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
}
