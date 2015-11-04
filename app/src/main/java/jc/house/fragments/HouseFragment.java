package jc.house.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.adapters.ListAdapter;
import jc.house.models.House;
import jc.house.models.ModelType;
import jc.house.xListView.XListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFragment extends JCNetFragment {
    private List<House> houses;

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
        this.houses = new ArrayList<House>();
        this.houses.add(new House(1, "", "楠楠", "地段很好啊", "13699297633", 121, 118));
        this.houses.add(new House(1, "", "楠楠", "地段很好啊", "13699297633", 121, 118));
        this.houses.add(new House(1, "", "楠楠", "地段很好啊", "13699297633", 121, 118));
        this.xlistView.setAdapter(new ListAdapter<House>(this.getActivity(), this.houses, ModelType.HOUSE));
    }

    @Override
    public void refresh() {
        super.refresh();
    }
}
