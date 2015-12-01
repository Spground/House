package jc.house.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jc.house.JCListView.XListView;
import jc.house.R;
import jc.house.activities.WebActivity;
import jc.house.adapters.ListAdapter;
import jc.house.global.FetchType;
import jc.house.models.JCActivity;
import jc.house.models.ModelType;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends JCNetFragment{
    private List<JCActivity> activities;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_activity, container, false);
        return view;
    }

    public ActivityFragment() {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.xlistView = (XListView)view.findViewById(R.id.list);
        this.xlistView.setPullLoadEnable(true);
        this.activities = new ArrayList<>();
        this.activities.add(new JCActivity(1,"","金宸•蓝郡一期"));
        this.activities.add(new JCActivity(1,"","连大•文润金宸三期"));
        this.activities.add(new JCActivity(1,"","金宸•蓝郡二期"));
        this.activities.add(new JCActivity(1,"","连大•文润金宸三期"));
        this.activities.add(new JCActivity(1, "", "金宸•蓝郡三期"));
        this.xlistView.setAdapter(new ListAdapter<>(this.getActivity(), this.activities, ModelType.ACTIVITY));
        this.xlistView.setXListViewListener(this);
        this.xlistView.setPullLoadEnable(false);
        this.xlistView.setHeaderDividersEnabled(false);
        this.xlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                if (position == 1) {
                    intent.putExtra("url", "http://zhan.qq.com/sites/templates/41428/index.html");
                } else if(position == 2) {
                    intent.putExtra("url", "http://zhan.qq.com/sites/templates/1068/index.html");
                } else if (position == 3) {
                    intent.putExtra("url", "http://zhan.qq.com/sites/templates/1060/index.html");
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleResponse(int statusCode, JSONObject response, FetchType fetchtype) {};

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    protected void fetchDataFromServer(FetchType fetchtype) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetXListView();
            }
        }, 1000);
    }
}
