package jc.house.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import jc.house.interfaces.IRefresh;
import jc.house.utils.ToastUtils;
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
        ToastUtils.show(this.getActivity(), "刷新数据或者是滑到最上面");
    }
}
