package jc.house.views;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class IndicatorView extends LinearLayout {
	private int normalResId;
	private int selectedResId;
	private int spacing;
	private int num;
	private Context context;
	private int selectedIndex;
	private List<ImageView> imageViews;

	public IndicatorView(Context context) {
		super(context);
		this.initView(context);
	}

	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
	}

	public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initView(context);
	}
	
	private void initView(Context context) {
		this.normalResId = R.drawable.ic_launcher;
		this.selectedResId = R.drawable.ic_launcher;
		this.spacing = 5;
		this.selectedIndex = 0;
		this.context = context;
		this.imageViews = new ArrayList<ImageView>();
		this.setGravity(Gravity.CENTER);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		if (num > 0) {
			this.imageViews.clear();
			this.num = num;
			for (int i = 0; i < num; i++) {
				ImageView imageView = new ImageView(this.context);
				imageView.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				if (this.selectedIndex == i) {
					imageView.setImageResource(this.selectedResId);
				} else {
					imageView.setImageResource(this.normalResId);
				}
				imageView.setPadding(this.spacing, 0, 0, 0);
				imageViews.add(imageView);
				this.addView(imageView);
			}
		}
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		if (this.selectedIndex != selectedIndex && selectedIndex < this.num
				&& selectedIndex >= 0) {
			((ImageView) (imageViews.get(this.selectedIndex)))
					.setImageResource(this.normalResId);
			((ImageView) (imageViews.get(selectedIndex)))
					.setImageResource(this.selectedResId);
			this.selectedIndex = selectedIndex;
		}
	}

	public int getNormalResId() {
		return normalResId;
	}

	public void setNormalResId(int normalResId) {
		this.normalResId = normalResId;
	}

	public int getSelectedResId() {
		return selectedResId;
	}

	public void setSelectedResId(int selectedResId) {
		this.selectedResId = selectedResId;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		if (spacing > 0) {
			this.spacing = spacing;
		}
	}

}