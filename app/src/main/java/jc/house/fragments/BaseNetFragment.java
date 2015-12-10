package jc.house.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import jc.house.JCListView.XListView;
import jc.house.activities.HomeActivity;
import jc.house.adapters.ListAdapter;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.interfaces.IRefresh;
import jc.house.models.BaseModel;
import jc.house.models.ServerResult;
import jc.house.utils.LogUtils;
import jc.house.utils.ServerUtils;
import jc.house.utils.ToastUtils;
import jc.house.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseNetFragment extends Fragment implements IRefresh, XListView.IXListViewListener {
    protected View view;
    protected XListView xlistView;
    protected AsyncHttpClient client;
    protected boolean isOver;
    protected static final boolean DEBUG = Constants.DEBUG;
    protected List<BaseModel> datas;
    protected ListAdapter adapter;
    protected BaseNetFragment() {
        super();
        this.client = new AsyncHttpClient();
        this.isOver = false;
        this.datas = new ArrayList<>();
    }
    @Override
    public void refresh() {
        this.xlistView.smoothScrollToPosition(0);
    }

    protected void resetXListView() {
        this.xlistView.stopLoadMore();
        this.xlistView.stopRefresh();
    }

    protected void handleCode(int code, String tag) {
        switch (code) {
            case Constants.CODE_FAILED:
                LogUtils.debug(tag, "网络请求参数有错误！");
                break;
            case Constants.CODE_NO_DATA:
                LogUtils.debug(tag, "网络请求连接正常，数据为空！");
                break;
            default:
                break;
        }
    }

    protected void ToastS(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void ToastL(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void toastNoMoreData() {
        ToastS("暂时没有更多信息");
    }
    protected void handleFailure() {
        if (!HomeActivity.isNetAvailable) {
            ToastS("当前网络不可用！");
        } else {
            ToastS("服务器连接错误，请重新尝试！");
        }
    }

    @Override
    public void onRefresh() {
        if (!DEBUG) {
            this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
        } else {
            this.xlistView.stopRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (!DEBUG) {
            this.fetchDataFromServer(FetchType.FETCH_TYPE_LOAD_MORE);
        } else {
            this.xlistView.stopLoadMore();
        }
    }

    protected void initListView() {
        this.xlistView.setAdapter(adapter);
        this.xlistView.setXListViewListener(this);
        this.xlistView.setPullLoadEnable(true);
        this.xlistView.setPullRefreshEnable(false);
    }

    protected Map<String, String> getParams(final FetchType fetchType, int pageSize) {
        if (FetchType.FETCH_TYPE_LOAD_MORE == fetchType && this.isOver) {
            return null;
        }
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", String.valueOf(pageSize));
        if (FetchType.FETCH_TYPE_LOAD_MORE == fetchType) {
            if (datas.size() > 0) {
                params.put("id", String.valueOf(((BaseModel)datas.get(datas.size() - 1)).getID()));
            }
        }
        return params;
    }
    protected void fetchDataFromServer(final FetchType fetchType, String URL,  Map<String, String> params) {
        if (null == params) {
            return;
        }
        this.client.post(URL, new RequestParams(params), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handleResponse(statusCode, response, fetchType);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                resetXListView();
                handleFailure();
            }
        });
    }
    protected void handleResponse(int statusCode, JSONObject response, final FetchType fetchtype) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            ServerResult result = ServerUtils.parseServerResponse(response);
            if (result.isSuccess) {
                handleResponse(result.array, fetchtype);
            } else {
                handleCode(result.code, "server code");
            }
        }
    }
    protected void setHeader() {
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
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }
    protected abstract void fetchDataFromServer(final FetchType fetchType);
    protected abstract void handleResponse(JSONArray array, final FetchType fetchType);
}
