package guda.mvcx.core.util;


import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by well on 2017/3/18.
 */
public class EnumUtil {



    public static Object byVal(Object clazz,Object val)  {
        if(clazz == null || val == null){
            return null;
        }
        try {
            Class<?> aClass = Class.forName(String.valueOf(clazz));
            Method method = aClass.getMethod("getVal");
            Object[] enumConstants = aClass.getEnumConstants();
            for (Object obj : enumConstants) {
               if(method.invoke(obj).equals(val)){
                   return obj;
               }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object byName(Object clazz,Object val)  {
        if(clazz == null || val == null){
            return null;
        }
        try {
            Class<?> aClass = Class.forName(String.valueOf(clazz));
            Method method = aClass.getMethod("name");
            Object[] enumConstants = aClass.getEnumConstants();
            for (Object obj : enumConstants) {
                if(method.invoke(obj).equals(val)){
                    return obj;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Object,Object> toMap(Object clazz)  {
        if(clazz == null){
            return null;
        }
        try {
            Class<?> aClass = Class.forName(String.valueOf(clazz));
            Method valMethod = aClass.getMethod("name");
            Method descMethod = aClass.getMethod("getDesc");
            Object[] enumConstants = aClass.getEnumConstants();
            Map<Object,Object> map = new LinkedHashMap<Object, Object>();
            for (Object obj : enumConstants) {
                map.put(valMethod.invoke(obj),descMethod.invoke(obj));
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Object,Object> toMap(Object clazz,Object valMethodName,Object nameMethodName)  {
        if(clazz == null ||valMethodName == null || nameMethodName == null){
            return null;
        }
        try {
            Class<?> aClass = Class.forName(String.valueOf(clazz));
            Method valMethod = aClass.getMethod(String.valueOf(valMethodName));
            Method descMethod = aClass.getMethod(String.valueOf(nameMethodName));
            Object[] enumConstants = aClass.getEnumConstants();
            Map<Object,Object> map = new LinkedHashMap<Object, Object>();
            for (Object obj : enumConstants) {
                map.put(String.valueOf(valMethod.invoke(obj)),String.valueOf(descMethod.invoke(obj)));
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
