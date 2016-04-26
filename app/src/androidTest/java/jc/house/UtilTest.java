package jc.house;

import android.test.AndroidTestCase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jc.house.models.HouseHelpers;
import jc.house.utils.GeneralUtils;

/**
 * Created by WuJie on 2016/1/11.
 */
public class UtilTest extends AndroidTestCase {
    public void test0() {
        Field[] fields = HouseHelpers.class.getDeclaredFields();
        for (int i = 0 ; i < fields.length; i++) {
            Log.v("field" + i, fields[i].getName() + "-" + fields[i].getType());
        }
    }
}
