package tv.kuainiu.util;

import android.content.ContentValues;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ClassRefUtil {

    public ClassRefUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 取Bean的属性和值对应关系的MAP
     *
     * @param bean
     * @return Map
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Map<String, String> getFieldValueMap(Object bean)
            throws NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Class<?> cls = bean.getClass();
        Map<String, String> valueMap = new HashMap<String, String>();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {

            String fieldType = field.getType().getSimpleName();
            String fieldGetName = parGetName(field.getName());
            if (!checkGetMet(methods, fieldGetName)) {
                continue;
            }
            Method fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
            Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});
            String result = null;
            if ("Date".equals(fieldType)) {
                result = fmtDate((Date) fieldVal);
            } else {
                if (null != fieldVal) {
                    result = String.valueOf(fieldVal);
                }
            }
            valueMap.put(field.getName(), result);

        }
        return valueMap;

    }

    /**
     * set属性的值到Bean
     *
     * @param bean
     * @param valMap
     */
    public static void setFieldValue(Object bean, Map<String, String> valMap) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            try {
                String fieldSetName = parSetName(field.getName());
                if (!checkSetMet(methods, fieldSetName)) {
                    continue;
                }
                Method fieldSetMet = cls.getMethod(fieldSetName,
                        field.getType());
                String value = valMap.get(field.getName());
                if (null != value && !"".equals(value)) {
                    String fieldType = field.getType().getSimpleName();
                    if ("String".equals(fieldType)) {
                        fieldSetMet.invoke(bean, value);
                    } else if ("Date".equals(fieldType)) {
                        Date temp = parseDate(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Integer".equals(fieldType)
                            || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value);
                        fieldSetMet.invoke(bean, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value);
                        fieldSetMet.invoke(bean, temp);
                    } else if ("Float".equalsIgnoreCase(fieldType)) {
                        Float temp = Float.parseFloat(value);
                        fieldSetMet.invoke(bean, temp);
                    } else {
                        LogUtils.e("ClassRefUtil", "not supper type"
                                + fieldType);
                    }
                }
            } catch (Exception e) {
                LogUtils.printStackTrace(e);
                continue;
            }
        }

    }

    /**
     * 将对象的属性值保存到ContentValues当中
     *
     * @param values
     * @param bean
     */
    public static void setContentValues(ContentValues values, Object bean) {
        Class<?> cls = bean.getClass();
        // 取出bean里的所有方法
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {

            String fieldType = field.getType().getSimpleName();
            String fieldGetName = ClassRefUtil.parGetName(field.getName());
            if (!ClassRefUtil.checkGetMet(methods, fieldGetName)) {
                continue;
            }
            Method fieldGetMet = null;
            try {
                fieldGetMet = cls.getMethod(fieldGetName, new Class[]{});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Object fieldVal = null;
            try {
                fieldVal = fieldGetMet.invoke(bean, new Object[]{});
            } catch (IllegalArgumentException e) {
                LogUtils.printStackTrace(e);
            } catch (IllegalAccessException e) {
                LogUtils.printStackTrace(e);
            } catch (InvocationTargetException e) {
                LogUtils.printStackTrace(e);
            }
            String result = null;
            if ("Date".equals(fieldType)) {
                result = ClassRefUtil.fmtDate((Date) fieldVal);
            } else {
                if (null != fieldVal) {
                    result = String.valueOf(fieldVal);
                }
            }
            values.put(field.getName(), result);

        }
    }

    /**
     * 根据类生成创建表的sql语句，字段属性目前只支持2种，int 和text
     *
     * @param cls
     * @param needSuperclass 是否需要父类的字段
     * @return
     */
    public static String generateCreateTableSql(Class<?> cls, boolean needSuperclass) {
        StringBuffer strbuff = new StringBuffer();
        List<Field> listFields = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            listFields.add(field);
        }

        if (needSuperclass && cls.getSuperclass() != null) {
            // 获取父类属性
            Field[] superFields = cls.getSuperclass().getDeclaredFields();
            for (Field field : superFields) {
                listFields.add(field);
            }
        }
        if (listFields.size() > 0) {
            strbuff.append("CREATE TABLE ").append(cls.getSimpleName())
                    .append(" ( ");

            for (int i = 0; i < listFields.size(); i++) {
                Field field = listFields.get(i);
                String type = field.getType().getSimpleName();

                // 过滤Androi Studio2.0 Run 特性注入的字段
                if (field.getName().endsWith("change")) {
                    continue;
                }

                strbuff.append(field.getName());

                DebugUtils.dd("type : " + field.getType());
                DebugUtils.dd("name : " + field.getName());


                if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer")) {
                    strbuff.append(" INT");
                } else if (type.equalsIgnoreCase("double")) {
                    strbuff.append(" DOUBLE");
                } else if (type.equalsIgnoreCase("date")) {
                    strbuff.append(" DATE");
                } else if (type.equalsIgnoreCase("float")) {
                    strbuff.append(" FLOAT");
                } else if (type.equalsIgnoreCase("boolean")) {
                    strbuff.append(" BOOLEAN");
                } else if (type.equalsIgnoreCase("long")) {
                    strbuff.append(" BIGINT");
                } else if (type.equalsIgnoreCase("byte")) {
                    strbuff.append(" TINYINT");
                } else if (type.equalsIgnoreCase("short")) {
                    strbuff.append(" SMALLINT");
                } else if (type.equalsIgnoreCase("decimal")) {
                    strbuff.append(" DECIMAL");
                } else if (type.equalsIgnoreCase("number")) {
                    strbuff.append(" NUMBER");
                } else {
                    strbuff.append(" TEXT");
                }
                strbuff.append(",");
            }
            strbuff.deleteCharAt(strbuff.length() - 1);
            strbuff.append(" ); ");
        }

        return strbuff.toString();
    }

    /**
     * 格式化string为Date
     *
     * @param datestr
     * @return date
     */
    public static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                fmtstr = "yyyy-MM-dd HH:mm:ss";
            } else {
                fmtstr = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.CHINA);
            return sdf.parse(datestr);
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
            return null;
        }
    }

    /**
     * 日期转化为String
     *
     * @param date
     * @return date string
     */
    public static String fmtDate(Date date) {
        if (null == date) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.CHINA);
            return sdf.format(date);
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
            return null;
        }
    }

    /**
     * 判断是否存在某属性的 set方法
     *
     * @param methods
     * @param fieldSetMet
     * @return boolean
     */
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
        for (Method met : methods) {
            if (fieldSetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否存在某属性的 get方法
     *
     * @param methods
     * @param fieldGetMet
     * @return boolean
     */
    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
        for (Method met : methods) {
            if (fieldGetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接某属性的 get方法
     *
     * @param fieldName
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "get" + fieldName.substring(0, 1).toUpperCase(Locale.getDefault())
                + fieldName.substring(1);
    }

    /**
     * 拼接在某属性的 set方法
     *
     * @param fieldName
     * @return String
     */
    public static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase(Locale.getDefault())
                + fieldName.substring(1);
    }
}
