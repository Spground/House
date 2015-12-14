package jc.house.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jc.house.R;

public class CircleView extends LinearLayout {

	public interface CircleViewOnClickListener {
		void onCircleViewItemClick(View v, int index);
	}

	private boolean autoPlay;
	private float timeInterval;
	private ViewPager viewPager;
	private IndicatorView indicatorView;
	private int[] imageReIds;
	private String[] imageUrls;
	private List<ImageView> imageViews;
	private Context context;
	private int currentIndex;
	private int num;
	private MyHandler mHandler;
	private CircleViewOnClickListener circleClickListener;
	private Animation animation;
	private static final int CIRCLE_FLAG = 3;
	public static final int SCROLL_LEFT = 1;
	public static final int SCROLL_RIGHT = 2;
	private int scrollOrientaion;

	public CircleView(Context context) {
		super(context);
		this.initView(context);
	}

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
	}

	public CircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.div_circleview, this);
		this.viewPager = (ViewPager) this.findViewById(R.id.viewpager);
		this.indicatorView = (IndicatorView) this
				.findViewById(R.id.indicatorView);
		this.autoPlay = true;
		this.timeInterval = 0;
		this.num = 0;
		this.context = context;
		this.imageViews = new ArrayList<>();
		this.animation = new AlphaAnimation(0.8f, 1.0f);
		this.animation.setDuration(600);
		this.scrollOrientaion = SCROLL_RIGHT;
	}
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}
	

	public boolean isAutoPlay() {
		return autoPlay;
	}

	public void setAutoPlay(boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

	public float getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(float timeInterval) {
		if (timeInterval > 0) {
			this.timeInterval = timeInterval;
		}
	}

	public int[] getImageReIds() {
		return imageReIds;
	}

	public void setOnCircleViewItemClickListener(
			CircleViewOnClickListener circleClickListener) {
		this.circleClickListener = circleClickListener;
	}
	

	public int getScrollOrientaion() {
		return scrollOrientaion;
	}

	public void setScrollOrientaion(int scrollOrientaion) {
		if(scrollOrientaion <= SCROLL_RIGHT && scrollOrientaion >= SCROLL_LEFT) {
			this.scrollOrientaion = scrollOrientaion;
		}
	}

	public void setImageReIds(int[] imageReIds) {
		if (null != imageReIds && imageReIds.length > 0) {
			this.imageReIds = imageReIds;
			this.num = imageReIds.length;
			addImageViews(true);
		}
	}

	public String[] getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String[] imageUrls) {
		if (null != imageUrls && imageUrls.length > 0) {
			this.imageUrls = imageUrls;
			this.num = imageUrls.length;
			this.addImageViews(false);
		}
	}

	private void addImageViews(boolean isLocalRes) {
		for (int i = 0; i < num; i++) {
			ImageView imageView = new ImageView(this.context);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ScaleType.CENTER_CROP);
			if (isLocalRes) {
				imageView.setImageDrawable(this.getResources().getDrawable(
						imageReIds[i]));
			} else {
				ImageLoader.getInstance().displayImage(
						"http://avatar.csdn.net/E/8/F/1_hyr83960944.jpg",
						imageView);
			}
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != circleClickListener) {
						circleClickListener.onCircleViewItemClick(v, currentIndex);
					}
				}

			});
			this.imageViews.add(imageView);
		}
		this.viewPager.setAdapter(new CirclePagerAdapter());
		this.viewPager
				.addOnPageChangeListener(new CircleOnPageChangeListener());
		this.indicatorView.setNormalResId(R.drawable.indicator_normal);
		this.indicatorView.setSelectedResId(R.drawable.indicator_selected);
		this.indicatorView.setNum(num);
		if (this.autoPlay && null == this.mHandler && num > 1
				&& this.timeInterval > 0) {
			this.mHandler = new MyHandler(this);
			new Timer()
					.schedule(new TimerTask() {

						@Override
						public void run() {
							Message msg = new Message();
							msg.what = CIRCLE_FLAG;
							mHandler.sendMessage(msg);
						}

					}, (long) this.timeInterval * 1000,
							(long) this.timeInterval * 1000);
		}
	}

	private class CircleOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int pos) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int index) {
			indicatorView.setSelectedIndex(index);
		}

	}

	private class CirclePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int pos) {
			View view = imageViews.get(pos);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}

	}
	

	private void setCurrentIndex(int index) {
		if (index >= 0 && index < num) {
			this.viewPager.setCurrentItem(index);
			this.indicatorView.setSelectedIndex(index);
			this.currentIndex = index;
		}
	}

	private void circleAnimate() {
		this.startAnimation(animation);
	}
	
	private void circle() {
		if(autoPlay) {
			if(SCROLL_RIGHT == this.scrollOrientaion) {
				currentIndex = (currentIndex + 1) % num;
			} else {
				currentIndex -= 1;
				if(currentIndex < 0) {
					currentIndex = num - 1;
				}
			}
			setCurrentIndex(currentIndex);
			circleAnimate();
		}
	}

	private static final class MyHandler extends Handler {
		private WeakReference<CircleView> weakCircleView;

		public MyHandler(CircleView view) {
			this.weakCircleView = new WeakReference<>(view);
		}

		@Override
		public void handleMessage(Message msg) {
			CircleView circleView = this.weakCircleView.get();
			if (null != circleView) {
				switch (msg.what) {
					case CIRCLE_FLAG:
						if(circleView.autoPlay) {
							circleView.circle();
						}
						break;
					default:
						break;
				}
			}
		}
	}

}
