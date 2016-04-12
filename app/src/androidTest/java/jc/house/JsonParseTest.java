package jc.house;

import android.test.AndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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

    public void test02() {
        int i = 3;
        JSONArray jsonArray;
        jsonArray = new JSONArray();
        while(--i >= 0) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title", "JC");
                jsonObject.put("picUrl", "http://www.baidu.com");
                jsonObject.put("id", 1000);
                jsonObject.put("detailUrl", "http://");
                jsonObject.put("postTime", 1000L);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonArray.put(i,jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LogUtils.debug("===TAG===", jsonArray.toString());
        List<BaseModel> list = (List<BaseModel>)ParseJson.jsonArray2ModelList(jsonArray, JCActivity.class);
        assertNotNull(list);
        LogUtils.debug("===TAG===", "list size is" + list.size());
        LogUtils.debug("===TAG===",  "list is " + list.toString());
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

    public void test05() {
        String methodName = StringUtils.getMethodNameByFieldName("name");
        assertEquals("setName",methodName);
        Class mClass = HouseDetail.class;
        try {
            try {
                Object object = mClass.newInstance();
                Method method = mClass.getMethod(methodName, String.class);
                try {
                    method.invoke(object,"");
                    assertTrue("name is grace", ((HouseDetail) object).getName() != null);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void test06() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", null);
            jsonObject.put("picUrl", "http://www.baidu.com");
            jsonObject.put("id", 10000);
            jsonObject.put("detailUrl", "http://");
            jsonObject.put("postTime", 1000L);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JCActivity activity = (JCActivity)ParseJson.jsonObj2Model(jsonObject, JCActivity.class);
        assertNotNull("activity is not null", activity != null);
        assertTrue("id is 10000", activity.getId() == 10000);
//        assertTrue("title is JC", activity.getTitle().equals("JC"));
        assertTrue("picUrl is not ok", activity.getPicUrl().equals("http://www.baidu.com"));
    }
        /*
        Class mClass = HouseDetail.class;
        try {
            Method method = mClass.getMethod("setId",Integer.class);
            assertNotNull("no setId method", method != null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Set<String> sets = new HashSet<String>();
        while(mClass != Object.class) {
            Field[] fields = mClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                sets.add(fields[i].getName());
            }
            mClass = mClass.getSuperclass();
        }
        assertTrue("sets is not empty", sets.size() == 15);
    }
    */

    public void test07() {
//        boolean flag = ParseJson.isSubclassOfBaseModel(CustomerHelper.class);
        boolean flag = BaseModel.class.isAssignableFrom(CustomerHelper.class);
        assertTrue("customerhelper is subclass of basemodel", flag);

    }

    public void testMapFromClass() {
        Map<String, Class> map = ParseJson.mapFromClass(JCActivity.class);
        for (String key : map.keySet()) {
            LogUtils.debug("TEST", key + " ===> " + map.get(key).getSimpleName());
        }
    }
}
