package jc.house.fragments;

import android.support.v4.app.Fragment;
import android.util.DebugUtils;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import jc.house.global.Constants;
import jc.house.interfaces.IRefresh;
import jc.house.utils.LogUtils;
import jc.house.xListView.XListView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class JCNetFragment extends Fragment implements IRefresh {
    protected View view;
    protected XListView xlistView;
    protected AsyncHttpClient client;
    protected JCNetFragment() {
        this.client = new AsyncHttpClient();
    }

    @Override
    public void refresh() {
        Toast.makeText(this.getActivity(), "刷新数据或者是滑到最上面", Toast.LENGTH_SHORT).show();
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
}
