package jc.house.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.easemob.chat.EMMessage;

import jc.house.R;
import jc.house.adapters.ChatMessageAdapter;

public class ChatMessageList extends RelativeLayout{
    
    protected ListView listView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected Context context;
    protected int chatType;
    protected String toChatUsername;
    protected ChatMessageAdapter messageAdapter;
    protected boolean showUserNick;

	public ChatMessageList(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public ChatMessageList(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	parseStyle(context, attrs);
    	init(context);
    }

    public ChatMessageList(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.jc_chat_message_list, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
        listView = (ListView) findViewById(R.id.list);
    }
    
    /**
     * init widget
     * @param toChatUsername
     * @param chatType
     */
    public void init(String toChatUsername, int chatType) {
        this.chatType = chatType;
        this.toChatUsername = toChatUsername;
        
        messageAdapter = new ChatMessageAdapter(context, toChatUsername, listView);
        // 设置adapter显示消息
        listView.setAdapter(messageAdapter);
        refreshSelectLast();
    }
    
    protected void parseStyle(Context context, AttributeSet attrs) {
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseChatMessageList);
//        showAvatar = ta.getBoolean(R.styleable.EaseChatMessageList_msgListShowUserAvatar, true);
//        myBubbleBg = ta.getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground);
//        otherBuddleBg = ta.getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground);
//        showUserNick = ta.getBoolean(R.styleable.EaseChatMessageList_msgListShowUserNick, false);
//        ta.recycle();
    }
    
    
    /**
     * 刷新列表
     */
    public void refresh(){
        messageAdapter.refresh();
    }
    
    /**
     * 刷新列表，并且跳至最后一个item
     */
    public void refreshSelectLast(){
        messageAdapter.refreshSelectLast();
    }
    
    /**
     * 刷新页面,并跳至给定position
     * @param position
     */
    public void refreshSeekTo(int position){
        messageAdapter.refreshSeekTo(position);;
    }
    
    /**
     * 获取listview
     * @return
     */
	public ListView getListView() {
		return listView;
	} 
	
	/**
	 * 获取SwipeRefreshLayout
	 * @return
	 */
	public SwipeRefreshLayout getSwipeRefreshLayout(){
	    return swipeRefreshLayout;
	}
	
	public EMMessage getItem(int position){
	    return messageAdapter.getItem(position);
	}
	
	/**
	 * 设置是否显示用户昵称
	 * @param showUserNick
	 */
	public void setShowUserNick(boolean showUserNick){
	    this.showUserNick = showUserNick;
	}
	
	public boolean isShowUserNick(){
	    return showUserNick;
	}
	
	public interface MessageListItemClickListener{
	    void onResendClick(EMMessage message);
	    /**
	     * 控件有对气泡做点击事件默认实现，如果需要自己实现，return true。
	     * 当然也可以在相应的chatrow的onBubbleClick()方法里实现点击事件
	     * @param message
	     * @return
	     */
	    boolean onBubbleClick(EMMessage message);
	    void onBubbleLongClick(EMMessage message);
	    void onUserAvatarClick(String username);
	}
	
	/**
	 * 设置list item里控件的点击事件
	 * @param listener
	 */
	public void setItemClickListener(MessageListItemClickListener listener){
	    messageAdapter.setItemClickListener(listener);
	}
}
