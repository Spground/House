package jc.house.activities;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.fragments.ActivityFragment;
import jc.house.fragments.ChatFragment;
import jc.house.fragments.HouseFragment;
import jc.house.fragments.JCBaseFragment;
import jc.house.fragments.NewsFragment;
import jc.house.views.TabViewItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

//wujie 2015/10/29 20:15
public class HomeActivity extends FragmentActivity implements OnClickListener {
	private List<TabViewItem> tabViewItems;
	private List<Fragment> fragments;
	private ViewPager viewPager;
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		this.tabViewItems = new ArrayList<TabViewItem>(5);
		TabViewItem chatItem = (TabViewItem) this.findViewById(R.id.first_page);
		chatItem.setSelectedResId(R.drawable.chat_selected);
		chatItem.setNormalResId(R.drawable.chat);
		chatItem.setSelected(true);
		chatItem.setTabName("首页");
		chatItem.setIndex(0);
		chatItem.setOnClickListener(this);
		tabViewItems.add(chatItem);
		TabViewItem frendsItem = (TabViewItem) this.findViewById(R.id.building);
		frendsItem.setSelectedResId(R.drawable.chat_selected);
		frendsItem.setNormalResId(R.drawable.chat);
		frendsItem.setTabName("楼盘");
		frendsItem.setSelected(false);
		frendsItem.setIndex(1);
		frendsItem.setOnClickListener(this);
		tabViewItems.add(frendsItem);
		TabViewItem activityItem = (TabViewItem) this.findViewById(R.id.activities);
		activityItem.setSelectedResId(R.drawable.chat_selected);
		activityItem.setNormalResId(R.drawable.chat);
		activityItem.setTabName("活动");
		activityItem.setSelected(false);
		activityItem.setIndex(2);
		activityItem.setOnClickListener(this);
		tabViewItems.add(activityItem);
		TabViewItem findingItem = (TabViewItem) this.findViewById(R.id.chat);
		findingItem.setSelectedResId(R.drawable.chat_selected);
		findingItem.setNormalResId(R.drawable.chat);
		findingItem.setTabName("聊天");
		findingItem.setSelected(false);
		findingItem.setIndex(3);
		findingItem.setOnClickListener(this);
		tabViewItems.add(findingItem);
		TabViewItem meItem = (TabViewItem) this.findViewById(R.id.me);
		meItem.setSelectedResId(R.drawable.chat_selected);
		meItem.setNormalResId(R.drawable.chat);
		meItem.setTabName("关于");
		meItem.setSelected(false);
		meItem.setIndex(4);
		meItem.setOnClickListener(this);
		tabViewItems.add(meItem);
		this.fragments = new ArrayList<Fragment>(4);
		NewsFragment newsFragment = new NewsFragment();
		this.fragments.add(newsFragment);
		HouseFragment houseFragment = new HouseFragment();
		this.fragments.add(houseFragment);
		ActivityFragment activityFragment = new ActivityFragment();
		this.fragments.add(activityFragment);
		ChatFragment chatFragment3 = new ChatFragment();
		this.fragments.add(chatFragment3);
		ChatFragment chatFragment4 = new ChatFragment();
		this.fragments.add(chatFragment4);
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

		});
		this.viewPager.setOnPageChangeListener(new OnPageChangeListener() {

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
		this.currentIndex = 0;
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

	@Override
	public void onClick(View v) {
		TabViewItem item = (TabViewItem) v;
		int index = item.getIndex();
		if (currentIndex != index) {
			viewPager.setCurrentItem(index, false);
		} else {
			((JCBaseFragment) (fragments.get(index))).refresh();
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

}
