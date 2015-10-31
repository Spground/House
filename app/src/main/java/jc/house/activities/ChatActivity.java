package jc.house.activities;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;

import java.util.List;

import jc.house.R;
import jc.house.views.TitleBar;

/**
 * 2015-10-31
 */
public class ChatActivity extends Activity {

    private TitleBar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**this must be called before setContentView() method**/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        this.titleBar = (TitleBar)findViewById(R.id.titlebar);
        this.titleBar.setTitle("会话");
    }

}

/**
 * ChatMessageAdapter，used only for ChatActivity
 */
class ChatMessageAdapter extends BaseAdapter{

    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private Context context;
    private ListView chatMsgsListView;

    private String chatUserName;
    private List<EMMessage> msgsDataSrc;
    private EMConversation conversation;

    public Handler handler = new Handler(){
        private void refreshMsgsDataSource() {
            // UI线程不能直接使用conversation.getAllMessages()
            // 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
            msgsDataSrc = conversation.getAllMessages();
            if(msgsDataSrc == null)
                return;
            for (int i = 0; i < msgsDataSrc.size(); i++) {
                // getMessage will set message as read status
                conversation.getMessage(i);
            }
            notifyDataSetChanged();
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshMsgsDataSource();
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (msgsDataSrc.size() > 0) {
                        chatMsgsListView.setSelection(msgsDataSrc.size() - 1);
                    }
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = msg.arg1;
                    chatMsgsListView.setSelection(position);
                    break;
                default:
                    break;
            }
        }
    };

    public ChatMessageAdapter(Context context, String chatUserName,ListView chatMsgsListView){
        this.context =context;
        this.chatUserName = chatUserName;
        this.chatMsgsListView = chatMsgsListView;
        this.conversation =  EMChatManager.getInstance().getConversation(chatUserName);
    }

    @Override
    public int getCount() {
        return msgsDataSrc == null ? 0 : msgsDataSrc.size();
    }

    @Override
    public Object getItem(int position) {
        if(msgsDataSrc != null && msgsDataSrc.size() >= position)
            return msgsDataSrc.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        EMMessage message = (EMMessage)getItem(position);
//        if(convertView == null){
//            convertView = createChatRow(context, message, position);
//        }
        //缓存的view的message很可能不是当前item的，传入当前message和position更新ui
//        ((EaseChatRow)convertView).setUpView(message, position, itemClickListener);
        return convertView;
    }

    //////////////////////////////custom methods//////////////////////////////////
    /**
     * 刷新页面
     */
    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * 刷新页面, 选择最后一个
     */
    public void refreshSelectLast() {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
    }

    /**
     * 刷新页面, 选择Position
     */
    public void refreshSeekTo(int position) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }

    /**
     * create a ChatRow for every message
     */
    private void createChatRow(){

    }
}