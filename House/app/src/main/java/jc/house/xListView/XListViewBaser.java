package jc.house.xListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public abstract class XListViewBaser extends LinearLayout {
	public static final int STATE_NORMAL = 0;
	public static final int STATE_READY = 1;
	public static final int STATE_REFRESHING = 2;
	protected ProgressBar mProgressBar;
	protected int state;

	public XListViewBaser(Context context) {
		super(context);
		this.initView(context);
	}

	public XListViewBaser(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
	}

	public XListViewBaser(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initView(context);
	}

	protected abstract void initView(Context context);
	
	protected abstract void normalState();
	
	protected abstract void readyState();
	
	protected abstract void refreshingState();
	
}
