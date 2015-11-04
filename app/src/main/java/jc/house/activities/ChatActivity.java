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
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**this must be called before setContentView() method**/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        this.titleBar = (TitleBar)findViewById(R.id.titlebar);
        this.titleBar.setTitle("会话");
        this.userName = getIntent().getStringExtra("userName");
    }

}

