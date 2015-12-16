package jc.house.chat.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;

import java.util.List;

import jc.house.chat.util.CommonUtils;
import jc.house.chat.widget.chatrow.EaseChatRowHouse;
import jc.house.chat.widget.chatrow.EaseChatRowImage;
import jc.house.global.Constants;
import jc.house.utils.LogUtils;
import jc.house.chat.widget.ChatMessageList;
import jc.house.chat.widget.chatrow.EaseChatRow;
import jc.house.chat.widget.chatrow.EaseChatRowText;

/**
 * ChatMessageAdapter，used only for ChatActivity
 */
public class ChatMessageAdapter extends BaseAdapter {
    public static final String TAG = "ChatMessageAdapter";

    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private Context context;
    private ListView chatMsgListView;

    private String chatUserName;
    private List<EMMessage> msgDataSrc;//数据源
    private EMConversation conversation;
    /**
     * ChatMessageList中的聊天项的点击监听
     */
    private ChatMessageList.MessageListItemClickListener itemClickListener;

    public Handler handler = new Handler(){
        private void refreshMsgDataSource() {
            // UI线程不能直接使用conversation.getAllMessages()
            // 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
            msgDataSrc = conversation.getAllMessages();
            if(msgDataSrc == null)
                return;
            for (int i = 0; i < msgDataSrc.size(); i++) {
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
                    refreshMsgDataSource();
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (msgDataSrc.size() > 0) {
                        chatMsgListView.setSelection(msgDataSrc.size() - 1);
                    }
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = msg.arg1;
                    chatMsgListView.setSelection(position);
                    break;
                default:
                    break;
            }
        }
    };

    public ChatMessageAdapter(Context context, String chatUserName,ListView chatMsgListView){
        LogUtils.debug(TAG, "ChatMessageAdapter构造函数被调用");
        this.context = context;
        this.chatUserName = chatUserName;
        this.chatMsgListView = chatMsgListView;
        this.conversation =  EMChatManager.getInstance().getConversation(chatUserName);
    }

    @Override
    public int getCount() {
        return msgDataSrc == null ? 0 : msgDataSrc.size();
    }

    @Override
    public EMMessage getItem(int position) {
        if(msgDataSrc != null && msgDataSrc.size() >= position)
            return msgDataSrc.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        /**get message from data-source**/
        EMMessage message = getItem(position);
        EaseChatRow convertChatRow = (EaseChatRow)convertView;

        //只有满足 convertChatRow不为空 且消息类型 和 视图的类型一致 且 消息的方向和视图的方向一致
        //才复用convertChatRow，否则重创建
//        if(!(convertChatRow != null
//                && convertChatRow.getMessageDirect() == message.direct
//                && convertChatRow.getMessageType() == message.getType())) {
//            convertView = createChatRow(context, message, position);
//        }
        convertView = createChatRow(context, message, position);
      //缓存的view的message很可能不是当前item的，传入当前message和position更新ui
        ((EaseChatRow)convertView).setUpView(message, position, itemClickListener);
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
        Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
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
        Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }

    /**
     * create a ChatRow for every message
     */
    private EaseChatRow createChatRow(Context context, EMMessage message, int position){
        //TXT消息
        if(message.getType().equals(EMMessage.Type.TXT )) {
            boolean isHouse = message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_HOUSE, false);
            if(isHouse)
                return new EaseChatRowHouse(context, message, position, this);
            else
                return new EaseChatRowText(context, message, position, this);
        }

        if(message.getType().equals(EMMessage.Type.IMAGE))
            return new EaseChatRowImage(context, message, position, this);
        return null;
    }

    public void setItemClickListener(ChatMessageList.MessageListItemClickListener listener){
        itemClickListener = listener;
    }
}