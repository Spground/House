package jc.house.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.chat.ChatActivity;
import jc.house.views.ViewPagerTitle;
import jc.house.views.MViewPager;
import jc.house.views.TitleBar;

public class HouseDetailActivity extends BaseActivity implements View.OnClickListener {
//	private TitleBar titleBar;
	private MViewPager viewPager;
	private List<TextView> textViews;
	private List<ViewPagerTitle> titles;
	private TextView mapTextView;
	private TextView chatTextView;
	private int currentIndex;
	private static final String TAG = "HouseDetailActivity";
	private static final int[] ids = {R.id.recommend, R.id.traffic, R.id.design};

	private ImageView houseImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setJCContentView(R.layout.activity_house_detail);
		this.houseImageView = (ImageView)findViewById(R.id.house_image_view);
		this.houseImageView.setOnClickListener(this);
//		this.titleBar = (TitleBar) this.findViewById(R.id.titlebar);
		this.mapTextView = (TextView)this.getLayoutInflater().inflate(R.layout.div_titlebar_rightview, null);
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
		this.setTitleBarTitle("楼盘详情");
		this.chatTextView = (TextView)this.findViewById(R.id.chat);
		this.chatTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到聊天页面
				Intent intent = new Intent(HouseDetailActivity.this, ChatActivity.class);
						intent.putExtra("toChatUserName", "admin");
				startActivity(intent);
			}
		});
		this.viewPager = (MViewPager)this.findViewById(R.id.viewpager);
		this.currentIndex = 0;
		this.titles = new ArrayList<>(3);
		for (int i = 0; i< 3; i++) {
			ViewPagerTitle title = (ViewPagerTitle)this.findViewById(ids[i]);
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

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.house_image_view){
			Intent showOriImg = new Intent(this, PhotoViewActivity.class);
			showOriImg.putExtra("image_url", "http://www.jinchenchina.cn/uploads/allimg/150710/0-150G0124350951.jpg");
			startActivity(showOriImg);
			return;
		}
		int index = ((ViewPagerTitle)v).getIndex();
		if (index != currentIndex) {
			titles.get(currentIndex).setSelected(false);
			titles.get(index).setSelected(true);
			currentIndex = index;
			viewPager.setCurrentItem(index);
		}
	}

}
