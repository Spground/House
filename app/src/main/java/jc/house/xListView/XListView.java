package jc.house.xListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Scroller;

public class XListView extends ListView implements OnScrollListener {
	
	public interface XListViewListener {
		public void refreshing();
		public void loadMore();
	}
	
	private boolean pullRefresh;
	private boolean pullLoadMore;
	private XListViewHeader mHeader;
	private XListViewFooter mFooter;
	private boolean isRefreshing;
	private boolean isReadyRefresh;
	private boolean isLoadingMore;
	private boolean isReadyLoadingMore;
	private float lastY = -1;
	private int totalItemCount = 0;
	private static final int SCROLL_DURATION = 400;
	private static final int LOAD_MORE_POINT = 50;
	private static final float SCROLL_RATE = 1.6f;
	private static final int SCROLLBACK_HEADER = 0;
	private static final int SCROLLBACK_FOOTER = 1;
	private int scrollFlag;
	private int mScrollBack;
	private XListViewListener xListener;
	private OnScrollListener mScrollListener; // user's scroll listener
	private Scroller scroller;

	public XListView(Context context) {
		super(context);
		this.initView(context);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initView(context);
	}

	private void initView(Context context) {
		this.pullRefresh = true;
		this.pullLoadMore = true;
		this.mHeader = new XListViewHeader(context);
		this.mHeader.setVisibleHeight(0);
		this.addHeaderView(mHeader);
		this.mFooter = new XListViewFooter(context);
		this.addFooterView(mFooter);
		this.isRefreshing = false;
		this.isLoadingMore = false;
		this.setOnScrollListener(this);
		this.scroller = new Scroller(context);
	}
	
	public OnScrollListener getmScrollListener() {
		return mScrollListener;
	}

	public void setmScrollListener(OnScrollListener mScrollListener) {
		this.mScrollListener = mScrollListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(null != this.mScrollListener) {
			this.mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.totalItemCount = totalItemCount;
		if(null != this.mScrollListener) {
			this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public boolean scrollOrientation() {
		return pullRefresh;
	}

	public void setPullRefresh(boolean pullRefresh) {
		this.pullRefresh = pullRefresh;
		if (this.pullRefresh) {
			this.mHeader.setVisibility(View.GONE);
		} else {
			this.mHeader.setVisibility(View.VISIBLE);
		}
	}

	public boolean isPullLoadMore() {
		return pullLoadMore;
	}

	public void setPullLoadMore(boolean pullLoadMore) {
		this.pullLoadMore = pullLoadMore;
		if (this.pullLoadMore) {
			this.mFooter.show();
			this.mFooter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startLoadMore();
				}

			});
		} else {
			this.mFooter.hide();
			this.mFooter.setOnClickListener(null);
		}
	}

	private void updateHeaderHeight(int height) {
		int mHeight = mHeader.getVisibleHeight() + height;
		mHeader.setVisibleHeight(mHeight);
		if (pullRefresh && !isRefreshing) {
			if (mHeader.getVisibleHeight() >= mHeader.getNormalHeight()) {
				isReadyRefresh = true;
				mHeader.setState(XListViewHeader.STATE_READY);
			} else {
				isReadyRefresh = false;
				mHeader.setState(XListViewHeader.STATE_NORMAL);
			}
		}
	}
	
	public XListViewListener getxListener() {
		return xListener;
	}

	public void setxListener(XListViewListener xListener) {
		this.xListener = xListener;
	}

	private void resetHeaderHeight() {
		int height = this.mHeader.getVisibleHeight();
		if (height == 0) {
			return;
		}
		if(isRefreshing && height <= mHeader.getNormalHeight()) {
			return;
		}
		int h = 0;
		if(height > mHeader.getNormalHeight()) {
			h = mHeader.getNormalHeight();
		}
		this.scroller.startScroll(0, height, 0, h - height, SCROLL_DURATION - 200);
		invalidate();
	}

	private void updateFooterHeight(int height) {
		int mHeight = this.mFooter.getBottomMargin() + height;
		if (pullLoadMore && !isLoadingMore) {
			if (mHeight >= LOAD_MORE_POINT) {
				this.isReadyLoadingMore = true;
				this.mFooter.setState(XListViewFooter.STATE_READY);
			} else {
				this.isReadyLoadingMore = false;
				this.mFooter.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		this.mFooter.setBottomMargin(mHeight);
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooter.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			scroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeader.setVisibleHeight(scroller.getCurrY());
			} else {
				mFooter.setBottomMargin(scroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (this.lastY == -1) {
			this.lastY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float span = ev.getRawY() - lastY;
			lastY = ev.getRawY();
			int height = (int) (span / SCROLL_RATE);
			if (pullRefresh
					&& this.getFirstVisiblePosition() == 0 && (this.mHeader
							.getVisibleHeight() > 0 || span > 0) && scrollFlag != 2) {
				this.updateHeaderHeight(height);
				scrollFlag = 1;
				mScrollBack = SCROLLBACK_HEADER;
			} else if (pullLoadMore
					&& (this.getLastVisiblePosition() == this.totalItemCount - 1) && (this.mFooter.getBottomMargin() > 0 || span < 0) && scrollFlag != 1){
				updateFooterHeight(-height);
				scrollFlag = 2;
				mScrollBack = SCROLLBACK_FOOTER;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullRefresh
					&& this.getFirstVisiblePosition() == 0 && scrollFlag != 2) {
				if (isReadyRefresh) {
					startRefresh();
				}
				resetHeaderHeight();
			}
			if (pullLoadMore
					&& this.getLastVisiblePosition() == this.totalItemCount - 1 && scrollFlag != 1) {
				if (isReadyLoadingMore) {
					startLoadMore();
				}
				this.resetFooterHeight();
			}
			scrollFlag = 0;
			lastY = -1;
			break;
		default:
			scrollFlag = 0;
			lastY = -1;
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	public void startRefresh() {
		if(!this.isRefreshing) {
			this.isRefreshing = true;
			this.isReadyRefresh = false;
			this.mHeader.setState(XListViewHeader.STATE_REFRESHING);
			if (null != this.xListener) {
				this.xListener.refreshing();
			}
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (isRefreshing == true) {
			isRefreshing = false;
			isReadyRefresh = false;
			resetHeaderHeight();
			this.mHeader.setState(XListViewHeader.STATE_NORMAL);
		}
	}
	
	public void startLoadMore() {
		if(!this.isLoadingMore) {
			this.isLoadingMore = true;
			this.isReadyLoadingMore = false;
			this.mFooter.show();
			this.mFooter.setState(XListViewFooter.STATE_REFRESHING);
			if (null != this.xListener) {
				this.xListener.loadMore();
			}
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (isLoadingMore == true) {
			isLoadingMore = false;
			isReadyLoadingMore = false;
			resetFooterHeight();
			mFooter.setState(XListViewFooter.STATE_NORMAL);
		}
	}

}
