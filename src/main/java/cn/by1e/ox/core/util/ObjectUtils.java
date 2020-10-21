package cn.by1e.ox.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author bangquan.qian
 * @date 2020-09-19 17:36
 */
public class ObjectUtils {

    public static String emptyIfNull(String str) {
        return str == null ? StringUtils.EMPTY : str;
    }

    public static <T> List<T> emptyIfNull(List<T> lst) {
        return lst == null ? Collections.emptyList() : lst;
    }

    public static <K, V> Map<K, V> emptyIfNull(Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    public static <T> ArrayList<T> newIfNull(ArrayList<T> lst) {
        return lst == null ? Lists.newArrayList() : lst;
    }

    public static <T> HashSet<T> newIfNull(HashSet<T> set) {
        return set == null ? Sets.newHashSet() : set;
    }

    public static <K, V> HashMap<K, V> newIfNull(HashMap<K, V> map) {
        return map == null ? Maps.newHashMap() : map;
    }

    public static <T> T getListFirst(List<T> lst) {
        return CollectionUtils.isEmpty(lst) ? null : lst.get(0);
    }

    public static <T> T getOnlyOne(List<T> lst) {
        if (CollectionUtils.isEmpty(lst)) {
            return null;
        }
        if (lst.size() > 1) {
            throw new RuntimeException("expect only one, but find more");
        }
        return lst.get(0);
    }

    public static String recode(String str, String srcCharset, String dstCharset) throws UnsupportedEncodingException {
        return new String(str.getBytes(srcCharset), dstCharset);
    }

    public static String toNotNullString(Object obj) {
        return obj == null ? StringUtils.EMPTY : String.valueOf(obj);
    }

    public static String toNullableString(Object obj) {
        return obj == null ? null : String.valueOf(obj);
    }

    public static List<String> str2List(String delimiter, String str) {
        return StringUtils.isBlank(str) ? Lists.newArrayList() : Arrays.asList(str.split(delimiter));
    }

}
