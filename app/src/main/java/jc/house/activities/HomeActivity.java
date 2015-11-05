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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.fragments.ActivityFragment;
import jc.house.fragments.ChatFragment;
import jc.house.fragments.HouseFragment;
import jc.house.fragments.NewsFragment;
import jc.house.global.Constants;
import jc.house.interfaces.IRefresh;
import jc.house.views.TabViewItem;

//hzj 2015/10/29 20:15
public class HomeActivity extends FragmentActivity implements OnClickListener {
	private List<TabViewItem> tabViewItems;
	private List<Fragment> fragments;
	private ViewPager viewPager;
	private int currentIndex;
	private long lastTime;
	private static final int EXIT_TIME_SPAN = 2000;
	private static final int TAB_ITEMS_NUM = 5;

	private ConnectivityManager netConnectManager;
	private IntentFilter filter;
	private MyReceiver mReceiver;
	public static boolean isNetAvailable;

	private static final boolean DEBUG = Constants.DEBUG;
	private static final String TAG = "HomeActivity";
	private static final String[] tabNames = {"首页", "楼盘", "活动", "聊天", "关于"};
	private static final int[] selectedResIds = {R.drawable.chat_selected, R.drawable.chat_selected, R.drawable.chat_selected, R.drawable.chat_selected, R.drawable.chat_selected};
	private static final int[] normalResIds = {R.drawable.chat, R.drawable.chat, R.drawable.chat, R.drawable.chat, R.drawable.chat, R.drawable.chat};
	private static final int[] tabItemIds= {R.id.first_page, R.id.building, R.id.building, R.id.activities, R.id.chat, R.id.me};

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void initTabViewItems() {
		this.tabViewItems = new ArrayList<TabViewItem>(TAB_ITEMS_NUM);
		for(int i = 0; i< TAB_ITEMS_NUM; i++) {
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
				if (DEBUG) {
					Log.i(TAG, "instantiateItem pos is " + pos);
				}
				return super.instantiateItem(container, pos);
			}

			@Override
			public void destroyItem(ViewGroup container, int pos, Object object) {
				if (DEBUG) {
					Log.i(TAG, "destroyItem pos is " + pos);
				}
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
				if (pos != currentIndex) {
					tabViewItems.get(pos).setSelected(true);
					tabViewItems.get(currentIndex).setSelected(false);
					currentIndex = pos;
				}
			}

		});
	}

	private void initFragments() {
		this.fragments = new ArrayList<Fragment>(TAB_ITEMS_NUM);
		this.fragments.add(new NewsFragment());
		this.fragments.add(new HouseFragment());
		this.fragments.add(new ActivityFragment());
		this.fragments.add(new ChatFragment());
		this.fragments.add(new ChatFragment());
	}

	@Override
	public void onClick(View v) {
		TabViewItem item = (TabViewItem) v;
		int index = item.getIndex();
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
		this.netConnectManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		this.filter = new IntentFilter();
		this.filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.mReceiver = new MyReceiver();
		this.registerReceiver(this.mReceiver, this.filter);
	}

	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch(intent.getAction()) {
				case ConnectivityManager.CONNECTIVITY_ACTION:
					NetworkInfo active = netConnectManager.getActiveNetworkInfo();
					if(null == active || !active.isConnected()) {
						isNetAvailable = false;
						if(DEBUG) {
							Log.i(TAG, "NetWork is unConnected!");
						}
					} else {
						isNetAvailable = true;
						if(DEBUG) {
							Log.i(TAG, "NetWork is connected!");
						}
					}
					break;
				default:
					break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastTime) <= EXIT_TIME_SPAN) {
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
}
