package com.edusky.message.api.toolkit;

import java.util.Collection;
import java.util.Map;

/**
 * @author tbc on 2016/8/25 0:56.
 */
public class Objs {
    private Objs() {
        throw new AssertionError("No " + getClass().getCanonicalName() + " instances for you!");
    }

    /**
     * 如果值为Null，返回true;<br>
     * 如果是集合类型,且size为0，返回true;<br>
     * 如果是String类型，且长度为0，返回true;<br>
     * 其它情况返回false;
     *
     * @param obj Object
     * @return boolean
     * @author tbc tianbencai@e-eduspace.com
     * @version 1.0 {2016年6月17日 下午12:52:06}
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;
        if (obj instanceof Map)
            return ((Map) obj).isEmpty();
        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();
        return obj instanceof String && ((String) obj).length() == 0;

    }

    public static boolean isEmptyString(String str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isEmptyCollection(Collection<?> c) {
        return isNull(c) || c.size() == 0;
    }

    public static boolean isEmptyMap(Map<?, ?> c) {
        return isNull(c) || c.size() == 0;
    }

    public static boolean isEmptyArray(Object... objects) {
        return objects.length > 0;
    }

    public static boolean nonEmptyString(String str) {
        return !isEmptyString(str);
    }

    public static boolean nonEmptyCollection(Collection<?> c) {
        return !isEmptyCollection(c);
    }

    public static boolean nonEmptyMap(Map<?, ?> c) {
        return !isEmptyMap(c);
    }

    public static boolean nonEmptyArray(Object... objects) {
        return !isEmptyArray(objects);
    }


    public static boolean nonEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object obj) {
        return !isNull(obj);
    }

}
