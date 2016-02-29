package jc.house.chat.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.squareup.picasso.Picasso;

import jc.house.R;
import jc.house.activities.HouseDetailActivity;
import jc.house.global.Constants;
import jc.house.utils.LogUtils;

/**
 * Created by WuJie on 2015/12/16.
 */
public class EaseChatRowHouse extends EaseChatRow {

    private TextView house_name, avgprice, tag;
    private String str_house_name, str_avgprice, str_tag;

    private ImageView house_imageView;
    private String img_url;

    public EaseChatRowHouse(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        try {
            str_house_name = message.getStringAttribute("house_name");
            str_avgprice = message.getStringAttribute("avgprice");
            str_tag = message.getStringAttribute("tag");
            img_url = Constants.IMAGE_URL + message.getStringAttribute("img_url");
            LogUtils.debug("===IMAGE_URL===", img_url);
        } catch (EaseMobException e) {
            e.printStackTrace();
            str_house_name = "NULL";
            str_avgprice = "NULL";
            str_tag = "NULL";
        }

    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
                        R.layout.jc_row_received_house : R.layout.jc_row_sent_house,
                this);
    }

    @Override
    protected void onFindViewById() {
        house_name = (TextView)findViewById(R.id.house_name);
        avgprice = (TextView)findViewById(R.id.avgprice);
        tag = (TextView)findViewById(R.id.tag);
        house_imageView = (ImageView)findViewById(R.id.house_image_view);
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
        house_name.setText(str_house_name);
        avgprice.setText(str_avgprice);
        tag.setText(str_tag);
        Picasso.with(context).setIndicatorsEnabled(true);
        Picasso.with(context).load(img_url)
                .placeholder(R.drawable.failure_image_red)
                .error(R.drawable.failure_image_red)
                .into(house_imageView);

    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, HouseDetailActivity.class);
        intent.putExtra(HouseDetailActivity.FLAG_ID, message.getIntAttribute(HouseDetailActivity.FLAG_ID, -1));
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }
}
