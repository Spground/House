package jc.house.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jc.house.models.BaseModel;
import jc.house.models.House;
import jc.house.models.News;

/**
 * Created by hzj on 2015/11/1.
 */
public final class ParseJson {

    public static List<News> parseNews(JSONArray array) {
        List<News> news = new ArrayList<>();
        if (null == array || array.length() == 0) {
            return news;
        }
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                if (item.has("id") && item.has("picUrl") && item.has("title") && item.has("author") && item.has("time")) {
                    int id = item.getInt("id");
                    String url = item.getString("picUrl");
                    String title = item.getString("title");
                    String author = item.getString("author");
                    String time = item.getString("time");
                    news.add(new News(id, url, title, author, time));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

    public static List<House> parseHouse(JSONArray array) {
        List<House> houses = new ArrayList<>();
        if (null == array || array.length() == 0) {
            return houses;
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject item = array.getJSONObject(i);
                if (item.has("id") && item.has("url") && item.has("name") && item.has("phone") && item.has("intro") && item.has("lat") && item.has("lng")) {
                    int id = item.getInt("id");
                    String url = item.getString("url");
                    String name = item.getString("name");
                    String phone = item.getString("phone");
                    String intro = item.getString("intro");
                    double lat = item.getDouble("lat");
                    double lng = item.getDouble("lng");
                    houses.add(new House(id, url, name, intro, phone, lat, lng));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return houses;
    }


    /**
     * json对象数组转对应的model对象List(json数组必须是同类)
     * @param array
     * @param clazz model的class对象
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static List<BaseModel> jsonArray2ModelList(JSONArray array,  Class<? extends BaseModel> clazz) {
        List<BaseModel> modelList = new ArrayList<>();
        if (null == array || array.length() == 0) {
            return null;
        }
        for(int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.optJSONObject(i);
            BaseModel modelObj = jsonObj2Model(jsonObject, clazz);
            modelList.add(modelObj);
        }
        return modelList;
    }

    /**
     * json对象转model对象
     * @param jsonObject
     * @param clazz
     * @return
     */
    public static BaseModel jsonObj2Model(JSONObject jsonObject, Class<? extends BaseModel> clazz) {
        BaseModel modelObj;
        try {
            modelObj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            LogUtils.debug("===TAG===", e.toString());
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LogUtils.debug("===TAG===", e.toString());
            return null;
        }

        Iterator<String> iterator = jsonObject.keys();
        while(iterator.hasNext()) {
            String key = iterator.next();
            Object value = null;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtils.debug("===TAG===", e.toString());
            }
            Field field;
            try {
                field = clazz.getDeclaredField(key);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                LogUtils.debug("===TAG===", e.toString());
                continue;
            }
            if(field != null) {
                try {
                    field.setAccessible(true);
                    field.set(modelObj, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    LogUtils.debug("===TAG===", e.toString());
                }
            }
        }
        return modelObj;
    }
}
