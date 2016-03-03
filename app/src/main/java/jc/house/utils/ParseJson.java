package jc.house.utils;

import android.animation.PropertyValuesHolder;
import android.os.Debug;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jc.house.models.BaseModel;

/**
 * Created by hzj on 2015/11/1.
 */
public final class ParseJson {

    /**
     * json对象数组转对应的model对象List
     *
     * @param array  A JSONArray object
     * @param clazz model对应的的Class
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static final List<? extends BaseModel> jsonArray2ModelList(JSONArray array, Class<? extends BaseModel> clazz) {
        List<BaseModel> modelList = new ArrayList<>();
        if (null == array || array.length() == 0) {
            return modelList;
        }
        Map<String, Class> fieldMap = mapFromClass(clazz);
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.optJSONObject(i);
            BaseModel modelObj = jsonObj2Model(jsonObject, clazz, fieldMap);
            modelList.add(modelObj);
        }
        return modelList;
    }

    /**
     * 说明参见下面的方法
     *
     * @param object
     * @param mClass
     * @return
     */
    public static final BaseModel jsonObj2Model(JSONObject object, Class<? extends BaseModel> mClass) {
        if (null == object || null == mClass) {
            throw new NullPointerException("object and mClass should not be null");
        }
        Map<String, Class> fieldMap = mapFromClass(mClass);
        return jsonObj2Model(object, mClass, fieldMap);
    }

    /**
     * 将JSONObject数据解析成对应某个类的对象
     *
     * @param object JSONObject对象
     * @param mClass 对应的model对象的Class
     * @param fieldMap mClass对应的（属性值-class）键值对
     * @return mClass对应的一个对象
     */
    private static final BaseModel jsonObj2Model(JSONObject object, Class<? extends BaseModel> mClass, Map<String, Class> fieldMap) {

        if (null == object || null == mClass || null == fieldMap) {
            throw new NullPointerException("object and mClass and fieldMap should not be null");
        }
        BaseModel result = null;
        try {
            result = mClass.newInstance();
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (fieldMap.containsKey(key)) {
                    try {
                        Method method = mClass.getMethod(StringUtils.getMethodNameByFieldName(key), fieldMap.get(key));
                        try {
                            if (JSONObject.NULL != object.get(key) && !object.isNull(key)) {
                                if (isSubclassOfBaseModel(fieldMap.get(key))) {
                                    //递归赋值
                                    method.invoke(result, jsonObj2Model((object.optJSONObject(key)), fieldMap.get(key)));
                                } else if (isSubclassOfList(fieldMap.get(key))) {
                                    method.invoke(result, jsonArray2ModelList((object.optJSONArray(key)), fieldMap.get(key)));
                                } else {
                                    method.invoke(result, object.get(key));
                                }
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取某个Class的（属性名称-Class）键值对
     */
    private static final Map<String, Class> mapFromClass(Class<? extends BaseModel> mClass) {
        Map<String, Class> result = new HashMap<>();
        if (null == mClass) {
            return result;
        }
        Class curClass = mClass;
        while (curClass != Object.class) {
            Field[] fields = curClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                result.put(fields[i].getName(), fields[i].getType());
            }
            if (curClass == BaseModel.class) {
                break;
            }
            curClass = curClass.getSuperclass();
        }
        return result;
    }

    private static final boolean isSubclassOfBaseModel(Class mClass) {
        if (mClass.isPrimitive() || !BaseModel.class.isAssignableFrom(mClass)) {
            return false;
        }
        return true;
    }

    private static final boolean isSubclassOfList(Class mClass) {
        if (!mClass.isPrimitive() && List.class.isAssignableFrom(mClass)) {
            return true;
        }
        return false;
    }

}
