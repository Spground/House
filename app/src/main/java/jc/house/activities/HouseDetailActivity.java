package jc.house.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.easemob.chat.EMChatManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import jc.house.R;
import jc.house.async.FetchServer;
import jc.house.async.MThreadPool;
import jc.house.async.ModelTask;
import jc.house.async.ModelsTask;
import jc.house.async.ParseTask;
import jc.house.async.StringTask;
import jc.house.chat.ChatActivity;
import jc.house.global.Constants;
import jc.house.models.BaseModel;
import jc.house.models.House;
import jc.house.models.HouseDetail;
import jc.house.models.ServerResult;
import jc.house.utils.LogUtils;
import jc.house.utils.ServerUtils;
import jc.house.utils.StringUtils;
import jc.house.views.CircleView;
import jc.house.views.MViewPager;
import jc.house.views.ViewPagerTitle;

public class HouseDetailActivity extends BaseNetActivity implements View.OnClickListener, CircleView.CircleViewOnClickListener {
    public static final String HOUSE_DETAIL_URL = Constants.SERVER_URL + "house/detail";
    public static final String FLAG_ID = "id";
    public static final String FLAG_HOUSE_DETAIL = "HouseDetail";
    public static final String FLAG_HELPER_ID = "HelperID";
    public static final String FLAG_HELPER_NAME = "HelperName";
    private static final String TAG = "HouseDetailActivity";
    private static final int[] ids = {R.id.recommend, R.id.traffic, R.id.design};
    private static Map<Integer, WeakReference<HouseDetail>> houseDetailCache = new HashMap<>();
    private MViewPager viewPager;
    private List<TextView> textViews;
    private List<ViewPagerTitle> titles;
    private HouseDetail houseDetail;
    private TextView mapTextView;
    private TextView chatTextView;
    private int currentIndex;
    private CircleView circleView;
    private TextView tvAddress;
    private TextView tvHouseType;
    private TextView tvForceType;
    private TextView tvAvgPrice;
    private TextView tvPhone;
    private int id = -1;
    private String hxID;
    private String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_house_detail);
        initViews();
        initViewPager();
        this.setScrollRightBack(true);
        if (!PRODUCING) {
            id = this.getIntent().getIntExtra(FLAG_ID, -1);
            if (id >= 0) {
                //getCache first
                this.houseDetail = getCache(id);
                if (this.houseDetail != null)
                    setServerData(houseDetail);
                else {
                    //cache miss
                    showDialog();
                    hideViews();
                    fetchDataFromServer();
                }
            } else {
                houseDetail = this.getIntent().getParcelableExtra(FLAG_HOUSE_DETAIL);
                this.hxID = this.getIntent().getStringExtra(FLAG_HELPER_ID);
                this.nickName = this.getIntent().getStringExtra(FLAG_HELPER_NAME);
                setServerData(houseDetail);
            }
        }
    }

    /**
     * 取缓存houseDetail
     *
     * @param id
     * @return
     */
    private HouseDetail getCache(int id) {
        LogUtils.debug("===HouseDetailActivity===", "getCache id is " + id);
        if (houseDetailCache.get(id) == null)
            return null;
        return (houseDetailCache.get(id)).get();
    }

    /**
     * 缓存HouseDetail
     *
     * @param id
     * @param houseDetail
     */
    private void putCache(int id, HouseDetail houseDetail) {
        WeakReference<HouseDetail> item = new WeakReference<>(houseDetail);
        houseDetailCache.put(id, item);
    }

    private void initViews() {
        this.circleView = (CircleView) this.findViewById(R.id.house_circle_view);
        this.circleView.setTimeInterval(5.0f);
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
                if (PRODUCING) {
                    intent.putExtra(MapActivity.FLAG_HOUSE, new House(12, "123", "456", "789", "hello", 39.70, 116.445));
                } else {
                    intent.putExtra(MapActivity.FLAG_IsSingleMarker, true);
                    intent.putExtra(MapActivity.FLAG_HOUSE, houseDetail);
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
                //判断不能和自己聊天
                if(!canChat()) {
                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(HouseDetailActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("你不能和自己聊天！");
                    dialog.show();
                    return;
                }
                Intent intent = new Intent(HouseDetailActivity.this, ChatActivity.class);
                if (!PRODUCING) {
                    if (id >= 0 && null != houseDetail && null != houseDetail.getHelper()) {
                        LogUtils.debug(TAG, "helper huanxinID is " + houseDetail.getHelper().getHxID().trim());
                        intent.putExtra("toChatUserName", houseDetail.getHelper().getHxID());
                        intent.putExtra("nickName", houseDetail.getHelper().getName());
                        intent.putExtra(ChatActivity.EXTRA_HOUSE, houseDetail);
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

    /**
     * 检查是否可以聊天，自己和自己不能聊天
     * @return
     */
    private boolean canChat() {
        String curUser = EMChatManager.getInstance().getCurrentUser().trim();
        if (id >= 0 && null != houseDetail && null != houseDetail.getHelper()) {
            LogUtils.debug(TAG, "helper huanxinID is " + houseDetail.getHelper().getHxID().trim());
            if(houseDetail.getHelper().getHxID().trim().equalsIgnoreCase(curUser))
                return false;
        } else if (!StringUtils.strEmpty(hxID) && !StringUtils.strEmpty(nickName)) {
            if(hxID.equalsIgnoreCase(curUser))
                return false;
        }
        return true;
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
        this.viewPager = (MViewPager) this.findViewById(R.id.house_detail_viewpager);
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
        if (null == model) {
            return;
        }
        this.houseDetail = model;
        putCache(model.getId(), model);
        hideDialog();
        showViews();
        this.tvAddress.setText(this.houseDetail.getAddress());
        this.tvHouseType.setText(this.houseDetail.getHouseType());
        this.tvForceType.setText(this.houseDetail.getForceType());
        this.tvAvgPrice.setText(this.houseDetail.getAvgPrice());
        this.tvPhone.setText(this.houseDetail.getPhone());
        this.textViews.get(0).setText(this.houseDetail.getRecReason());
        this.textViews.get(1).setText(this.houseDetail.getTrafficLines());
        this.textViews.get(2).setText(this.houseDetail.getDesignIdea());
        this.circleView.setImageUrls(houseDetail.getImageUrls());
        this.circleView.setOnCircleViewItemClickListener(this);
        this.circleView.setTimeInterval(5.0f);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                circleView.setAutoPlay(true);
            }
        }, 2000);
    }

    private void fetchDataFromServer() {
        Map<String, String> params = new HashMap<>();
        params.put(FLAG_ID, String.valueOf(id));
        FetchServer.share().postModelFromServer(HOUSE_DETAIL_URL, params, HouseDetail.class, new ModelTask() {
            @Override
            public void onSuccess(BaseModel model, ServerResult result) {
                super.onSuccess(model, result);
                setServerData((HouseDetail)model);
            }

            @Override
            public void onFail(String msg) {
                super.onFail(msg);
                handleFailure();
            }

            @Override
            public void onCode(int code) {
                super.onCode(code);
                handleCode(code, TAG);
            }
        });
        /*
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
        */
    }

//    private void parseServerData(int statusCode, final JSONObject response) {
//        if (ServerUtils.isConnectServerSuccess(statusCode, response)) {
//            final ServerResult result = ServerUtils.parseServerResponse(response, ServerResult.Type.Object);
//            if (result.isSuccess) {
//                MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, HouseDetail.class) {
//                    @Override
//                    public void onSuccess(BaseModel model) {
//                        HouseDetail hModel = (HouseDetail) model;
//                        setServerData(hModel);
//                    }
//                });
//            } else {
//                handleCode(result.code, TAG);
//            }
//        } else {
//            handleFailure();
//        }
//    }

    @Override
    public void finish() {
        this.circleView.cancelTimer();
        LogUtils.debug("===" + TAG + "===", "HouseDetailActivity onFinish() is invoked!");
        super.finish();
    }

    @Override
    protected void onDestroy() {
        LogUtils.debug("===" + TAG + "===", "HouseDetailActivity onDestroy() is invoked!");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getClass().isAssignableFrom(ViewPagerTitle.class)) {
            int index = ((ViewPagerTitle) v).getIndex();
            if (index != currentIndex) {
                titles.get(currentIndex).setSelected(false);
                titles.get(index).setSelected(true);
                currentIndex = index;
                viewPager.setCurrentItem(index);
            }
        }
    }

    @Override
    public void onCircleViewItemClick(View v, int index) {
        if (null != houseDetail.getImageUrls() && index < houseDetail.getImageUrls().length) {
            Intent intent = new Intent(this, PhotoViewActivity.class);
//            String[] single = new String[1];
//            single[0] = (houseDetail.getImageUrls())[index];
            intent.putExtra(PhotoViewActivity.FLAG_IMAGE_URL, houseDetail.getImageUrls());
//            intent.putExtra(PhotoViewActivity.FLAG_IMAGE_URL, single);
            intent.putExtra(PhotoViewActivity.FLAG_CURRENT_INDEX, index);
            startActivity(intent);
        }
    }
}
