package jc.house.xListView;

import jc.house.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class XListViewFooter extends XListViewBaser {
	private RelativeLayout mContainer;
	private TextView mHintView;
	private static final String NORMAL_HINT = "加载更多";
	private static final String READY_HINT = "松开加载数据";

	public XListViewFooter(Context context) {
		super(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XListViewFooter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.xlistview_footer, this);
		this.mContainer = (RelativeLayout) this
				.findViewById(R.id.xlistview_footer_container);
		this.mHintView = (TextView) this
				.findViewById(R.id.xlistview_footer_hintview);
		this.mProgressBar = (ProgressBar) this
				.findViewById(R.id.xlistview_footer_progressbar);
	}

	public void setState(int state) {
		if (state < STATE_NORMAL || state > STATE_REFRESHING) {
			return;
		}
		switch (state) {
		case STATE_NORMAL:
			normalState();
			break;
		case STATE_READY:
			readyState();
			break;
		case STATE_REFRESHING:
			refreshingState();
			break;
		}
	}

	@Override
	protected void normalState() {
		this.mProgressBar.setVisibility(View.GONE);
		this.mHintView.setText(NORMAL_HINT);
		this.mHintView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void readyState() {
		this.mProgressBar.setVisibility(View.GONE);
		this.mHintView.setText(READY_HINT);
		this.mHintView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void refreshingState() {
		this.mHintView.setVisibility(View.GONE);
		this.mProgressBar.setVisibility(View.VISIBLE);
	}

	public void hide() {
		LayoutParams params = (LinearLayout.LayoutParams) this.mContainer
				.getLayoutParams();
		params.height = 0;
		this.setLayoutParams(params);
	}

	public void show() {
		LayoutParams params = (LinearLayout.LayoutParams) this.mContainer
				.getLayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;
		this.mContainer.setLayoutParams(params);
	}

	public void setBottomMargin(int height) {
		if (height < 0) {
			return;
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mContainer
				.getLayoutParams();
		params.bottomMargin = height;
		this.mContainer.setLayoutParams(params);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mContainer
				.getLayoutParams();
		return params.bottomMargin;
	}

}
