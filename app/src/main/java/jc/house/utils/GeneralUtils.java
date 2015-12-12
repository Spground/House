package jc.house.utils;

import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Created by WuJie on 2015/12/12.
 */
public class GeneralUtils {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static String getDateString(long timeStamp) {
        Date date = new Date(timeStamp * 1000);
        return sdf.format(date);
    }
}
