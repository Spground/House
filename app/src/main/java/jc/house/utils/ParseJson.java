package jc.house.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jc.house.models.BaseModel;

/**
 * Created by hzj on 2015/11/1.
 */
public final class ParseJson {

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
            Field field = null;
            try {
                //get current class's field
                field = clazz.getDeclaredField(key);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                LogUtils.debug("===TAG===", e.toString());
            }

            //if field is null, go to find superclass public field
            if(field == null) {
                try {
                    field = clazz.getField(key);
                    LogUtils.debug("===TAG===",  "find superclass field " + key);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    LogUtils.debug("===TAG===", e.toString());
                }
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
