package jc.house.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import jc.house.R;
import jc.house.global.Constants;
import jc.house.global.MPicasso;

/**
 * Created by WuJie on 2016/3/12.
 */
public class ImageLoader {
    /**
     * resize image with Picasso
     * @param imageView
     * @param url 如果 url为文件名，则isOriginal参数有效，若为完整路径，则有效
     * @param isOriginal
     * @param targetWidth 不能等于0， 如果设置小于0，则默认加载全图，不进行压缩显示
     * @param targetHeight 不能等于0， 如果设置小于0，则默认加载全图，不进行压缩显示
     */
    public static void loadImage(ImageView imageView, String url, boolean isOriginal,
                                      int targetWidth, int targetHeight) {
        if(url == null) {
            loadImage(imageView, null);
            return;
        }
        //TODO 待完善
        if(url.contains("http:") ||
                url.contains("https:") ||
                url.contains("file:") ||
                url.contains("content:"));
        else
            url = (isOriginal ? Constants.IMAGE_URL_ORIGIN : Constants.IMAGE_URL_THUMBNAIL) + url;
        if(targetHeight < 0 || targetWidth < 0) {
            loadImage(imageView, url);
        } else if(targetHeight == 0 || targetWidth == 0){
            throw new RuntimeException("targetHeight and targetWidth should be more than zero");
        } else {
            MPicasso.getInstance()
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .resize(targetWidth, targetHeight)
                    .centerInside()
                    .placeholder(R.drawable.failure_image_red)
                    .error(R.drawable.failure_image_red)
                    .into(imageView);
        }
    }

    private static void loadImage(ImageView imageView, String url) {
        MPicasso.getInstance()
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.failure_image_red)
                .error(R.drawable.failure_image_red)
                .into(imageView);

    }
}
