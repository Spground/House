package jc.house.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.adapters.ListAdapter;
import jc.house.models.JCActivity;
import jc.house.models.ModelType;
import jc.house.xListView.XListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends JCBaseFragment {
    private XListView xListView;
    private List<JCActivity> activities;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.xListView = (XListView)view.findViewById(R.id.list);
        this.activities = new ArrayList<JCActivity>();
        this.activities.add(new JCActivity(1,"","万达广场"));
        this.activities.add(new JCActivity(1,"","万达广场"));
        this.activities.add(new JCActivity(1,"","万达广场"));
        this.activities.add(new JCActivity(1,"","万达广场"));
        this.activities.add(new JCActivity(1, "", "万达广场"));
        this.xListView.setAdapter(new ListAdapter<JCActivity>(this.getActivity(), this.activities, ModelType.ACTIVITY));
    }

    @Override
    public void refresh() {
        super.refresh();
    }
}
