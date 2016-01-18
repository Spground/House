package jc.house.chat.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;

import jc.house.R;
import jc.house.chat.util.EmojiUtils;
import jc.house.global.Constants;
import jc.house.utils.LogUtils;

public class EaseChatRowText extends EaseChatRow{

	private TextView contentView;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
		super(context, message, position, adapter);
	}

	@Override
	protected void onInflatView() {
		inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
				R.layout.jc_row_received_message : R.layout.jc_row_sent_message, this);
	}

	@Override
	protected void onFindViewById() {
		contentView = (TextView) findViewById(R.id.tv_chatcontent);
	}

    @Override
    public void onSetUpView() {
        /**TODO:设置聊天表情**/
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        Spannable span = EmojiUtils.getSmiledText(context, txtBody.getMessage());
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);
//        contentView.setText(txtBody.getMessage());
        if (message.direct == EMMessage.Direct.SEND) {
            //设置发送消息后的相关回调
            setMessageSendCallback();
            switch (message.status) {
            case CREATE: 
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
            case SUCCESS: // 发送成功
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case FAIL: // 发送失败，显示感叹号
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS: // 发送中
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
            default:
               break;
            }
        }
        //如果是接收的message
        else{
            //如果是收到的message还没有向发送者发送ack
            //那么赶紧ack收到的此消息
            if(!message.isAcked() && message.getChatType() == ChatType.Chat){
                try {
                    EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                    message.isAcked = true;
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    LogUtils.debug("message:" + message.getMsgId() + "ack失败！" + e.toString());
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        // TODO Auto-generated method stub
    }

}
