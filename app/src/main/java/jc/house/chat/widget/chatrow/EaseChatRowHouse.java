package jc.house.chat.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.easemob.chat.EMMessage;

import jc.house.R;
import jc.house.activities.HouseDetailActivity;

/**
 * Created by WuJie on 2015/12/16.
 */
public class EaseChatRowHouse extends EaseChatRow {

    public EaseChatRowHouse(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
                        R.layout.jc_row_received_house : R.layout.jc_row_sent_house,
                this);
    }

    @Override
    protected void onFindViewById() {

    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {

    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, HouseDetailActivity.class);
        intent.putExtra(HouseDetailActivity.FLAG_ID, message.getIntAttribute(HouseDetailActivity.FLAG_ID, -1));
        context.startActivity(intent);
    }
}
