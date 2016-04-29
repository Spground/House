package jc.house.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jc.house.models.BaseModel;

/**
 * Created by hzj on 2015/11/1.
 */
public final class ParseJson {

    /**
     * json对象数组转对应的model对象list
     *
     * @param array A JSONArray object
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
     * @param object   JSONObject对象
     * @param mClass   对应的model的Class
     * @param fieldMap mClass对应的（属性值-class）键值对
     * @return mClass对应的对象
     */
    private static final BaseModel jsonObj2Model(JSONObject object, Class<? extends BaseModel> mClass, Map<String, Class> fieldMap) {

        if (null == object || null == mClass || null == fieldMap) {
            throw new NullPointerException("object & mClass & fieldMap should not be null");
        }
        BaseModel result = null;
        try {
            result = mClass.newInstance();
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!fieldMap.containsKey(key)) {
                    continue;
                }
                try {
                    Method method = mClass.getMethod(StringUtils.getMethodNameByFieldName(key), fieldMap.get(key));
                    if (object.isNull(key)) {
                        continue;
                    }
                    try {
                        if (isSubclassOfBaseModel(fieldMap.get(key))) {
                            //递归赋值
                            method.invoke(result, jsonObj2Model((object.optJSONObject(key)), fieldMap.get(key)));
                        } else if (isSubclassOfList(fieldMap.get(key))) {
                            Class mCls = fieldMap.get(key + fieldMap.get(key).getName());
                            if (null != mCls) {
                                method.invoke(result, jsonArray2ModelList(object.optJSONArray(key), mCls));
                            }
                        } else {
                            method.invoke(result, object.opt(key));
                        }
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
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
    public static final Map<String, Class> mapFromClass(Class<? extends BaseModel> mClass) {
        Map<String, Class> result = new HashMap<>();
        if (null == mClass) {
            return result;
        }
        Class curClass = mClass;
        while (curClass != Object.class) {
            Field[] fields = curClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Class<?> fClass = fields[i].getType();
                result.put(fields[i].getName(), fClass);
                if (isSubclassOfList(fClass)) {
                    Type type = fields[i].getGenericType();
                    if (null == type) {
                        continue;
                    }
                    if (type instanceof ParameterizedType) {
                        result.put(fields[i].getName() + fClass.getName(), (Class)(((ParameterizedType)type).getActualTypeArguments()[0]));
                    }
                }
            }
            if (curClass == BaseModel.class) {
                break;
            }
            curClass = curClass.getSuperclass();
        }
        return result;
    }



    public static final boolean isSubclassOfBaseModel(Class mClass) {
        if (!mClass.isPrimitive() && BaseModel.class.isAssignableFrom(mClass)) {
            return true;
        }
        return false;
    }

    public static final boolean isSubclassOfList(Class mClass) {
        if (!mClass.isPrimitive() && List.class.isAssignableFrom(mClass)) {
            return true;
        }
        return false;
    }

}
