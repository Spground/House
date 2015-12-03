package jc.house.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jc.house.JCListView.XListView;
import jc.house.activities.HomeActivity;
import jc.house.global.Constants;
import jc.house.global.FetchType;
import jc.house.interfaces.IRefresh;
import jc.house.utils.LogUtils;
import jc.house.utils.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseNetFragment extends Fragment implements IRefresh, XListView.IXListViewListener {
    protected View view;
    protected XListView xlistView;
    protected AsyncHttpClient client;
    protected boolean isOver;
    protected BaseNetFragment() {
        super();
        this.client = new AsyncHttpClient();
        this.isOver = false;
    }
    @Override
    public void refresh() {
        ToastUtils.show(this.getActivity(), "刷新数据或者是滑到最上面");
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

    protected void toastNoMoreData() {
        ToastUtils.show(this.getActivity(), "暂时没有更多信息");
    }
    protected void handleFailure() {
        if (!HomeActivity.isNetAvailable) {
            ToastUtils.show(this.getActivity(), "当前网络不可用！");
        } else {
            ToastUtils.show(this.getActivity(), "服务器连接错误，请重新尝试！");
        }
    }

    @Override
    public void onRefresh() {
        this.fetchDataFromServer(FetchType.FETCH_TYPE_REFRESH);
    }

    @Override
    public void onLoadMore() {
        this.fetchDataFromServer(FetchType.FETCH_TYPE_LOAD_MORE);
    }

    protected abstract void fetchDataFromServer(final FetchType fetchType);
    protected abstract void handleResponse(int statusCode, JSONObject response, final FetchType fetchtype);
}
