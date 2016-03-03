package jc.house.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.easemob.chat.EMChatManager;

import jc.house.R;
import jc.house.activities.AboutUsActivity;
import jc.house.activities.CalculatorActivity;
import jc.house.activities.CompanyIntroActivity;
import jc.house.activities.CustomerHelperLoginActivity;
import jc.house.activities.FeedbackActivity;
import jc.house.global.Constants;
import jc.house.global.MApplication;
import jc.house.interfaces.IRefresh;
import jc.house.utils.ToastUtils;

/**
 * Created by WuJie on 2015/11/13.
 */
public class AboutFragment extends BaseFragment implements IRefresh, View.OnClickListener {

    private Button logoutBtn;
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
        this.view.findViewById(R.id.id_calculator).setOnClickListener(this);
        if(!Constants.APPINFO.USER_VERSION) {
            logoutBtn = (Button)this.view.findViewById(R.id.id_btn_logout);
            logoutBtn.setOnClickListener(this);
            logoutBtn.setVisibility(View.VISIBLE);
            String str = "退出登录(" + EMChatManager.getInstance().getCurrentUser() + ")";
            logoutBtn.setText(str);
        }

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_company_introduction:
                Intent intent = new Intent(getActivity(), CompanyIntroActivity.class);
//                intent.putExtra("url", "http://202.118.67.200:10717/house/web/index.php?r=introduction%2Fview");
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
            case R.id.id_calculator:
                startActivity(new Intent(getActivity(), CalculatorActivity.class));
                break;
            case R.id.id_btn_logout:
                logoutBtn.setVisibility(View.GONE);
                //退出登录跳转到登录界面
                EMChatManager.getInstance().logout();
                ((MApplication)getActivity().getApplicationContext()).isEmployeeLogin = false;
                getActivity().finish();
                startActivity(new Intent(getActivity(), CustomerHelperLoginActivity.class));
                break;
        }
    }
}
