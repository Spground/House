package jc.house.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import jc.house.R;
import jc.house.utils.LogUtils;
import jc.house.views.TitleBar;
import jc.house.widgets.ChatMessageList;

/**
 * 2015-10-31
 */
public class ChatActivity extends Activity {

    private TitleBar titleBar;
    private String toChatUserName;

    private ChatMessageList chatMsgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**this must be called before setContentView() method**/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        this.titleBar = (TitleBar)findViewById(R.id.titlebar);
        this.titleBar.setTitle("会话");
        this.toChatUserName = getIntent().getStringExtra("toChatUserName");
        this.chatMsgList = (ChatMessageList)findViewById(R.id.message_list);
        initChatMsgList();
    }

    private void initChatMsgList(){
        LogUtils.debug("初始化ChatMsgList");
        this.chatMsgList.init(toChatUserName,0);
        sendTxtMessage("Hello Admin" + Math.random(),"admin");
    }

    /**发送消息**/
    private void sendTxtMessage(String content,String username){
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
//创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//如果是群聊，设置chattype,默认是单聊
        message.setChatType(EMMessage.ChatType.GroupChat);
//设置消息body
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);
//设置接收人
        message.setReceipt(username);
//把消息加入到此会话对象中
        conversation.addMessage(message);
//发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack(){
            @Override
            public void onSuccess() {
                LogUtils.debug("发送成功！");
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.debug("发送失败！");
            }

            @Override
            public void onProgress(int i, String s) {
                LogUtils.debug("正在发送！");
            }
        });
    }
}

