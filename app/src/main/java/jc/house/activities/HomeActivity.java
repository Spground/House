package jc.house.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.chat.service.ReceiveNewMessageService;
import jc.house.fragments.AboutFragment;
import jc.house.fragments.ActivityFragment;
import jc.house.fragments.ChatFragment;
import jc.house.fragments.HouseFragment;
import jc.house.fragments.NewsFragment;
import jc.house.global.Constants;
import jc.house.interfaces.IRefresh;
import jc.house.utils.LogUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.currentIndex = 0;
        this.initTabViewItems();
        this.initFragments();
        this.lastTime = 0;
        this.initViewPager();
        this.initNetConnectManager();
        startUpReceiveNewMessageService();
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
        this.viewPager.setOffscreenPageLimit(3);
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
                tabViewItems.get(pos).unlightLittleRedDot();
                if (pos != currentIndex) {
                    tabViewItems.get(pos).setSelected(true);
                    tabViewItems.get(currentIndex).setSelected(false);
                    currentIndex = pos;
                }
            }

        });
        this.currentIndex = 0;
        loginHuanXin();
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
        tabViewItems.get(index).unlightLittleRedDot();
        if (currentIndex != index) {
            viewPager.setCurrentItem(index, false);
        } else {
            ((IRefresh) (fragments.get(index))).refresh();
        }
        currentIndex = index;
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
//                ToastUtils.show(HomeActivity.this, "再次点击退出应用");
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
    private void loginHuanXin() {

        /**login huanxin**/
        EMChatManager.getInstance().login(Constants.ACCOUNT.Account, Constants.ACCOUNT.Pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        LogUtils.debug(TAG, "登陆聊天服务器成功！");
                        //登录成功加载所有的数据库记录到内存
                        EMGroupManager.getInstance().loadAllGroups();
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

    @Override
    public void onNewMessageReceived() {
        /**update tab item's unread**/
        if (currentIndex != 3)
            tabViewItems.get(3).lightLittleRedDot();
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
                default:
                    break;
            }

        }
    }
}
