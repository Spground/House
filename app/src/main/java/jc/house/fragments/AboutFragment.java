package jc.house.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jc.house.R;
import jc.house.activities.AboutUsActivity;
import jc.house.activities.FeedbackActivity;
import jc.house.activities.WebActivity;
import jc.house.interfaces.IRefresh;
import jc.house.utils.ToastUtils;

/**
 * Created by WuJie on 2015/11/13.
 */
public class AboutFragment extends BaseFragment implements IRefresh, View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_about, container, false);
        setHeader();
        return this.view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        this.view.findViewById(R.id.id_about_us).setOnClickListener(this);
        this.view.findViewById(R.id.id_check_update).setOnClickListener(this);
        this.view.findViewById(R.id.id_company_introduction).setOnClickListener(this);
        this.view.findViewById(R.id.id_user_feedback).setOnClickListener(this);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_company_introduction:
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", "http://fangchanxiaozha.flzhan.com/index.html?rd=0.855881774565205");
                startActivity(intent);
                break;
            case R.id.id_user_feedback:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.id_check_update:
                ToastUtils.show(getActivity(), "您的已经是最新版本!");
                break;
            case R.id.id_about_us:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
        }
    }
}
