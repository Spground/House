package jc.house.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;
import jc.house.R;
import jc.house.async.MThreadPool;
import jc.house.chat.ChatActivity;
import jc.house.global.Constants;
import jc.house.models.CustomerHelper;
import jc.house.models.House;
import jc.house.models.HouseDetail;
import jc.house.models.ServerResult;
import jc.house.utils.LogUtils;
import jc.house.utils.ParseJson;
import jc.house.utils.ServerUtils;
import jc.house.views.MViewPager;
import jc.house.views.ViewPagerTitle;

public class HouseDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "HouseDetailActivity";
    private static final String URL = Constants.SERVER_URL + "house/detail";
    private static final int[] ids = {R.id.recommend, R.id.traffic, R.id.design};
    //	private TitleBar titleBar;
    private MViewPager viewPager;
    private List<TextView> textViews;
    private List<ViewPagerTitle> titles;
    private HouseDetail houseDetail;
    private TextView mapTextView;
    private TextView chatTextView;
    private int currentIndex;
    private ImageView houseImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_house_detail);
        if (!PRODUCT) {
            fetchDataFromServer();
        }
        initViews();
        initViewPager();
    }

    private void initViews() {
        this.houseImageView = (ImageView) findViewById(R.id.house_image_view);
        this.houseImageView.setOnClickListener(this);
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
                    intent.putExtra(MapActivity.FLAG_HOUSE, new House(12, "123", "456", "789", "hello", 123.12, 123.23));
                } else {
                    //TODO 跳转
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
                intent.putExtra("toChatUserName", "admin");
                startActivity(intent);
            }
        });
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
            textView.setText("NBA卫冕冠军库里在新赛季依旧有着高光的发挥，他带领勇士队在新赛季获得16连胜，风头正劲的库里在NBA中的地位就如同梅西在足球界的地位。");
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

    private void setServerData() {
        Toast.makeText(this, this.houseDetail.getPhone() + this.houseDetail.getHelper().getName() + this.houseDetail.getHelper().getHxID(), Toast.LENGTH_SHORT).show();
    }

    private void fetchDataFromServer() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        this.getHttpClient().post(URL, new RequestParams(params), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                parseServerData(statusCode, response);
                LogUtils.debug("houseDetail", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.debug("houseDetail", responseString);
            }
        });
    }

    private void parseServerData(int statusCode, final JSONObject response) {
        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
            int code = 0;
            try {
                code = response.getInt(ServerResult.CODE);
                if (ServerResult.CODE_SUCCESS == code) {
                    MThreadPool.getInstance().getExecutorService().submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                houseDetail = (HouseDetail) ParseJson.jsonObjectToBaseModel(response.getJSONObject(ServerResult.RESULT), HouseDetail.class);
                                houseDetail.setHelper((CustomerHelper) ParseJson.jsonObjectToBaseModel(response.getJSONObject(ServerResult.HELPER), CustomerHelper.class));
                                new Handler(getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setServerData();
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.house_image_view) {
            Intent showOriImg = new Intent(this, PhotoViewActivity.class);
            showOriImg.putExtra("image_url", "http://www.jinchenchina.cn/uploads/allimg/150710/0-150G0124350951.jpg");
            startActivity(showOriImg);
            return;
        }
        int index = ((ViewPagerTitle) v).getIndex();
        if (index != currentIndex) {
            titles.get(currentIndex).setSelected(false);
            titles.get(index).setSelected(true);
            currentIndex = index;
            viewPager.setCurrentItem(index);
        }
    }

}
