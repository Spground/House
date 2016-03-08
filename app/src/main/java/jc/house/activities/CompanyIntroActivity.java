package jc.house.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jc.house.R;
import jc.house.global.Constants;
import jc.house.global.ServerResultType;
import jc.house.models.CompanyIntroItem;
import jc.house.models.ServerResult;
import jc.house.utils.ListUtils;
import jc.house.utils.LogUtils;
import jc.house.utils.ParseJson;
import jc.house.utils.ServerUtils;
import jc.house.utils.StringUtils;
import jc.house.views.CircleView;

public class CompanyIntroActivity extends BaseNetActivity implements View.OnClickListener, CircleView.CircleViewOnClickListener {

    private static final String[] NAMES = {"公司简介", "公司视频", "联系我们", "公司业务"};
    private static final String[] URLS = {"introduction/viewintroduction&id=1", "introduction/viewvideo&id=1", "introduction/viewphone&id=1", "introduction/viewcontent&id=1"};
    private static final String TAG = "CompanyIntroActivity";
    private static final int[] imageReIds = {R.drawable.home01,
            R.drawable.home02, R.drawable.home03};
    private static final String URL = Constants.SERVER_URL + "introduction/images";
    private CircleView circleView;
    private TextView companyDes, companyVideo, companyContact, companyBusiness;
    private List<CompanyIntroItem> introItems;
    private String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_company_introduction);
        setTitleBarTitle("公司简介");
        initViews();
        setDefaultData();
        fetchDataFromServer();
    }

    private void setDefaultData() {
        this.introItems = new ArrayList<>();
        for (int i = 0; i < NAMES.length; i++) {
            this.introItems.add(new CompanyIntroItem(NAMES[i], URLS[i]));
        }
        this.circleView.setImageReIds(imageReIds);
    }

    private void initViews() {
        this.circleView = (CircleView) this.findViewById(R.id.company_circle_view);
        this.circleView.setTimeInterval(5.0f);
        this.companyDes = (TextView) this.findViewById(R.id.company_des);
        this.companyVideo = (TextView) this.findViewById(R.id.company_video);
        this.companyContact = (TextView) this.findViewById(R.id.company_contact);
        this.companyBusiness = (TextView) this.findViewById(R.id.company_business);
        this.companyDes.setOnClickListener(this);
        this.companyVideo.setOnClickListener(this);
        this.companyContact.setOnClickListener(this);
        this.companyBusiness.setOnClickListener(this);
    }

    private void jumpToWebActivity(int pos) {
        if (null != this.introItems && pos < introItems.size()) {
            CompanyIntroItem item = this.introItems.get(pos);
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra(WebActivity.FLAG_URL, Constants.SERVER_URL + item.getUrl());
            intent.putExtra(WebActivity.FLAG_TITLE, item.getName());
            startActivity(intent);
        }
    }

    private void fetchDataFromServer() {
        this.client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                parseServerData(statusCode, response);
                LogUtils.debug(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                handleFailure();
            }
        });
    }

    private void parseServerData(int statusCode, JSONObject response) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            ServerResult result = ServerUtils.parseServerResponse(response, ServerResultType.Object);
            if (result.isSuccess) {
                try {
                    String url = result.object.getString("url");
                    setUrls(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                handleCode(statusCode, TAG);
            }
        } else {
            handleFailure();
        }
    }

    private void setUrls(String url) {
        if (null != url) {
            this.urls = StringUtils.parseImageUrlsOrigin(url);
            this.circleView.setImageUrls(urls);
            this.circleView.setOnCircleViewItemClickListener(this);
        }
    }

    @Override
    public void onCircleViewItemClick(View v, int index) {
        if (null != urls && index < urls.length) {
            Intent intent = new Intent(this, PhotoViewActivity.class);
            intent.putExtra(PhotoViewActivity.FLAG_IMAGE_URL, urls);
            intent.putExtra(PhotoViewActivity.FLAG_CURRENT_INDEX, index);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.company_des:
                jumpToWebActivity(0);
                break;
            case R.id.company_video:
                jumpToWebActivity(1);
                break;
            case R.id.company_contact:
                jumpToWebActivity(2);
                break;
            case R.id.company_business:
                jumpToWebActivity(3);
                break;
            default:
                break;
        }
    }
}
