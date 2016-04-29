package jc.house;

import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jc.house.models.CustomerHelper;
import jc.house.models.HouseHelpers;
import jc.house.utils.GeneralUtils;
import jc.house.utils.ParseJson;

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

    //测试新的Parson解析List字段的case
    public void test1() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject("{ \"name\": \"连大•文润金宸\", \"id\": 11, \"helpers\": [ { \"id\": 1, \"hxID\": \"nannan\", \"name\": \"张楠楠\", \"picUrl\": \"1457399788.jpg\" }, { \"id\": 3, \"hxID\": \"ly\", \"name\": \"李苗\", \"picUrl\": \"1457580174.jpg\" } ] }");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HouseHelpers helpers = (HouseHelpers) ParseJson.jsonObj2Model(jsonObject, HouseHelpers.class);
        assertNotNull(helpers);
        Log.v("===TEST1===", helpers.getName());
        for(CustomerHelper helper : helpers.getHelpers())
            Log.v("===TEST1===", helper.getName());

    }
}
