package jc.house.chat.event;

import android.content.Intent;

import com.easemob.chat.EMMessage;

/**
 * Created by WuJie on 2015/11/9.
 */
public class NewMessageEvent extends BaseEvent {
    private EMMessage message;
    private Intent intent;

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public EMMessage getMessage() {
        return message;
    }

    public void setMessage(EMMessage message) {
        this.message = message;
    }

    public NewMessageEvent(Intent intent){
        this.intent = intent;
    }

    public NewMessageEvent(EMMessage message){
        this.message = message;
    }


}
