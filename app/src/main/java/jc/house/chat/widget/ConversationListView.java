package jc.house.chat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import jc.house.R;

/**
 * Created by WuJie on 2015/11/11.
 */
public class ConversationListView extends ListView {
    Context context;

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;

    public ConversationListView(Context context) {
        super(context);
    }

    public ConversationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ConversationListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        TypedArray ta = this.context.obtainStyledAttributes(attrs, R.styleable.EaseConversationList);
        primaryColor = ta.getColor(R.styleable.EaseConversationList_cvsListPrimaryTextColor, getResources().getColor(R.color.list_itease_primary_color));
        secondaryColor = ta.getColor(R.styleable.EaseConversationList_cvsListSecondaryTextColor, getResources().getColor(R.color.list_itease_secondary_color));
        timeColor = ta.getColor(R.styleable.EaseConversationList_cvsListTimeTextColor, getResources().getColor(R.color.list_itease_secondary_color));
        primarySize = ta.getDimensionPixelSize(R.styleable.EaseConversationList_cvsListPrimaryTextSize, 0);
        secondarySize = ta.getDimensionPixelSize(R.styleable.EaseConversationList_cvsListSecondaryTextSize, 0);
        timeSize = ta.getDimension(R.styleable.EaseConversationList_cvsListTimeTextSize, 0);
        ta.recycle();
    }



}
