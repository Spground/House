package jc.house.chat.widget.chatrow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;

import java.io.File;

import jc.house.R;
import jc.house.chat.util.ChatImageCache;
import jc.house.chat.util.ChatImageUtils;
import jc.house.chat.util.CommonUtils;
import jc.house.utils.LogUtils;
import jc.house.utils.ToastUtils;

/**
 * Created by WuJie on 2015/12/5.
 */
public class EaseChatRowImage extends EaseChatRow {

    protected ImageView imageView;
    private ImageMessageBody imgBody;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
                        R.layout.jc_row_received_picture : R.layout.jc_row_sent_picture,
                this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {
        imgBody = (ImageMessageBody) message.getBody();
        // 接收方向的消息
        if (message.direct == EMMessage.Direct.RECEIVE) {
            if (message.status == EMMessage.Status.INPROGRESS) {
                imageView.setImageResource(R.drawable.jc_default_image);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.jc_default_image);
                if (imgBody.getLocalUrl() != null) {
                    // String filePath = imgBody.getLocalUrl();
                    //远程的图片路径
                    String remotePath = imgBody.getRemoteUrl();
                    //本地的图片路径
                    String filePath = ChatImageUtils.getImagePath(remotePath);
                    //远程的缩略图路径
                    String thumbRemoteUrl = imgBody.getThumbnailUrl();
                    //本地的缩略图路径
                    String thumbnailPath = ChatImageUtils.getThumbnailImagePath(thumbRemoteUrl);
                    showImageView(thumbnailPath, imageView, filePath, message);
                }
            }
            return;
        }
    //  是发送出去的图片消息
        String filePath = imgBody.getLocalUrl();
        if (filePath != null) {
            showImageView(ChatImageUtils.getThumbnailImagePath(filePath), imageView, filePath, message);
        }
        handleSendMessage();
    }

    @Override
    protected void onBubbleClick() {
        //点击可以查看全图
        ToastUtils.debugShow(activity, "暂时还不能查看全图");
    }

    /**
     * load image into image view
     *
     * @param thumbernailPath 缩略图的路径
     * @param iv 显示的imageview
     * @param localFullSizePath 本地完整图片的路径
     * @param message 对应图片的的消息对象
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv,
                                  final String localFullSizePath,final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        final Bitmap bitmap = ChatImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            //异步去磁盘加载，并调整尺寸，显示缩略图片
            new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    //缩略图可能不存在磁盘上
                    //TODO
                    if (file.exists()) {
                        BitmapFactory.Options options = ChatImageUtils.getBitmapOptions(thumbernailPath);
                        int originalWidth = options.outWidth;
                        int originalHeight = options.outHeight;
                        if(originalWidth < 280 || originalHeight < 280){
                            //image is two small, so zoom in to 280 * 280
                            Bitmap source = BitmapFactory.decodeFile(thumbernailPath);
                            int w = source.getWidth();
                            int h = source.getHeight();
                            float sx =(float)380 / w;//
                            float sy =(float)380 / h;
                            float r = sx > sy ? sx : sy;
                            LogUtils.debug("===TAG===", "r is " + r + " w is " + w + " h is " + h);
                            Matrix matrix = new Matrix();
                            matrix.postScale(r, r); // 长和宽放大缩小的比例
                            Bitmap resizeBmp = Bitmap.createBitmap(source, 0, 0, w,
                                    h, matrix, true);
                            source.recycle();
                            return resizeBmp;
                        }
                        return ChatImageUtils.decodeScaleImage(thumbernailPath, 280, 280);
                    } else {
                        if (message.direct == EMMessage.Direct.SEND) {
                            return ChatImageUtils.decodeScaleImage(localFullSizePath, 280, 280);
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
                        //内存LRU缓存
                        ChatImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status == EMMessage.Status.FAIL) {
                            if (CommonUtils.isNetWorkConnected(activity)) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EMChatManager.getInstance().asyncFetchMessage(message);
                                    }
                                }).start();
                            }
                        }

                    }
                }
            }.execute();

            return true;
        }
    }

    /**
     * 处理发送消息的消息的状态变化
     */
    protected void handleSendMessage() {
        setMessageSendCallback();
        switch (message.status) {
            case SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                if(percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.INVISIBLE);
                break;
            case FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                if(percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                if(percentageView != null){
                    percentageView.setVisibility(View.VISIBLE);
                    percentageView.setText(message.progress + "%");
                }
                statusView.setVisibility(View.INVISIBLE);
                break;
            default:
                progressBar.setVisibility(View.VISIBLE);
                if(percentageView != null){
                    percentageView.setVisibility(View.VISIBLE);
                    percentageView.setText(message.progress + "%");
                }
                statusView.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
