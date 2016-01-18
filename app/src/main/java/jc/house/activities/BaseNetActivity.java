package jc.house.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.squareup.picasso.Picasso;

import jc.house.R;
import jc.house.global.Constants;
import jc.house.models.ServerResult;
import jc.house.utils.LogUtils;

public class BaseNetActivity extends BaseActivity {
    protected AsyncHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client = new AsyncHttpClient();
        client.setTimeout(5 * 1000);
    }

    protected void handleCode(int code, String tag) {
        switch (code) {
            case ServerResult.CODE_FAILURE:
                LogUtils.debug(tag, "网络请求参数有错误！");
                break;
            case ServerResult.CODE_NO_DATA:
                LogUtils.debug(tag, "网络请求连接正常，数据为空！");
                break;
            default:
                break;
        }
        hideDialog();
    }

    protected void loadImage(ImageView imageView, String url) {
        Picasso.with(this).load(Constants.IMAGE_URL + url).placeholder(R.drawable.failure_image_red).error(R.drawable.failure_image_red).into(imageView);
    }

    protected void handleFailure() {
        if (!HomeActivity.isNetAvailable) {
            ToastS("当前网络不可用！");
        } else {
            ToastS("服务器连接错误，请重新尝试！");
        }
        hideDialog();
    }
}
