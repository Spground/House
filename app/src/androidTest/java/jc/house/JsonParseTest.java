package jc.house;

import android.test.AndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

import jc.house.models.BaseModel;
import jc.house.models.JCActivity;
import jc.house.models.News;
import jc.house.utils.LogUtils;
import jc.house.utils.ParseJson;

/**
 * Created by WuJie on 2015/12/9.
 */
public class JsonParseTest extends AndroidTestCase {
    public void test01() {

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
        LogUtils.debug("===TAG===", jsonObject.toString());
        JCActivity modelInstance = (JCActivity)ParseJson.jsonObj2Model(jsonObject, JCActivity.class);
        assertNotNull(modelInstance);
        LogUtils.debug("===TAG===", modelInstance.getPicUrl() + "");
        LogUtils.debug("===TAG===",  modelInstance.getTitle() + "");
        LogUtils.debug("===TAG===",  modelInstance.getDetailUrl() + "");
        LogUtils.debug("===TAG===",  modelInstance.getId() + "");
        LogUtils.debug("===TAG===",  modelInstance.getPostTime()  + "");
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
        List<BaseModel> list = ParseJson.jsonArray2ModelList(jsonArray, JCActivity.class);
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
}
