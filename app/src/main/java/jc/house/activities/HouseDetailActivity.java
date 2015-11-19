package jc.house.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.views.JCTextView;
import jc.house.views.MViewPager;
import jc.house.views.TitleBar;

public class HouseDetailActivity extends Activity implements View.OnClickListener {
	private TitleBar titleBar;
	private MViewPager viewPager;
	private List<TextView> textViews;
	private List<JCTextView> titles;
	private TextView mapTextView;
	private int currentIndex;
	private static final String TAG = "HouseDetailActivity";
	private static final int[] ids = {R.id.recommend, R.id.traffic, R.id.design};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		this.titleBar = (TitleBar) this.findViewById(R.id.titlebar);
		this.mapTextView = (TextView)this.getLayoutInflater().inflate(R.layout.right_view, null);
//		this.mapTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
				startActivity(intent);
			}
		});
		this.titleBar.setRightChildView(mapTextView);
		this.titleBar.setTitle("楼盘详情");
		this.viewPager = (MViewPager)this.findViewById(R.id.viewpager);
		this.currentIndex = 0;
		this.titles = new ArrayList<>(3);
		for (int i = 0; i< 3; i++) {
			JCTextView title = (JCTextView)this.findViewById(ids[i]);
			title.setIndex(i);
			title.setSelected(i == currentIndex);
			title.setOnClickListener(this);
			titles.add(title);
		}
		this.textViews = new ArrayList<>(3);
		for (int i = 0; i < 3; i++) {
			final TextView textView = new TextView(this);
			textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			textView.setPadding(5, 1, 5, 5);
			if (i == 0) {
				textView.setText("\n" +
						"当你需要获取textview真正高度时\n" +
						"思考-设计-code\n" +

						"31省前三季度GDP之和超出全国1.9万亿， 广东GDP居首\n" +
						"扑克牌里“J、Q、K”，你们都叫什么？\n" +
						"算法和设计\n");
			} else {
				textView.setText("\n" +
						"当你需要获取textview真正高度时\n" +
						"31省前三季度GDP之和超出全国1.9万亿， 广东GDP居首\n" +
						"因此我们需要给textview绑定一个监听器\n" +
						"扑克牌里“J、Q、K”，你们都叫什么？\n" +
						"算法和设计\n" + "算法和设计\n" + "hello world!\n");
			}
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

	@Override
	public void onClick(View v) {
		int index = ((JCTextView)v).getIndex();
		if (index != currentIndex) {
			titles.get(currentIndex).setSelected(false);
			titles.get(index).setSelected(true);
			currentIndex = index;
			viewPager.setCurrentItem(index);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
