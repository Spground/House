package jc.house.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jc.house.R;
import jc.house.async.MThreadPool;
import jc.house.async.ParseTask;
import jc.house.chat.ChatActivity;
import jc.house.global.Constants;
import jc.house.global.ServerResultType;
import jc.house.models.BaseModel;
import jc.house.models.House;
import jc.house.models.HouseDetail;
import jc.house.models.ServerResult;
import jc.house.utils.ServerUtils;
import jc.house.utils.StringUtils;
import jc.house.views.MViewPager;
import jc.house.views.ViewPagerTitle;

public class HouseDetailActivity extends BaseNetActivity implements View.OnClickListener {
    private static final String TAG = "HouseDetailActivity";
    public static final String HOUSE_DETAIL_URL = Constants.SERVER_URL + "house/detail";
    private static final int[] ids = {R.id.recommend, R.id.traffic, R.id.design};
    private MViewPager viewPager;
    private List<TextView> textViews;
    private List<ViewPagerTitle> titles;
    private HouseDetail houseDetail;
    private TextView mapTextView;
    private TextView chatTextView;
    private int currentIndex;
    private ImageView houseImageView;
    private TextView tvAddress;
    private TextView tvHouseType;
    private TextView tvForceType;
    private TextView tvAvgPrice;
    private TextView tvPhone;
    private int id = -1;
    public static final String FLAG_ID = "id";
    public static final String FLAG_HOUSE_DETAIL = "HouseDetail";
    public static final String FLAG_HELPER_ID = "HelperID";
    public static final String FLAG_HELPER_NAME = "HelperName";
    private String hxID;
    private String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_house_detail);
        initViews();
        initViewPager();
        this.setScrollRightBack(true);
        if (!PRODUCT) {
            id = this.getIntent().getIntExtra(FLAG_ID, -1);
            if (id >= 0) {
                showDialog();
                hideViews();
                fetchDataFromServer();
            } else {
                houseDetail = this.getIntent().getParcelableExtra(FLAG_HOUSE_DETAIL);
                this.hxID = this.getIntent().getStringExtra(FLAG_HELPER_ID);
                this.nickName = this.getIntent().getStringExtra(FLAG_HELPER_NAME);
                setServerData(houseDetail);
            }
        }
    }

    private void initViews() {
        this.houseImageView = (ImageView) findViewById(R.id.house_image_view);
        this.houseImageView.setOnClickListener(this);
        if (PRODUCT) {
            houseImageView.setImageResource(R.drawable.failure_image_red);
        }
        this.mapTextView = (TextView) this.getLayoutInflater().inflate(R.layout.div_titlebar_rightview, null);
        this.mapTextView.setText("地图");
        this.mapTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    mapTextView.setTextColor(Color.LTGRAY);
                } else {
                    mapTextView.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        this.mapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseDetailActivity.this, MapActivity.class);
                if (PRODUCT) {
                    intent.putExtra(MapActivity.FLAG_HOUSE, new House(12, "123", "456", "789", "hello", 39.70, 116.445));
                } else {
                    //TODO 跳转
                    intent.putExtra(MapActivity.FLAG_IsSingleMarker, true);
                    intent.putExtra(MapActivity.FLAG_HOUSE, (House)houseDetail);
                }
                startActivity(intent);
            }
        });
        this.titleBar.setRightChildView(mapTextView);
        this.setTitleBarTitle("楼盘详情");

        this.chatTextView = (TextView) this.findViewById(R.id.chat);
        this.chatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到聊天页面
                Intent intent = new Intent(HouseDetailActivity.this, ChatActivity.class);
                if (!PRODUCT) {
                    if (id >= 0 && null != houseDetail && null != houseDetail.getHelper()) {
                        intent.putExtra("toChatUserName", houseDetail.getHelper().getHxID());
                        intent.putExtra("nickName", houseDetail.getHelper().getName());
                        intent.putExtra(ChatActivity.EXTRA_HOUSE, (House)houseDetail);
                        startActivity(intent);
                    } else if (!StringUtils.strEmpty(hxID) && !StringUtils.strEmpty(nickName)) {
                        intent.putExtra("toChatUserName", hxID);
                        intent.putExtra("nickName", nickName);
                        intent.putExtra(ChatActivity.EXTRA_HOUSE, houseDetail);
                        startActivity(intent);
                    }
                } else {
                    intent.putExtra("toChatUserName", "admin");
                    startActivity(intent);
                }
            }
        });

        this.tvAddress = (TextView) this.findViewById(R.id.address);
        this.tvHouseType = (TextView) this.findViewById(R.id.houseType);
        this.tvForceType = (TextView) this.findViewById(R.id.forceType);
        this.tvAvgPrice = (TextView) this.findViewById(R.id.avgPrice);
        this.tvPhone = (TextView) this.findViewById(R.id.phone);
    }

    private void hideViews() {
        this.tvAddress.setVisibility(View.GONE);
        this.tvHouseType.setVisibility(View.GONE);
        this.tvForceType.setVisibility(View.GONE);
        this.tvAvgPrice.setVisibility(View.GONE);
        this.tvPhone.setVisibility(View.GONE);
        this.viewPager.setVisibility(View.GONE);
    }

    private void showViews() {
        this.tvAddress.setVisibility(View.VISIBLE);
        this.tvHouseType.setVisibility(View.VISIBLE);
        this.tvForceType.setVisibility(View.VISIBLE);
        this.tvAvgPrice.setVisibility(View.VISIBLE);
        this.tvPhone.setVisibility(View.VISIBLE);
        this.viewPager.setVisibility(View.VISIBLE);
    }

    private void initViewPager() {
        this.viewPager = (MViewPager) this.findViewById(R.id.viewpager);
        this.currentIndex = 0;
        this.titles = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            ViewPagerTitle title = (ViewPagerTitle) this.findViewById(ids[i]);
            title.setIndex(i);
            title.setSelected(i == currentIndex);
            title.setOnClickListener(this);
            titles.add(title);
        }
        this.textViews = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setPadding(12, 10, 12, 5);
            textView.setTextSize(13.0f);
            textView.setTextColor(Color.rgb(120, 120, 120));
            textView.setBackgroundColor(Color.rgb(250, 250, 250));
            textView.setText("");
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLineSpacing(0, 1.2f);
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            textViews.add(textView);
        }
        this.viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return textViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                TextView textView = textViews.get(position);
                container.addView(textView);
                return textView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(textViews.get(position));
            }
        });

        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != currentIndex) {
                    titles.get(currentIndex).setSelected(false);
                    titles.get(position).setSelected(true);
                    currentIndex = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setServerData(HouseDetail model) {
        if (null != model) {
            this.houseDetail = model;
            this.tvAddress.setText(this.houseDetail.getAddress());
            this.tvHouseType.setText(this.houseDetail.getHouseType());
            this.tvForceType.setText(this.houseDetail.getForceType());
            this.tvAvgPrice.setText(this.houseDetail.getAvgPrice());
            this.tvPhone.setText(this.houseDetail.getPhone());
            this.textViews.get(0).setText(this.houseDetail.getRecReason());
            this.textViews.get(1).setText(this.houseDetail.getTrafficLines());
            this.textViews.get(2).setText(this.houseDetail.getDesignIdea());
            this.loadImage(houseImageView, this.houseDetail.getUrl());
            hideDialog();
            showViews();
            if (null != houseDetail.getHelper() && PRODUCT) {
                Toast.makeText(this, houseDetail.getHelper().getName() + houseDetail.getHelper().getHxID(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchDataFromServer() {
        Map<String, String> params = new HashMap<>();
        params.put(FLAG_ID, String.valueOf(id));
        this.client.post(HOUSE_DETAIL_URL, new RequestParams(params), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                parseServerData(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void parseServerData(int statusCode, final JSONObject response) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            final ServerResult result = ServerUtils.parseServerResponse(response, ServerResultType.Object);
            if (ServerResult.CODE_SUCCESS == result.code) {
                MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, HouseDetail.class){
                    @Override
                    public void onSuccess(BaseModel model) {
                        setServerData((HouseDetail)model);
                    }
                });
            } else {
                handleCode(result.code, TAG);
            }
        } else {
            handleFailure();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.house_image_view) {
            Intent showOriImg = new Intent(this, PhotoViewActivity.class);
            if (!PRODUCT) {
                if (null != houseDetail) {
                    showOriImg.putExtra(PhotoViewActivity.FLAG_IMAGE_URL, Constants.IMAGE_URL + houseDetail.getUrl());
                    startActivity(showOriImg);
                }
            }
        } else {
            int index = ((ViewPagerTitle) v).getIndex();
            if (index != currentIndex) {
                titles.get(currentIndex).setSelected(false);
                titles.get(index).setSelected(true);
                currentIndex = index;
                viewPager.setCurrentItem(index);
            }
        }
    }

}
