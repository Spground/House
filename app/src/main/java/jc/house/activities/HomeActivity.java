package jc.house.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import jc.house.R;
import jc.house.async.MThreadPool;
import jc.house.async.ParseTask;
import jc.house.chat.service.ReceiveNewMessageService;
import jc.house.fragments.AboutFragment;
import jc.house.fragments.ActivityFragment;
import jc.house.fragments.ChatFragment;
import jc.house.fragments.HouseFragment;
import jc.house.fragments.NewsFragment;
import jc.house.global.Constants;
import jc.house.global.MApplication;
import jc.house.global.ServerResultType;
import jc.house.interfaces.IRefresh;
import jc.house.models.BaseModel;
import jc.house.models.CustomerHelper;
import jc.house.models.ServerResult;
import jc.house.utils.GeneralUtils;
import jc.house.utils.LogUtils;
import jc.house.utils.ServerUtils;
import jc.house.utils.StringUtils;
import jc.house.utils.ToastUtils;
import jc.house.views.TabViewItem;

//hzj 2015/10/29 20:15
public class HomeActivity extends FragmentActivity implements OnClickListener, ChatFragment.OnNewMessageReceivedListener {
    private static final int EXIT_TIME_SPAN = 2000;
    private static final int TAB_ITEMS_NUM = 5;
    private static final String TAG = "HomeActivity";
    private static final String[] tabNames = {"首页", "楼盘", "活动", "聊天", "关于"};
    private static final int[] selectedResIds = {R.drawable.tab_home_selected,
            R.drawable.tab_building_selected,
            R.drawable.tab_activity_selected,
            R.drawable.tab_chat_selected,
            R.drawable.tab_about_selected};
    private static final int[] normalResIds = {R.drawable.tab_home_normal,
            R.drawable.tab_building_normal,
            R.drawable.tab_activity_normal,
            R.drawable.tab_chat_normal,
            R.drawable.tab_about_normal};
    private static final int[] tabItemIds = {R.id.first_page, R.id.building, R.id.activities, R.id.chat, R.id.me};
    public static boolean isNetAvailable;
    private List<TabViewItem> tabViewItems;
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private int currentIndex;
    private long lastTime;
    private ConnectivityManager netConnectManager;
    private IntentFilter filter;
    private MyReceiver mReceiver;

    private final String REGISTER_INFO = Constants.PREFERENCESNAME.RegisterInfo;
    private final String HUANXINID_KEY = "huanxinid";
    private final String PWD_KEY = "pwd";
    private final String DEFAULT_PWD = Constants.DEFAULT_PWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSharedPreferences(REGISTER_INFO, 0);
        setContentView(R.layout.activity_home);
        this.currentIndex = 0;
        this.initTabViewItems();
        this.initFragments();
        this.lastTime = 0;
        this.initViewPager();
        this.initNetConnectManager();
        startUpReceiveNewMessageService();
        //获取客服环信ID-Model映射表
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getCustomerHelperNickName();
            }
        }, 0);
    }

    /**
     * 获取客服==>环信ID名称映射规则
     */
    private void getCustomerHelperNickName() {
        LogUtils.debug(TAG, "getCustomerHelperNickName");
        AsyncHttpClient client = new AsyncHttpClient();
        //get cache first
        loadDataFromLocal(CustomerHelper.class);
        client.post(Constants.CUSTOMER_HELPER_NAME_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.debug(TAG, "onSuccess");
                if (!ServerUtils.isConnectServerSuccess(statusCode, response))
                    return;
                ServerResult result = ServerUtils.parseServerResponse(response, ServerResultType.Array);
                if (!result.isSuccess)
                    return;
                //cache to local
                saveToLocal(result.array.toString(), CustomerHelper.class);
                MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, CustomerHelper.class) {
                    @Override
                    public void onSuccess(List<? extends BaseModel> models) {
                        super.onSuccess(models);
                        //populate the customer helper mapping
                        for (BaseModel model : models) {
                            CustomerHelper c = (CustomerHelper) model;
                            ((MApplication) getApplication()).customerHelperNameMapping.put(c.getHxID(), c);
                        }
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.debug(TAG, statusCode + responseString);
            }
        });
    }

    /**
     * 将jsonStr缓存到本地
     *
     * @param jsonStr
     * @param cls
     */
    private void saveToLocal(String jsonStr, Class<? extends BaseModel> cls) {
        LogUtils.debug("===jsonStr===", jsonStr);
        ((MApplication) getApplicationContext()).saveJsonString(jsonStr, cls);
    }

    /**
     * 将本地的jsonStr缓存数据取出来
     */
    private boolean loadDataFromLocal(Class<? extends BaseModel> cls) {
        String content = ((MApplication) getApplicationContext()).getJsonString(cls);
        if (!StringUtils.strEmpty(content)) {
            ServerResult result = new ServerResult();
            try {
                result.array = new JSONArray(content);
                result.resultType = ServerResultType.Array;
                MThreadPool.getInstance().submitParseDataTask(new ParseTask(result, cls) {
                    @Override
                    public void onSuccess(List<? extends BaseModel> models) {
                        for (BaseModel model : models) {
                            CustomerHelper customerHelper = (CustomerHelper) model;
                            if (customerHelper == null)
                                continue;
                            ((MApplication) getApplicationContext()).customerHelperNameMapping.put(customerHelper.getHxID(), customerHelper);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtils.debug("===TAG===", "customer helper content is + " + content + cls.toString());
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initTabViewItems() {
        this.tabViewItems = new ArrayList<>(TAB_ITEMS_NUM);
        for (int i = 0; i < TAB_ITEMS_NUM; i++) {
            TabViewItem item = (TabViewItem) this.findViewById(tabItemIds[i]);
            item.setTabName(tabNames[i]);
            item.setSelectedResId(selectedResIds[i]);
            item.setNormalResId(normalResIds[i]);
            item.setSelected(this.currentIndex == i);
            item.setIndex(i);
            item.setOnClickListener(this);
            tabViewItems.add(item);
        }
    }

    private void initViewPager() {
        this.viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        this.viewPager.setOffscreenPageLimit(2);
        this.viewPager.setAdapter(new FragmentPagerAdapter(this
                .getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int pos) {
                return fragments.get(pos);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int pos) {
                LogUtils.debug(TAG, "instantiateItem pos is " + pos);
                return super.instantiateItem(container, pos);
            }

            @Override
            public void destroyItem(ViewGroup container, int pos, Object object) {
                LogUtils.debug(TAG, "destroyItem pos is " + pos);
                //super.destroyItem(container, pos, object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return super.isViewFromObject(view, object);
            }
        });
        this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int pos) {
            }

            @Override
            public void onPageScrolled(int current, float rate, int des) {

            }

            @Override
            public void onPageSelected(int pos) {
                /**set little red dot invisible**/
                tabViewItems.get(pos).hideLittleRedDot();
                if (pos != currentIndex) {
                    tabViewItems.get(pos).setSelected(true);
                    tabViewItems.get(currentIndex).setSelected(false);
                    currentIndex = pos;
                }
            }

        });

        /**如果是用户版**/
        if (Constants.APPINFO.USER_VERSION) {
            if (checkRegister()) {
                SharedPreferences prf = getSharedPreferences(REGISTER_INFO, 0);
                String huanxinid = prf.getString(HUANXINID_KEY, null);
                String pwd = prf.getString(PWD_KEY, null);
                if (huanxinid == null || pwd == null) {

                } else {
                    loginHuanXin(huanxinid, pwd);
                }
            } else {
                String huanxinid = GeneralUtils.getSystemIdentity();
                String pwd = DEFAULT_PWD;
                register(huanxinid, pwd);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    private void initFragments() {
        this.fragments = new ArrayList<>(TAB_ITEMS_NUM);
        this.fragments.add(new NewsFragment());
        this.fragments.add(new HouseFragment());
        this.fragments.add(new ActivityFragment());
        this.fragments.add(new ChatFragment());
        this.fragments.add(new AboutFragment());
    }

    @Override
    public void onClick(View v) {
        TabViewItem item = (TabViewItem) v;
        int index = item.getIndex();
        /**set little red dot invisible**/
        //tabViewItems.get(index).showLittleRedDot();
        if (currentIndex != index) {
            viewPager.setCurrentItem(index, false);
        } else {
            ((IRefresh) (fragments.get(index))).refresh();
        }
        currentIndex = index;
        if(index == 3)
            ((IRefresh) (fragments.get(index))).refresh();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initNetConnectManager() {
        this.netConnectManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        this.filter = new IntentFilter();
        this.filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //add new message broadcast action
        this.filter.addAction(EMChatManager.getInstance().getNewMessageBroadcastAction());
        //增加账号冲突的广播
        this.filter.addAction(MApplication.CONNECTION_CONFLICT);
        this.mReceiver = new MyReceiver();
        this.registerReceiver(this.mReceiver, this.filter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastTime) <= EXIT_TIME_SPAN) {
                //remember to stop service while exiting the app
                stopService(new Intent(this, ReceiveNewMessageService.class));
                finish();
            } else {
                lastTime = currentTime;
                Toast.makeText(HomeActivity.this, "再次点击退出应用", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(this.mReceiver);
        super.onDestroy();
    }

    /**
     * 登录环信
     */
    private void loginHuanXin(final String huanxinid, final String pwd) {

        /**login huanxin**/
        EMChatManager.getInstance().login(huanxinid, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        LogUtils.debug(TAG, "登陆聊天服务器成功！");
                        LogUtils.debug(TAG, "login succeed huanxin id is " + huanxinid + " pwd is " + pwd);
                        //登录成功加载所有的数据库记录到内存
                        EMChatManager.getInstance().loadAllConversations();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                LogUtils.debug(TAG, "登陆聊天服务器失败！");
            }
        });
    }

    /**
     * startup service to receive new message
     */
    private void startUpReceiveNewMessageService() {
        Intent intent = new Intent(this, ReceiveNewMessageService.class);
        startService(intent);
        LogUtils.debug(TAG, "ReceiveNewMessageService is starting up...");
    }

    /**
     * check isRegister
     *
     * @return
     */
    private boolean checkRegister() {
        SharedPreferences prf = this.getSharedPreferences(REGISTER_INFO, 0);
        String huanxinid = prf.getString(HUANXINID_KEY, null);
        if (huanxinid == null)
            return false;
        else
            return true;
    }

    /**
     * 注册
     *
     * @param huanxinid
     * @param pwd
     */
    private void register(final String huanxinid, final String pwd) {
        MThreadPool.getInstance().getExecutorService().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    EMChatManager.getInstance().createAccountOnServer(huanxinid, pwd);
                    //write to shared preference if register succeed
                    SharedPreferences prf = getSharedPreferences(REGISTER_INFO, 0);
                    SharedPreferences.Editor editor = prf.edit();
                    editor.putString(HUANXINID_KEY, huanxinid);
                    editor.putString(PWD_KEY, pwd);
                    editor.commit();
                    //login when register succeed
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginHuanXin(huanxinid, pwd);
                        }
                    });
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    //register failed
                    int errorCode = e.getErrorCode();
                    //set preferences`s value as null if register failed
                    SharedPreferences prf = getSharedPreferences(REGISTER_INFO, 0);
                    SharedPreferences.Editor editor = prf.edit();
                    if (prf.getString(HUANXINID_KEY, null) != null) {
                        editor.putString(HUANXINID_KEY, null);
                        editor.putString(PWD_KEY, null);
                        editor.commit();
                    }

                    if (errorCode == EMError.NONETWORK_ERROR) {
                        ToastUtils.show(getApplicationContext(), "网络异常，请检查网络！");
                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                        LogUtils.debug("===HomeActivity===", "用户已存在");
                    } else if (errorCode == EMError.UNAUTHORIZED) {
                        LogUtils.debug("===HomeActivity===", "注册失败，无权限");
                    } else {
                        LogUtils.debug("===HomeActivity===", "注册失败");
                    }
                }
            }
        });
    }

    @Override
    public void onNewMessageReceived() {
        /**update tab item's unread**/
        if (currentIndex != 3)
            tabViewItems.get(3).showLittleRedDot();
    }

    /**
     * 广播接收者
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    NetworkInfo active = netConnectManager.getActiveNetworkInfo();
                    if (null == active || !active.isConnected()) {
                        isNetAvailable = false;
                        LogUtils.debug(TAG, "NetWork is unConnected!");
                    } else {
                        isNetAvailable = true;
                        LogUtils.debug(TAG, "NetWork is connected!");
                    }
                    break;
                case MApplication.CONNECTION_CONFLICT:
                    LogUtils.debug(TAG, "账号冲突");
                    ToastUtils.show(getApplicationContext(), "您的账号在别的设备登录");
                    Intent it = new Intent(HomeActivity.this, CustomerHelperLoginActivity.class);
                    startActivity(it);
                    //销毁所有的Activity
                    for(WeakReference<Activity> activityWeakReference : ((MApplication)getApplicationContext()).loadedActivities) {
                        Activity activity = activityWeakReference.get();
                        if(activity != null)
                            activity.finish();
                    }
                    break;
                default:
                    LogUtils.debug(TAG, "intent action is " + intent.getAction());
                    break;
            }

        }
    }
}
