package jc.house.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.util.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jc.house.R;
import jc.house.chat.util.CommonUtils;
import jc.house.chat.util.EmojiUtils;
import jc.house.global.Constants;
import jc.house.global.MApplication;
import jc.house.models.CustomerHelper;

/**
 * 会话列表adapter
 */
public class ConversationListAdapter extends BaseAdapter {
    private static final String TAG = "ConversationListAdapter";

    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;

    private boolean notifyByFilter;
    private Context context;

    private Map<String, CustomerHelper> customerHelperMap;

    public ConversationListAdapter(Context context, List<EMConversation> conversationList) {
        this.context = context;
        this.conversationList = conversationList;
        copyConversationList = new ArrayList<>();
        copyConversationList.addAll(conversationList);
        customerHelperMap = ((MApplication) context.getApplicationContext()).customerHelperNameMapping;
    }

    @Override
    public int getCount() {
        //debug模式下返回一个item
        if (Constants.DEBUG && conversationList != null && conversationList.size() == 0)
            return 1;
        return conversationList != null ? conversationList.size() : (Constants.DEBUG ? 1 : 0);
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.jc_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);
            convertView.setTag(holder);
        }

        // 获取与此用户的会话
        EMConversation conversation = getItem(position);
        // 获取toChatUserName
        String huanxinid = conversation == null ? (!Constants.APPINFO.USER_VERSION ? "admin" : "wujie")
                : conversation.getUserName();
        //显示客服的头像
        String avatarUrl = findAvatarUserUrl(huanxinid);
        if (avatarUrl != null)
            loadImage(holder.avatar, avatarUrl);
        else
            /**set view**/
            holder.avatar.setImageResource(R.drawable.jc_default_avatar);

        //TODO 名字显示 用户版得知道映射规则
        //显示用户名的后六位
        String suffix = huanxinid.length() >= 6 ?
                (huanxinid.substring(huanxinid.length() - 6, huanxinid.length())) : huanxinid;
        holder.name.setText(Constants.APPINFO.USER_VERSION ?
                (customerHelperMap == null ?
                        huanxinid : (customerHelperMap.get(huanxinid) == null ?
                        huanxinid : customerHelperMap.get(huanxinid).getName()))
                : "顾客" + suffix);
        holder.huanxinid = huanxinid;

        if (conversation != null && conversation.getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        if (conversation != null && conversation.getMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();
            holder.message.setText(EmojiUtils.getSmiledText(this.context,
                            CommonUtils.getMessageDigest(lastMessage, (this.context))),
                    BufferType.SPANNABLE);

            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct == EMMessage.Direct.SEND
                    && lastMessage.status == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notifyByFilter) {
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notifyByFilter = false;
        }
    }

    /**
     * 加载用户头像
     *
     * @param imageView
     * @param url
     */
    private void loadImage(ImageView imageView, String url) {
        url = Constants.IMAGE_URL_ORIGIN + url;
        Picasso.with(context).load(url).
                placeholder(R.drawable.jc_default_avatar).
                error(R.drawable.jc_default_avatar)
                .into(imageView);
    }

    /**
     * 根据客服环信ID查找客服的头像
     *
     * @param huanxinID
     * @return
     */
    private String findAvatarUserUrl(String huanxinID) {
        CustomerHelper customerHelper = ((MApplication) this.context.getApplicationContext())
                .customerHelperNameMapping.get(huanxinID);
        if (customerHelper != null)
            return customerHelper.getPicUrl();
        else
            return null;
    }

    public static class ViewHolder {
        public TextView title;
        /**
         * 和谁的聊天记录
         */
        public TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        ImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_itease_layout;

        public String huanxinid;
    }
}

