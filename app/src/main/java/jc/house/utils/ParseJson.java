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
     * json对象数组转对应的model对象List(json数组必须是同类)
     *
     * @param array
     * @param clazz model的class对象
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

    private static final BaseModel jsonObj2Model(JSONObject object, Class<? extends BaseModel> mClass, Map<String, Class> fieldMap) {
        BaseModel result = null;
        if (null == object || null == mClass || null == fieldMap) {
            return result;
        }
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

    public static final BaseModel jsonObj2Model(JSONObject object, Class<? extends BaseModel> mClass) {
        if (null == object || null == mClass) {
            return null;
        }
        Map<String, Class> fieldMap = mapFromClass(mClass);
        return jsonObj2Model(object, mClass, fieldMap);
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
