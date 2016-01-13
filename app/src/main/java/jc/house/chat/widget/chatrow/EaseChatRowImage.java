package jc.house.chat.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;

import java.io.File;

import jc.house.R;
import jc.house.activities.PhotoViewActivity;
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
        if(imgBody == null)
            return;
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
                    String localThumbnailPath = ChatImageUtils.getThumbnailImagePath(thumbRemoteUrl);
                    showImageView(localThumbnailPath, imageView, filePath, message);
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
        /**消息状态不是成功的直接不能查看全图**/
        if(this.message.status != EMMessage.Status.SUCCESS) {
            Log.v("===TAG===", "clicked message is not success");
            return;
        }

        Intent showBigImgIntent = new Intent(context, PhotoViewActivity.class);
        String url;
        ImageMessageBody body = (ImageMessageBody)this.message.getBody();
        if(this.message.direct == EMMessage.Direct.SEND) {
            //TODO
            /**使用本地的全图, 让picasso自己去压缩显示**/
            Log.v("===TAG===", "image getLocalUrl is :" + body.getLocalUrl());
            url = "file:" + body.getLocalUrl();
        } else {
            /**接收到的图片, 显示原图 而非 本地的缩略图**/
            url = body.getRemoteUrl();
        }

        Log.v("===TAG===", "image url is :" + url);
        showBigImgIntent.putExtra(PhotoViewActivity.FLAG_IMAGE_URL, url);
        context.startActivity(showBigImgIntent);
    }

    /**
     * load image into image view
     *
     * @param localThumbnailPath 本地缩略图的路径
     * @param iv 显示的ImageView
     * @param localFullSizePath 本地完整图片的路径
     * @param message 对应图片的的消息对象
     * @return the image exists or not
     */
    private boolean showImageView(final String localThumbnailPath, final ImageView iv,
                                  final String localFullSizePath,final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        final Bitmap bitmap = ChatImageCache.getInstance().get(localThumbnailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            //异步去磁盘加载，并调整尺寸，显示缩略图片
            new AsyncTask<Object, Void, Bitmap>() {
                /**
                 *  放大那些小的图片
                 * @param path
                 * @param expectedWidth
                 * @param expectedHeight
                 * @return
                 */
                public Bitmap zoomInSmallImage(String path, int expectedWidth, int expectedHeight) {
                        Bitmap source = BitmapFactory.decodeFile(path);
                        //image is two small, so zoom in to 280 * 280
                        if(source == null)
                            return null;
                        int w = source.getWidth();
                        int h = source.getHeight();
                        float sx =(float)expectedWidth / w;//
                        float sy =(float)expectedHeight / h;
                        float r = sx > sy ? sx : sy;
                        LogUtils.debug("===TAG===", "r is " + r + " w is " + w + " h is " + h);
                        Matrix matrix = new Matrix();
                        matrix.postScale(r, r); // 长和宽放大缩小的比例
                        Bitmap resizeBmp = Bitmap.createBitmap(source, 0, 0, w,
                                h, matrix, true);
                        source.recycle();
                        return resizeBmp;
                }
                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(localThumbnailPath);
                    //缩略图可能不存在磁盘上
                    //TODO
                    if (file.exists()) {
                        BitmapFactory.Options options = ChatImageUtils.getBitmapOptions(localThumbnailPath);
                        int originalWidth = options.outWidth;
                        int originalHeight = options.outHeight;
                        //image is too small, so zoom in to 380 * 380
                        if(originalWidth < 280 || originalHeight < 280)
                            return zoomInSmallImage(localThumbnailPath, 380, 380);
                        else
                            return ChatImageUtils.decodeScaleImage(localThumbnailPath, 280, 280);
                    } else {
                        if (message.direct == EMMessage.Direct.SEND) {
                            BitmapFactory.Options options = ChatImageUtils.getBitmapOptions(localFullSizePath);
                            int originalWidth = options.outWidth;
                            int originalHeight = options.outHeight;
                            if(originalHeight < 280 || originalWidth < 280)
                                return zoomInSmallImage(localFullSizePath, 380, 380);
                            else
                                return ChatImageUtils.decodeScaleImage(localFullSizePath, 280, 280);
                        } else {
                            //just return default image
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
                        //内存LRU缓存
                        ChatImageCache.getInstance().put(localThumbnailPath, image);
                    } else {
                        if (message.status == EMMessage.Status.FAIL && message.direct == EMMessage.Direct.RECEIVE) {
                            if (CommonUtils.isNetWorkConnected(activity)) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EMChatManager.getInstance().asyncFetchMessage(message);
                                    }
                                }).start();
                            }
                        }
                        else {
                            //can't find any image to show, so just show default image
                            imageView.setImageResource(R.drawable.jc_default_image);
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
