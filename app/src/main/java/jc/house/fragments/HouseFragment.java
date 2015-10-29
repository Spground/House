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
import jc.house.models.House;
import jc.house.xListView.XListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFragment extends JCBaseFragment {
    private XListView xListView;
    private List<House> houses;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.common_list,container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.xListView = (XListView)this.view.findViewById(R.id.list);
        this.houses = new ArrayList<House>();
    }

    @Override
    public void refresh() {
        super.refresh();
    }
}
