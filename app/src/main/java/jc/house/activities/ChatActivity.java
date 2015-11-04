package jc.house.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
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
    private SwipeRefreshLayout swipeRefreshLayout;

    private Button sendBtn;
    private EditText inputEText;

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**this must be called before setContentView() method**/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        initView();
        this.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTxtMessage(ChatActivity.this.inputEText.getText().toString(),
                        ChatActivity.this.toChatUserName);
                //clear input text
                ChatActivity.this.inputEText.setText("");
            }
        });
        initChatMsgList();
        //注册广播
        registerBroadcastReceiver();
        EMChat.getInstance().setAppInited();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }

    /**
     * 初始化各种视图
     */
    private void initView(){
        this.titleBar = (TitleBar)findViewById(R.id.titlebar);
        this.titleBar.setTitle("会话");
        this.toChatUserName = getIntent().getStringExtra("toChatUserName");
        this.chatMsgList = (ChatMessageList)findViewById(R.id.message_list);
        this.sendBtn = (Button)findViewById(R.id.sendBtn);
        this.inputEText = (EditText)findViewById(R.id.inputEText);
        swipeRefreshLayout = chatMsgList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
    }
    /**
     * 初始化聊天对话列表
     */
    private void initChatMsgList(){
        LogUtils.debug("初始化ChatMsgList");
        LogUtils.debug("和" + toChatUserName + "的聊天对话中有" +
                EMChatManager.getInstance().getConversation(toChatUserName).getUnreadMsgCount() +
                " 条未读");
        this.chatMsgList.init(toChatUserName, 0);
    }

    /**
     * 发送TXT消息
     */
    private void sendTxtMessage(String content,String username){
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //设置消息body
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(username);
        //把消息加入到此会话对象中
        conversation.addMessage(message);
        //发送消息

        /**发送一条消息**/
        EMMessage message0 = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        TextMessageBody txtBody0 = new TextMessageBody(content);
        message0.addBody(txtBody0);
        message0.setFrom("admin");
        message0.setMsgTime(System.currentTimeMillis() + 1000 * 20);
        message0.direct = EMMessage.Direct.RECEIVE;
        message0.setTo("wujie");
        receiveMessage(message0);

        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.debug("发送成功！");
                /**refresh chatMsgList**/
                ChatActivity.this.chatMsgList.refresh();
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

    /**注册新消息接受广播**/
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String msgId = intent.getStringExtra("msgid");
                EMMessage message = EMChatManager.getInstance().getMessage(msgId);
                LogUtils.debug("收到消息" + msgId);
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 注销广播监听着
     */
    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    /**
     * 假装收到一条消息
     * @param message
     */
    private void receiveMessage(EMMessage message){
        EMChatManager.getInstance().importMessage(message, true);
    }
}

