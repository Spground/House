package jc.house.fragments;


import android.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;

import jc.house.xListView.XListView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class JCNetFragment extends JCBaseFragment {

    protected XListView xlistView;
    protected AsyncHttpClient client;
    protected JCNetFragment() {
        this.client = new AsyncHttpClient();
    }

}
