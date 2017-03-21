package guda.mvcx.handle;


import com.google.common.base.Predicate;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by well on 2017/3/20.
 */
public class ReflectTool {

    public static Object resolveField(Parameter parameter, Map<String, String> map,String paramName) throws InstantiationException, IllegalAccessException, ParseException {
        Class clazz=parameter.getType();
        if (isSimpleClass(clazz)) {
            if(paramName==null){
                return null;
            }
            String s = map.get(paramName);
            if(s==null){
                return null;
            }
            return resolveSimpleVal(clazz, s);
        } else {
            return resolveCustomField(map, clazz);
        }
    }

    public static boolean isSimpleClass(Class clazz) {
        if (clazz == null) {
            return true;
        }
        if (Integer.class.isAssignableFrom(clazz)
                || int.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz)
                || long.class.isAssignableFrom(clazz)
                || Number.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz)
                || float.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz)
                || double.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                || char.class.isAssignableFrom(clazz)
                || Short.class.isAssignableFrom(clazz)
                || short.class.isAssignableFrom(clazz)
                || String.class.isAssignableFrom(clazz)
                || boolean.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    public static Object resolveCustomField(final Map<String, String> map, Class clazz) throws IllegalAccessException, InstantiationException {
        final Object obj = clazz.newInstance();
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz, new Predicate<Field>() {
            @Override
            public boolean apply(Field field) {
                return true;
            }
        });
        for(Field field:allFields){
            String fieldName = field.getName();
            String val = map.get(fieldName);
            try {
                Object o = resolveSimpleVal(field.getType(), val);
                if (o != null) {
                    field.setAccessible(true);
                    field.set(obj, o);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return obj;
    }

    public static Object resolveSimpleVal(Class paramTypeClass, Object val) throws IllegalAccessException, InstantiationException, ParseException {
        if (val == null || paramTypeClass == null) {
            return null;
        }

        if (String.class.isAssignableFrom(paramTypeClass)) {
            return val.toString();
        } else if (int.class.isAssignableFrom(paramTypeClass)) {
            return new Integer(val.toString()).intValue();
        } else if (Integer.class.isAssignableFrom(paramTypeClass)) {
            return new Integer(val.toString());
        } else if (Long.class.isAssignableFrom(paramTypeClass)) {
            return new Long(val.toString());
        } else if (double.class.isAssignableFrom(paramTypeClass)) {
            return Double.parseDouble(val.toString());
        } else if (Double.class.isAssignableFrom(paramTypeClass)) {
            return new Double(val.toString());
        } else if (float.class.isAssignableFrom(paramTypeClass)) {
            return Float.parseFloat(val.toString());
        } else if (Float.class.isAssignableFrom(paramTypeClass)) {
            return new Float(val.toString());
        } else if (long.class.isAssignableFrom(paramTypeClass)) {
            return Long.parseLong(val.toString());
        } else if (Character.class.isAssignableFrom(paramTypeClass)) {
            if (val.toString().length() != 1) {
                throw new RuntimeException("convert data error type:[" + paramTypeClass + "],with value:[" + val + "]");
            }
            return Character.valueOf(val.toString().charAt(0));
        } else if (char.class.isAssignableFrom(paramTypeClass)) {
            if (val.toString().length() != 1) {
                throw new RuntimeException("convert data error type:[" + paramTypeClass + "],with value:[" + val + "]");
            }
            return Character.valueOf(val.toString().charAt(0)).charValue();
        } else if (Date.class.isAssignableFrom(paramTypeClass)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            return simpleDateFormat.parse(val.toString());
        } else {
            throw new RuntimeException("cannot find convert class type:[" + paramTypeClass + "],with value:[" + val + "]");
        }

    }

//    public static void main(String args[]) throws IllegalAccessException, InstantiationException, ParseException {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userName", "张三");
//        map.put("test", "3");
//        System.out.println(isSimpleClass(UserForm.class));
//        Object obj = resolveField(UserForm.class, map, "2014-12-12 11:32:42");
//        System.out.println(ReflectionToStringBuilder.toString(obj));
//        System.out.println(obj.getClass());
//    }


}
