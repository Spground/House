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
        Map<String, Class> fieldMap = mapFromClass(clazz);
        for(int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.optJSONObject(i);
            BaseModel modelObj = jsonObjectToBaseModel(jsonObject, clazz, fieldMap);
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

    public static final Set<String> allFieldsInClass(Class mClass) {
        if (null == mClass) {
            return null;
        }
        Class curClass = mClass;
        Set<String> result = new HashSet<>();
        while(curClass != Object.class) {
            Field[] fields = curClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                result.add(fields[i].getName());
            }
            curClass = curClass.getSuperclass();
        }
        return result;
    }

    private static final Map<String, Class> mapFromClass(Class mClass) {
        if (null == mClass) {
            return null;
        }
        Map<String, Class> result = new HashMap<>();
        Class curClass = mClass;
        while (curClass != Object.class) {
            Field[] fields = curClass.getDeclaredFields();
            for(int i = 0; i < fields.length; i++) {
                result.put(fields[i].getName(), fields[i].getType());
            }
            curClass = curClass.getSuperclass();
        }
        return result;
    }

    public static final BaseModel jsonObjectToBaseModel(JSONObject object, Class<? extends BaseModel> mClass, Map<String, Class> fieldMap) {
        if (null == object || null == mClass) {
            return null;
        }
        BaseModel result = null;
        try {
            result = mClass.newInstance();
            Iterator<String> keys = object.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                if (fieldMap.containsKey(key)) {
                    try {
                        Method method = mClass.getMethod(StringUtils.methodNameBaseFieldName(key), fieldMap.get(key));
                        try {
                            if (null != object.get(key)) {
                                if (isSubclassOfBaseModel(fieldMap.get(key))){
                                    //递归赋值
                                    method.invoke(result, jsonObjectToBaseModel((JSONObject)(object.get(key)), fieldMap.get(key)));
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
                } else {
                    //输出没有key值
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static final BaseModel jsonObjectToBaseModel(JSONObject object, Class<? extends BaseModel> mClass) {
        if (null == object || null == mClass) {
            return null;
        }
        Map<String, Class> fieldMap = mapFromClass(mClass);
        return jsonObjectToBaseModel(object, mClass, fieldMap);
    }

    private static final boolean isSubclassOfBaseModel(Class mClass) {
        if (mClass.isPrimitive() || !BaseModel.class.isAssignableFrom(mClass)) {
            return false;
        }
        return true;
    }

}
