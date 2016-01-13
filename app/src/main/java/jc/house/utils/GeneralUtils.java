package jc.house.utils;

import android.os.Build;
import android.os.Environment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * Created by WuJie on 2015/12/12.
 */
public class GeneralUtils {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static String getDateString(long timeStamp) {
        Date date = new Date(timeStamp * 1000);
        return sdf.format(date);
    }

    /**
     * 为每一个设备生成一个唯一的标示，此标示为MD5(Android版本号+手机型号+当前时间（yyyyMMddHHmmss+三位随机数)
     * @return
     */
    public static String getSystemIdentity() {
        String androidVersion = Build.VERSION.RELEASE;
        String phoneModel = Build.MODEL;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowtime = format.format(new Date());
        String random = String.format("%d", (int)(Math.random() * 1000));
        String id = MD5((androidVersion + phoneModel + nowtime + random).trim());
        LogUtils.debug("===GerneralUtil===", "SystemID is androidVersion:" + androidVersion
                        + "\nphoneModel: " + phoneModel
                + "\nnowtime: " + nowtime
                + "\nrandow: " + random
                + "\nid: " + id
        );
        LogUtils.debug("===GerneralUtil===","MD5() is " + id);
        return id;
    }

    public static String MD5(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
