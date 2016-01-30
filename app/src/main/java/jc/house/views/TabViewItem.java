package jc.house.views;

import jc.house.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabViewItem extends LinearLayout {
	private ImageView imageView;
	private TextView textView;
	private View littleRedDotView;//小红点

	private boolean selected;
	private int normalResId, selectedResId;
	private int index;
	private final int normalTextColor = Color.rgb(99, 99, 99);
	private final int selectedTextColor = Color.rgb(255, 0 , 0);

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
		LayoutInflater.from(context).inflate(R.layout.div_tabview_item, this);
		this.imageView = (ImageView) this.findViewById(R.id.tab_image);
		this.textView = (TextView) this.findViewById(R.id.tab_name);
		this.littleRedDotView = this.findViewById(R.id.unread_msg_little_red_dot);
		this.setBackgroundColor(Color.rgb(231, 231, 231));
		this.textView.setTextColor(normalTextColor);
		this.selected = false;
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

	public void setSelectedResId(int selectedResId) {
		this.selectedResId = selectedResId;
	}

	public void setNormalResId(int normalResId) {
		this.normalResId = normalResId;
	}

	/**
	 * set little red dot visible
	 */
	public void lightLittleRedDot(){
		this.littleRedDotView.setVisibility(View.VISIBLE);
	}

	public void unlightLittleRedDot(){
		this.littleRedDotView.setVisibility(View.INVISIBLE);
	}
}
