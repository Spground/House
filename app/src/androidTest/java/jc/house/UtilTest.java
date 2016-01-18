package jc.house;

import android.test.AndroidTestCase;

import jc.house.utils.GeneralUtils;

/**
 * Created by WuJie on 2016/1/11.
 */
public class UtilTest extends AndroidTestCase {
    public void test0() {
        String id = GeneralUtils.getSystemIdentity();
        assertNotNull(id);
    }
}
