package jc.house.chat.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import jc.house.R;
import jc.house.chat.adapter.EmojiExpressionPagerAdapter;
import jc.house.chat.util.EmojiUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 表情图片控件
 */
public class EmojiconMenu extends EmojiconMenuBase {
	
	private float emojiconSize;
	private List<String> reslist;
	private Context context;
	private ViewPager expressionViewpager;
	
	private int emojiconRows;
	private int emojiconColumns;
	private final int defaultRows = 3;
	private final int defaultColumns = 7;
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public EmojiconMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public EmojiconMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public EmojiconMenu(Context context) {
		super(context);
		init(context, null);
	}
	
	private void init(Context context, AttributeSet attrs){
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.jc_widget_emojicon, this);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseEmojiconMenu);
		emojiconColumns = ta.getInt(R.styleable.EaseEmojiconMenu_emojiconColumns, defaultColumns);
		emojiconRows = ta.getInt(R.styleable.EaseEmojiconMenu_emojiconRows, defaultRows);
		ta.recycle();
		// 表情list
		reslist = getExpressionRes(EmojiUtils.getSmilesSize());
		// 初始化表情viewpager
		List<View> views = getGridChildViews();
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		expressionViewpager.setAdapter(new EmojiExpressionPagerAdapter(views));
	}
	
	/**
	 * 获取表情的gridview的子views
	 * @return
	 */
	private List<View> getGridChildViews(){
	    int itemSize = emojiconColumns * emojiconRows -1;
	    int totalSize = EmojiUtils.getSmilesSize();
	    int pageSize = totalSize % itemSize == 0 ? totalSize/itemSize : totalSize/itemSize + 1;
	    List<View> views = new ArrayList<View>();
	    for(int i = 0; i < pageSize; i++){
	        View view = View.inflate(context, R.layout.jc_expression_gridview, null);
	        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
	        gv.setNumColumns(emojiconColumns);
	        List<String> list = new ArrayList<>();
	        if(i != pageSize -1){
	            list.addAll(reslist.subList(i * itemSize, (i+1) * itemSize));
	        }else{
	            list.addAll(reslist.subList(i * itemSize, totalSize));
	        }
	        list.add("delete_expression");
	        final EmojiIconGridAdapter gridAdapter = new EmojiIconGridAdapter(context, 1, list);
	        gv.setAdapter(gridAdapter);
	        gv.setOnItemClickListener(new OnItemClickListener() {

	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                String filename = gridAdapter.getItem(position);
	                if(listener != null){
	                    if (filename != "delete_expression"){
	                        try {
	                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
	                            Class clz = Class.forName("jc.house.chat.util.EmojiUtils");
	                            Field field = clz.getField(filename);
	                            CharSequence cs = EmojiUtils.getSmiledText(context,(String) field.get(null));
	                            listener.onExpressionClicked(cs);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }else{
	                        listener.onDeleteImageClicked();
	                    }
	                }
	            }
	        });
	        
	        views.add(view);
	    }
	    return views;
	}
	
	private List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;
			reslist.add(filename);
		}
		return reslist;
	}

	/**每一页ViewPager的表情的GridView的适配器**/
	private class EmojiIconGridAdapter extends ArrayAdapter<String> {

	    public EmojiIconGridAdapter(Context context, int textViewResourceId, List<String> objects) {
	        super(context, textViewResourceId, objects);
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        if(convertView == null){
	            convertView = View.inflate(getContext(), R.layout.jc_row_expression, null);
	        }
	        
	        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);
	        
	        String filename = getItem(position);
	        int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
	        imageView.setImageResource(resId);
	        
	        return convertView;
	    }
	}
}
