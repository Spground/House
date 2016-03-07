package jc.house.chat.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;

import de.greenrobot.event.EventBus;
import jc.house.chat.event.NewMessageEvent;
import jc.house.utils.LogUtils;

/**
 * Created by WuJie on 2015/11/9.
 */
public class ReceiveNewMessageService extends BaseService {
    private BroadcastReceiver receiver;
    public static final String INTENT_ACTION = "RECEIVE_NEW_MESSAGE_SERVICE";

    private static final String TAG = "ReceiveNewMessageService";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.debug(TAG, "onStartCommand is invoked!");
        if(receiver == null){
            receiver = new NewMessageReceiver();
            //register broadcast receiver
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
            intentFilter.addAction(EMChatManager.getInstance().getNewMessageBroadcastAction());
            registerReceiver(receiver, intentFilter);
            LogUtils.debug(TAG, "NewMessageService is started up!");
        }
        // If i am killed, please restart me!
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.debug(TAG, "onDestroy() is invoked!");
        unregisterReceiver();
    }

    private void unregisterReceiver(){
        if(receiver != null)
            unregisterReceiver(receiver);
    }

    /**
     * be responsible for receiving new message
     */
    class NewMessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            /**post an new message event **/
            LogUtils.debug(TAG, "received a new message broadcast");
            NewMessageEvent event = new NewMessageEvent(intent);
            EventBus.getDefault().post(event);
            //abort the broadcast
            abortBroadcast();
        }
    }
}
