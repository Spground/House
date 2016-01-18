package jc.house.views;

import android.content.Context;
import android.os.CountDownTimer;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.global.Constants;

public class CircleView extends LinearLayout {

	public interface CircleViewOnClickListener {
		void onCircleViewItemClick(View v, int index);
	}

	public enum SCROLL_ORIENTATION {
		RIGHT,
		LEFT
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
	private CircleViewOnClickListener circleClickListener;
	private Animation animation;
	private SCROLL_ORIENTATION orientation;
	private TimerCircle timer;
	private boolean first;

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
		this.indicatorView.setNormalResId(R.drawable.indicator_normal);
		this.indicatorView.setSelectedResId(R.drawable.indicator_selected);
		this.autoPlay = true;
		this.timeInterval = 0;
		this.num = 0;
		this.context = context;
		this.imageViews = new ArrayList<>();
		this.animation = new AlphaAnimation(0.8f, 1.0f);
		this.animation.setDuration(600);
		this.orientation = SCROLL_ORIENTATION.RIGHT;
		this.first = true;
	}

	public void setAutoPlay(boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

	public void setTimeInterval(float timeInterval) {
		if (timeInterval > 0) {
			this.timeInterval = timeInterval;
		}
	}

	public void setOnCircleViewItemClickListener(
			CircleViewOnClickListener circleClickListener) {
		this.circleClickListener = circleClickListener;
	}

	public void setOrientation(SCROLL_ORIENTATION orientation) {
		this.orientation = orientation;
	}

	public void setImageReIds(int[] imageReIds) {
		synchronized (this) {
			if (null != imageReIds && imageReIds.length > 0 && first) {
				if (first) {
					this.imageReIds = imageReIds;
					this.num = imageReIds.length;
				}
				addImageViews(true);
			}
		}
	}

	public void setImageUrls(String[] imageUrls) {
		synchronized (this) {
			if (null != imageUrls && imageUrls.length > 0) {
				if (first) {
					this.imageUrls = imageUrls;
					this.num = imageUrls.length;
				}
				this.addImageViews(false);
			}
		}
	}

	private void addImageViews(boolean isLocalRes) {
		if (this.first) {
			this.imageViews.clear();
			if (null != timer) {
				timer.cancel();
			}
			for (int i = 0; i < num; i++) {
				ImageView imageView = new ImageView(this.context);
				imageView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				imageView.setScaleType(ScaleType.CENTER_CROP);
				if (isLocalRes) {
					imageView.setImageResource(imageReIds[i]);
				} else {
					loadImage(imageView, imageUrls[i]);
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
			this.indicatorView.setNum(num);
			this.viewPager.setAdapter(new CirclePagerAdapter());
			this.viewPager
					.addOnPageChangeListener(new CircleOnPageChangeListener());
			if (this.autoPlay && num > 1) {
				if (null == timer) {
					timer = new TimerCircle(System.currentTimeMillis() / 1000 + (long) timeInterval * 1000, (long) timeInterval * 1000, this);
				}
				timer.start();
			}
			first = false;
		} else {
			if (isLocalRes) {
				for (int i = 0; i < num && i < this.imageReIds.length; i++) {
					this.imageViews.get(i).setImageResource(this.imageReIds[i]);
				}
			} else {
				for (int i = 0; i < num && i < this.imageUrls.length; i++) {
					this.loadImage(this.imageViews.get(i), this.imageUrls[i]);
				}
			}
		}
	}

	private void loadImage(ImageView imageView, String url) {
		Picasso.with(context).load(Constants.IMAGE_URL + url).placeholder(R.drawable.home02).error(R.drawable.home02).into(imageView);
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
			if (position < imageViews.size()) {
				container.removeView(imageViews.get(position));
			}
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
			if(SCROLL_ORIENTATION.RIGHT == this.orientation) {
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

	class TimerCircle extends CountDownTimer {
		private CircleView circleView;
		public TimerCircle(long millisInFuture, long countDownInterval, CircleView circleView) {
			super(millisInFuture, countDownInterval);
			this.circleView = circleView;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			circleView.circle();
		}

		@Override
		public void onFinish() {

		}
	}

}
