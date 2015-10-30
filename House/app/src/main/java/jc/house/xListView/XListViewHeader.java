package jc.house.xListView;

import jc.house.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class XListViewHeader extends XListViewBaser {
	private RelativeLayout mContainer;
	private ImageView mArrowImageView;

	private final int ROTATE_ANIM_DURATION = 180;

	private Animation mRotateAnimUp;
	private Animation mRotateAnimDown;

	public XListViewHeader(Context context) {
		super(context);
	}

	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XListViewHeader(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.xlistview_header, this);
		this.mContainer = (RelativeLayout) this
				.findViewById(R.id.xlistview_header_container);
		this.mArrowImageView = (ImageView) this
				.findViewById(R.id.xlistview_header_arrow);
		this.mProgressBar = (ProgressBar) this
				.findViewById(R.id.xlistview_header_progressbar);
		this.mRotateAnimUp = new RotateAnimation(0.f, -180.f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.mRotateAnimUp.setDuration(ROTATE_ANIM_DURATION);
		this.mRotateAnimUp.setFillAfter(true);
		this.mRotateAnimDown = new RotateAnimation(-180.f, 0.f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.mRotateAnimDown.setDuration(ROTATE_ANIM_DURATION);
		this.mRotateAnimDown.setFillAfter(true);
		this.state = STATE_NORMAL;
	}

	public int getNormalHeight() {
		return 80;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		if (state < STATE_NORMAL || state > STATE_REFRESHING) {
			return;
		}
		switch (state) {
		case STATE_NORMAL:
			this.normalState();
			break;
		case STATE_READY:
			this.readyState();
			break;
		case STATE_REFRESHING:
			this.refreshingState();
			break;
		}
		this.state = state;
	}

	@Override
	protected void normalState() {
		if (this.state == STATE_READY) {
			this.mArrowImageView.startAnimation(mRotateAnimDown);
			this.initStateViews();
		} else if (this.state == STATE_REFRESHING) {
			this.mArrowImageView.clearAnimation();
		}else {
			this.initStateViews();
		}
	}

	@Override
	protected void readyState() {
		this.initStateViews();
		if (this.state == STATE_NORMAL) {
			this.mArrowImageView.startAnimation(mRotateAnimUp);
		} else if (this.state == STATE_REFRESHING) {
			this.mArrowImageView.clearAnimation();
		}
	}

	@Override
	protected void refreshingState() {
		this.mArrowImageView.setVisibility(View.GONE);
		this.mArrowImageView.clearAnimation();
		this.mProgressBar.setVisibility(View.VISIBLE);
	}

	private void initStateViews() {
		this.mArrowImageView.setVisibility(View.VISIBLE);
		this.mProgressBar.setVisibility(View.GONE);
	}

	public void setVisibleHeight(int height) {
		if (height < 0) {
			height = 0;
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		params.height = height;
		mContainer.setLayoutParams(params);
	}

	public int getVisibleHeight() {
		return mContainer.getLayoutParams().height;
	}

}
