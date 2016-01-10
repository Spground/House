package jc.house;

import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jc.house.models.BaseModel;
import jc.house.models.CustomerHelper;
import jc.house.models.HouseDetail;
import jc.house.models.JCActivity;
import jc.house.models.News;
import jc.house.utils.LogUtils;
import jc.house.utils.ParseJson;
import jc.house.utils.StringUtils;

/**
 * Created by WuJie on 2015/12/9.
 */
public class JsonParseTest extends AndroidTestCase {
    public void test01() {

          JSONObject jsonObject = new JSONObject();
          try {
              jsonObject.put("title", "JC");
              jsonObject.put("picUrl", "http://www.baidu.com");
              jsonObject.put("id", 10000);
              jsonObject.put("detailUrl", "http://");
              jsonObject.put("postTime", 1000L);
          } catch (JSONException e) {
              e.printStackTrace();
          }
        LogUtils.debug("===TAG===", jsonObject.toString());
        JCActivity modelInstance = (JCActivity)ParseJson.jsonObj2Model(jsonObject, JCActivity.class);
        assertNotNull(modelInstance);
        assertEquals(10000, modelInstance.id);
        LogUtils.debug("===TAG===", modelInstance.getPicUrl() + "");
        LogUtils.debug("===TAG===",  modelInstance.getTitle() + "");
        System.err.print("id is" + modelInstance.id + "");
        LogUtils.debug("===TAG===", modelInstance.getPostTime()  + "");
    }

    public void test03() {
        Class mClass = News.class;
        Field[] fields = mClass.getFields();
        for (int i = 0; i < fields.length; i++) {
            LogUtils.debug("ClassFields" + i + fields[i].getName());
        }
    }

    public void test04() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("houseType", "JC");
            jsonObject.put("forceType", "http://www.baidu.com");
            jsonObject.put("id", 10000);
            jsonObject.put("avgPrice", "http://");
            jsonObject.put("trafficLines", "55");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.debug("===TAG===", jsonObject.toString());
        HouseDetail modelInstance = (HouseDetail)ParseJson.jsonObj2Model(jsonObject, HouseDetail.class);
        assertNotNull(modelInstance);
        assertEquals(10000, modelInstance.id);
    }

    public void test07() {
//        boolean flag = ParseJson.isSubclassOfBaseModel(CustomerHelper.class);
        boolean flag = BaseModel.class.isAssignableFrom(CustomerHelper.class);
        assertTrue("customerhelper is subclass of basemodel", flag);

    }
}
