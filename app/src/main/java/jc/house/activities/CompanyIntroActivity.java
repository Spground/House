package jc.house.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.async.FetchServer;
import jc.house.async.StringTask;
import jc.house.global.Constants;
import jc.house.models.CompanyIntroItem;
import jc.house.utils.SP;
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
        fetchLocalUrl();
        fetchDataFromServer();
    }

    private void setDefaultData() {
        this.introItems = new ArrayList<>();
        for (int i = 0; i < NAMES.length; i++) {
            this.introItems.add(new CompanyIntroItem(NAMES[i], URLS[i]));
        }
    }

    private void initViews() {
        this.circleView = (CircleView) this.findViewById(R.id.company_circle_view);
        this.circleView.setTimeInterval(5.0f);
        this.circleView.setAutoPlay(false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                circleView.setAutoPlay(true);
            }
        }, 1000);
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
        FetchServer.share().fetchCompanyImages(new StringTask() {
            @Override
            public void onSuccess(String result) {
                setUrls(result);
            }

            @Override
            public void onFail(String msg) {
                super.onFail(msg);
                handleFailure();
            }

            @Override
            public void onCode(int code) {
                handleCode(code, TAG);
            }
        });
    }

    private void setUrls(String url) {
        if (!StringUtils.strEmpty(url)) {
            this.urls = StringUtils.parseImageUrlsOrigin(url);
            this.circleView.setImageUrls(urls);
            this.circleView.setOnCircleViewItemClickListener(this);
            saveUrlToLocal(url);
        }
    }

    private void saveUrlToLocal(String url) {
        SP.with(this).saveString(TAG, url);
    }

    private void fetchLocalUrl() {
        String url = SP.with(this).getString(TAG);
        if (!StringUtils.strEmpty(url)) {
            this.urls = StringUtils.parseImageUrlsOrigin(url);
            this.circleView.setImageUrls(urls);
            this.circleView.setOnCircleViewItemClickListener(this);
        } else {
            this.circleView.setImageReIds(imageReIds);
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
    public void finish() {
        this.circleView.cancelTimer();
        super.finish();
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
