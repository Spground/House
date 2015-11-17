package jc.house.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jc.house.JCListView.XListView;
import jc.house.R;
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
        this.view = inflater.inflate(R.layout.activity_list, container, false);
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
        this.activities = new ArrayList<JCActivity>();
        this.activities.add(new JCActivity(1,"","万达广场"));
        this.activities.add(new JCActivity(1,"","万达广场"));
        this.activities.add(new JCActivity(1,"","万达广场"));
        this.activities.add(new JCActivity(1,"","万达广场"));
        this.activities.add(new JCActivity(1, "", "万达广场"));
        this.xlistView.setAdapter(new ListAdapter<JCActivity>(this.getActivity(), this.activities, ModelType.ACTIVITY));
    }

    @Override
    protected void handleResponse(int statusCode, JSONObject response, FetchType fetchtype) {};

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    protected void fetchDataFromServer(FetchType fetchtype) {

    }
}
