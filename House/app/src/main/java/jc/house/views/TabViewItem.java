package jc.house.views;

import jc.house.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabViewItem extends LinearLayout {
	private ImageView imageView;
	private TextView textView;
	private boolean selected;
	private int normalResId, selectedResId;
	private int index;
	private final int normalTextColor = Color.rgb(99, 99, 99);
	private final int selectedTextColor = Color.rgb(66, 204, 99);

	public TabViewItem(Context context) {
		super(context);
		this.initView(context);
	}

	public TabViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
	}
	
	public TabViewItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initView(context);
	}
	
	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.tab_item, this);
		this.imageView = (ImageView) this.findViewById(R.id.tab_image);
		this.textView = (TextView) this.findViewById(R.id.tab_name);
		this.setBackgroundColor(Color.rgb(231, 231, 231));
		this.textView.setTextColor(normalTextColor);
		this.selectedResId = R.drawable.ic_launcher;
		this.normalResId = R.drawable.ic_launcher;
		this.selected = false;
		this.imageView.setImageResource(this.normalResId);
		this.index = 0;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setTabName(String name) {
		this.textView.setText(name);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if (selected) {
			this.textView.setTextColor(selectedTextColor);
			this.imageView.setImageResource(selectedResId);
		} else {
			this.textView.setTextColor(normalTextColor);
			this.imageView.setImageResource(this.normalResId);
		}
		this.selected = selected;
	}

	public int getSelectedResId() {
		return selectedResId;
	}

	public void setSelectedResId(int selectedResId) {
		this.selectedResId = selectedResId;
	}

	public int getNormalResId() {
		return normalResId;
	}

	public void setNormalResId(int normalResId) {
		this.normalResId = normalResId;
	}

	public void setColorSelectedRate(float rate) {
		if (rate > 0 && rate < 1) {
			this.imageView.setAlpha(1 - rate);
			this.textView
					.setTextColor(Color.rgb(
							Color.red(selectedTextColor)
									- (int) (rate * (Color
											.red(selectedTextColor) - Color
											.red(normalTextColor))),
							Color.green(selectedTextColor)
									- (int) (rate * (Color
											.green(selectedTextColor) - Color
											.green(normalTextColor))),
							Color.blue(selectedTextColor)
									- (int) (rate * (Color
											.blue(selectedTextColor) - Color
											.blue(normalTextColor)))));
		}
	}

	public void setColorNormalRate(float rate) {
		if (rate > 0 && rate < 1) {
			this.imageView.setAlpha(rate);
			this.textView
					.setTextColor(Color.rgb(
							Color.red(normalTextColor)
									+ (int) (rate * (Color
											.red(selectedTextColor) - Color
											.red(normalTextColor))),
							Color.green(normalTextColor)
									+ (int) (rate * (Color
											.green(selectedTextColor) - Color
											.green(normalTextColor))),
							Color.blue(normalTextColor)
									+ (int) (rate * (Color
											.blue(selectedTextColor) - Color
											.blue(normalTextColor)))));
		}
	}
}
