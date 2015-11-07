package jc.house.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import jc.house.R;
import jc.house.utils.LogUtils;
import jc.house.utils.ToastUtils;
import jc.house.views.TitleBar;
import jc.house.widgets.ChatInputMenu;
import jc.house.widgets.ChatMessageList;
import jc.house.widgets.ChatExtendMenu;


/**
 * 2015-10-31
 */
public class ChatActivity extends Activity {
    public static final String TAG = "ChatActivity";
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;

    public static ChatActivity instance = null;

    protected int[] itemStrings = { R.string.attach_take_pic, R.string.attach_picture, R.string.attach_location };
    protected int[] itemsDrawables = { R.drawable.jc_chat_takepic_selector, R.drawable.jc_chat_image_selector,
            R.drawable.jc_chat_location_selector};
    protected int[] itemIds = { ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION };

    private TitleBar titleBar;

    private String toChatUserName;

    private ChatMessageList chatMsgList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private ChatInputMenu inputMenu;
    protected MyItemClickListener extendMenuItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**this must be called before setContentView() method**/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        init();
        initChatMsgList();
        //注册广播
        registerBroadcastReceiver();
        EMChat.getInstance().setAppInited();

        instance = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
        instance = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
    /**
     * 初始化各种视图
     */
    private void init(){
        this.titleBar = (TitleBar)findViewById(R.id.titlebar);
        this.titleBar.setTitle("会话");
        this.toChatUserName = getIntent().getStringExtra("toChatUserName");
        this.chatMsgList = (ChatMessageList)findViewById(R.id.message_list);

        this.swipeRefreshLayout = chatMsgList.getSwipeRefreshLayout();
        this.swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        //下方输入菜单
        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (ChatInputMenu)findViewById(R.id.input_menu);
        //注册扩展菜单项
        registerExtendMenuItem();
        inputMenu.init();
        inputMenu.setChatInputMenuListener(new ChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                // 发送文本消息
                sendTxtMessage(content,toChatUserName);
            }

            //发送语音
            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
//                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderCallback() {
//
//                    @Override
//                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
//                        // 发送语音消息
//                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
//                    }
//                });
                return false;
            }
        });


    }
    /**
     * 注册底部菜单扩展栏item; 覆盖此方法时如果不覆盖已有item，item的id需大于3
     */
    protected void registerExtendMenuItem(){
        for(int i = 0; i < itemStrings.length; i++){
            inputMenu.registerExtendMenuItem(itemStrings[i], itemsDrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }
    /**
     * 初始化聊天对话列表
     */
    private void initChatMsgList(){
        LogUtils.debug(TAG, "初始化ChatMsgList");
        LogUtils.debug(TAG, "和" + toChatUserName + "的聊天对话中有" +
                EMChatManager.getInstance().getConversation(toChatUserName).getUnreadMsgCount() +
                " 条未读");
        this.chatMsgList.init(toChatUserName, 0);
    }

    /**
     *发送TXT消息
     * @param content：发送的内容
     * @param toChatUserName：发送给谁
     */
    private void sendTxtMessage(String content,String toChatUserName){
        EMConversation conversation = EMChatManager.getInstance().getConversation(toChatUserName);
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //设置消息body
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(toChatUserName);
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
//        receiveMessage(message0);

        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.debug(TAG, "发送成功！");
                /**refresh chatMsgList**/
                ChatActivity.this.chatMsgList.refresh();
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.debug(TAG, "发送失败！");
            }

            @Override
            public void onProgress(int i, String s) {
                LogUtils.debug(TAG, "正在发送！");
            }
        });
    }

    /**注册新消息接受广播**/
    private void registerBroadcastReceiver() {
        LogUtils.debug(TAG, "注册新消息广播接收者");
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msgId = intent.getStringExtra("msgid");
                String from = intent.getStringExtra("from");
                //if receive other person's message ignore
                if(!from.equals(toChatUserName))
                    return;
                EMMessage message = EMChatManager.getInstance().getMessage(msgId);
                LogUtils.debug(TAG, "收到消息" + msgId);
                ToastUtils.debugShow(ChatActivity.this, "收到来自" + message.getFrom() + "的消息！");
                //refresh listview
                chatMsgList.refresh();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 注销广播监听着
     */
    private void unregisterBroadcastReceiver(){
        if(broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
        return;
    }

    /**
     * 假装收到一条消息
     * @param message
     */
    private void receiveMessage(EMMessage message){
        EMChatManager.getInstance().importMessage(message, true);
    }

    /**
     * 扩展菜单栏item点击事件
     *
     */
    class MyItemClickListener implements ChatExtendMenu.ChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
//            if(chatFragmentListener != null){
//                if(chatFragmentListener.onExtendMenuItemClick(itemId, view)){
//                    return;
//                }
//            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE: // 拍照
//                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
//                    selectPicFromLocal(); // 图库选择图片
                    break;
                case ITEM_LOCATION: // 位置
//                    startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;

                default:
                    break;
            }
        }

    }
}

