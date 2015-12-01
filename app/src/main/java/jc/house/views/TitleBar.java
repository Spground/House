package jc.house.views;

import jc.house.R;
import jc.house.utils.StringUtils;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleBar extends LinearLayout {
	private Context mContext;
	private ImageView backImage;
	private TextView titleView;
	private LinearLayout rightView;

	public TitleBar(Context context) {
		super(context);
		this.initView(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
	}

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initView(context);
	}
	
	private void initView(Context context) {
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.div_titlebar, this);
		this.backImage = (ImageView)this.findViewById(R.id.back_image);
		this.backImage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((Activity)mContext).finish();
			}
			
		});
		this.titleView = (TextView)this.findViewById(R.id.title);
		this.rightView = (LinearLayout)this.findViewById(R.id.right_view);
	}
	
	public void setTitle(String title) {
		if(!StringUtils.strEmpty(title)) {
			this.titleView.setText(title);
		}
	}
	
	public void setRightChildView(View child) {
		if(null != child) {
			this.rightView.removeAllViews();
			this.rightView.addView(child);
		}
	}

}
