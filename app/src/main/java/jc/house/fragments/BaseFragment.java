package jc.house.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import jc.house.R;
import jc.house.global.Constants;
import jc.house.global.MApplication;

/**
 * Created by hzj on 2015/12/10.
 */
public class BaseFragment extends Fragment {
    protected View view;
    protected static final boolean PRODUCT = Constants.PRODUCT;
    protected ProgressDialog progressDialog;
    protected MApplication mApplication;

    protected void ToastS(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void ToastL(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showDialog() {
        if (null == this.progressDialog) {
            this.progressDialog = new ProgressDialog(this.getActivity());
            this.progressDialog.setMessage("正在加载中");
            this.progressDialog.setCanceledOnTouchOutside(true);
            this.progressDialog.setCancelable(true);
        }
        this.progressDialog.show();
    }

    protected void hideDialog() {
        if (null != this.progressDialog) {
            this.progressDialog.hide();
        }
    }

    protected void setHeader() {
        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, 20, 0, 20);
        header.initWithString("JIN CHEN");
        header.setTextColor(R.color.red_jc);
        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1500);
            }
        });
    }
}
