package jc.house.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutCompat;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import jc.house.global.Constants;
import jc.house.utils.LogUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

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
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initViewBefore(context);
    }

    private void initView() {
        this.viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        this.viewPager.setOffscreenPageLimit(1);
        this.viewPager.setCurrentItem(0, true);
        this.indicatorView = (IndicatorView) this
                .findViewById(R.id.indicatorView);
    }

    private void initViewForCommon() {
        LayoutInflater.from(context).inflate(R.layout.div_circleview, this);
        initView();
    }

    private void initViewForPhoto() {
        LayoutInflater.from(context).inflate(R.layout.div_circleview_big, this);
        initView();
    }

    private void initViewBefore(Context context) {
        this.context = context;
        this.first = true;
        this.autoPlay = false;
        this.timeInterval = 3.0f;
        this.num = 0;
        this.imageViews = new ArrayList<>();
        this.animation = new AlphaAnimation(0.8f, 1.0f);
        this.animation.setDuration(600);
        this.orientation = SCROLL_ORIENTATION.RIGHT;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        if (null == timer) {
            timer = new TimerCircle(System.currentTimeMillis() / 1000 + (long) timeInterval * 1000, (long) timeInterval * 1000, this);
            timer.start();
        }
    }

    public void setTimeInterval(float timeInterval) {
        if (timeInterval > 0) {
            this.timeInterval = timeInterval;
        } else {
            throw new IllegalStateException("timeInterval should be > 0");
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
                    initViewForCommon();
                    this.imageReIds = imageReIds;
                    this.num = imageReIds.length;
                } else if (null == this.imageReIds) {
                    this.imageReIds = imageReIds;
                }
                addImageViews(true);
            }
        }
    }

    public void setImageUrls(String[] imageUrls) {
        synchronized (this) {
            if (null != imageUrls && imageUrls.length > 0) {
                if (first) {
                    initViewForCommon();
                    this.imageUrls = imageUrls;
                    this.num = imageUrls.length;
                } else if (null == this.imageUrls) {
                    this.imageUrls = imageUrls;
                }
                this.addImageViews(false);
            }
        }
    }

    private void addImageViews(boolean isLocalRes) {
        if (null != timer) {
            timer.cancel();
        }
        if (this.first) {
            this.imageViews.clear();
            for (int i = 0; i < num; i++) {
                ImageView imageView = new ImageView(this.context);
                imageView.setScaleType(ScaleType.FIT_XY);
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
            if (num > 1) {
                this.indicatorView.setNum(num);
            }
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
            int size = 0;
            if (null != imageUrls) {
                size = imageUrls.length;
            } else if (null != imageReIds) {
                size = imageReIds.length;
            }
            if (isLocalRes) {
                for (int i = 0; i < num && i < size; i++) {
                    this.imageViews.get(i).setImageResource(this.imageReIds[i]);
                }
            } else {
                for (int i = 0; i < num && i < size; i++) {
                    this.loadImage(this.imageViews.get(i), this.imageUrls[i]);
                }
            }
            timer.start();
        }
    }

    public void setPhotoViews(String[] urls, boolean isPIcasso) {
        if (null != urls && urls.length <= 0) {
            return;
        }
        initViewForPhoto();
        this.imageViews.clear();
        this.imageUrls = urls;
        this.num = urls.length;
        for (int i = 0; i < urls.length; i++) {
            boolean canZoom = false;
            PhotoView photoView = new PhotoView(context);
            photoView.setScaleType(ScaleType.FIT_CENTER);
            photoView.setZoomable(canZoom);
            photoView.setEnabled(canZoom);
//            photoView.setAdjustViewBounds(true);
            if (isPIcasso) {
                loadImageLocal(photoView, urls[i]);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(urls[i]);
                photoView.setImageBitmap(bitmap);
            }
            PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
            attacher.setZoomable(canZoom);
            if (!canZoom) {
                attacher.setOnDoubleTapListener(null);
                attacher.setOnScaleChangeListener(null);
            }
            attacher.setAllowParentInterceptOnEdge(true);
//            photoView.setAdjustViewBounds(true);
            if (canZoom) {
                attacher.setMinimumScale(1.0f);
                attacher.setScaleLevels(1.0f, 1.5f, 2.5f);
            }
            attacher.update();
            this.imageViews.add(photoView);
        }
        if(num > 1) {
            this.indicatorView.setNum(num);
            this.viewPager.addOnPageChangeListener(new CircleOnPageChangeListener());
        }
        this.viewPager.setAdapter(new CirclePagerAdapter());
    }

    private void loadImage(ImageView imageView, String url) {
        Picasso.with(context).load(url).placeholder(R.drawable.failure_image_red).error(R.drawable.failure_image_red).into(imageView);
    }

    private void loadImageLocal(ImageView imageView, String url) {
        Picasso.with(context).load(url).placeholder(R.drawable.failure_image_red).skipMemoryCache().config(Bitmap.Config.ALPHA_8).error(R.drawable.failure_image_red).into(imageView);
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
            currentIndex = index;
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

    public void cancelTimer() {
        if (null != timer) {
            this.setAutoPlay(false);
            timer.cancel();
        }
    }

    public void setShowIndex(int index) {
        setCurrentIndex(index);
    }

    private void circleAnimate() {
        this.startAnimation(animation);
    }

    private void circle() {
        if (autoPlay && num > 1) {
            if (SCROLL_ORIENTATION.RIGHT == this.orientation) {
                currentIndex = (currentIndex + 1) % num;
            } else {
                currentIndex -= 1;
                if (currentIndex < 0) {
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
