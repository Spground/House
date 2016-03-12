package jc.house.chat.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Direct;
import com.easemob.util.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.Date;

import jc.house.R;
import jc.house.chat.widget.ChatMessageList;
import jc.house.global.Constants;
import jc.house.global.MApplication;
import jc.house.models.CustomerHelper;
import jc.house.utils.GeneralUtils;
import jc.house.utils.ImageLoader;
import jc.house.utils.LogUtils;
import jc.house.utils.ToastUtils;

public abstract class EaseChatRow extends LinearLayout {
    protected static final String TAG = EaseChatRow.class.getSimpleName();

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected EMCallBack messageSendCallback;
    protected EMCallBack messageReceiveCallback;

    protected ChatMessageList.MessageListItemClickListener itemClickListener;

    public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        onInflatView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        ackedView = (TextView) findViewById(R.id.tv_ack);
        deliveredView = (TextView) findViewById(R.id.tv_delivered);
        onFindViewById();
    }

    /**
     * 根据当前message和position设置控件属性等
     *
     * @param message           ChatRow对应的Message对象
     * @param position          ChatRow在ListView中的位置
     * @param itemClickListener ChatRow的点击事件监听者
     */
    public void setUpView(EMMessage message, int position,
                          ChatMessageList.MessageListItemClickListener itemClickListener) {

        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;

        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    /**
     * 设置基本的View
     */
    private void setUpBaseView() {
        // 设置用户昵称头像，bubble背景等
        String huanxinID;
        String avatarUrl;
        //TODO 设置用户的头像
        huanxinID = this.message.getFrom();
        LogUtils.debug(TAG, "the message is from " + huanxinID);
        avatarUrl = findAvatarUserUrl(huanxinID);
        LogUtils.debug(TAG, "avatarUrl in EaseChatRow is " + (avatarUrl == null ? "null" : avatarUrl));
        //压缩显示头像
        if (avatarUrl != null)
            ImageLoader.loadImage(this.userAvatarView, avatarUrl, false,
                    GeneralUtils.dip2px(context, 48),
                    GeneralUtils.dip2px(context, 48));
        /** timestamp visible logic **/
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            //ListView中的第一行，显示此Message对象的时间戳
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // 两条消息时间离得如果稍长，显示时间
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }

        //设置已发送提示
        if (deliveredView != null) {
            if (message.isDelivered) {
                deliveredView.setVisibility(View.VISIBLE);
            } else {
                deliveredView.setVisibility(View.INVISIBLE);
            }
        }

        //设置消息发送的回执
        if (ackedView != null) {
            if (message.isAcked) {
                if (deliveredView != null) {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
                ackedView.setVisibility(View.VISIBLE);
            } else {
                ackedView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 设置消息发送callback
     */
    protected void setMessageSendCallback() {
        if (messageSendCallback == null) {
            messageSendCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (percentageView != null)
                                percentageView.setText(progress + "%");
                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }

    /**
     * 设置消息接收callback
     */
    protected void setMessageReceiveCallback() {
        if (messageReceiveCallback == null) {
            messageReceiveCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (percentageView != null) {
                                percentageView.setText(progress + "%");
                            }
                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageReceiveCallback);
    }

    /**
     * 设置该ChatRow Bubble的各种点击事件
     * Bubble的长按和短按
     * 发送状态的点击(重发)
     * 头像的点击
     */
    private void setClickListener() {
        if (bubbleLayout != null) {
            bubbleLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (!itemClickListener.onBubbleClick(message)) {
                            //如果listener返回false不处理这个事件，执行lib默认的处理
                            onBubbleClick();
                        }
                    }
                }
            });
            //长按的事件监听
            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        /**设置状态视图的点击事件监听比如发送失败的“感叹号”==**/
        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        //重发消息
                        itemClickListener.onResendClick(message);
                    }
                }
            });
        }

        /**设置头像的点击事件监听**/
        userAvatarView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    if (message.direct == Direct.SEND) {
                        itemClickListener.onUserAvatarClick(EMChatManager.getInstance().getCurrentUser());
                    } else {
                        itemClickListener.onUserAvatarClick(message.getFrom());
                    }
                }
            }
        });
    }

    /**
     * 根据Message对象的状态
     * 更新当前的ChatRow视图
     */
    protected void updateView() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (message.status == EMMessage.Status.FAIL) {

                    if (message.getError() == EMError.MESSAGE_SEND_INVALID_CONTENT) {
                        ToastUtils.show(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_invalid_content));
                    } else if (message.getError() == EMError.MESSAGE_SEND_NOT_IN_THE_GROUP) {
                        ToastUtils.show(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_not_in_the_group));
                    } else {
                        ToastUtils.show(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast));
                    }
                }

                onUpdateView();
            }
        });

    }

    /**
     * 填充layout
     */
    protected abstract void onInflatView();

    /**
     * 查找chatrow里的控件
     */
    protected abstract void onFindViewById();

    /**
     * 消息状态改变，刷新ListView
     */
    protected abstract void onUpdateView();

    /**
     * 设置更新控件属性
     */
    protected abstract void onSetUpView();

    /**
     * 聊天气泡被点击事件
     */
    protected abstract void onBubbleClick();

    /**
     * 根据客服环信ID查找客服的头像
     *
     * @param huanxinID
     * @return
     */
    protected String findAvatarUserUrl(String huanxinID) {
        CustomerHelper customerHelper = ((MApplication) this.context.getApplicationContext())
                .customerHelperNameMapping.get(huanxinID);
        if (customerHelper != null)
            return customerHelper.getPicUrl();
        else
            return null;
    }
}
