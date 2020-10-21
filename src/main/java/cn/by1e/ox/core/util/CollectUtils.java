package cn.by1e.ox.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bangquan.qian
 * @date 2020-10-14 20:40
 */
public class CollectUtils {

    public static <T, R> Set<R> toSet(Function<T, R> function, Collection<T> col) {
        return Objects.isNull(col) ? Sets.newHashSet() : col.stream().map(function).collect(Collectors.toSet());
    }

    public static <T, R> List<R> toList(Function<T, R> function, Collection<T> col) {
        return Objects.isNull(col) ? Lists.newArrayList() : col.stream().map(function).collect(Collectors.toList());
    }

    public static <T, R> List<R> toDistinctList(Function<T, R> function, Collection<T> col) {
        return Objects.isNull(col) ? Lists.newArrayList() : col.stream().map(function).distinct().collect(Collectors.toList());
    }

    public static <K, V> Map<K, V> toMap(Function<V, K> function, Collection<V> col) {
        return Objects.isNull(col) ? Maps.newHashMap() : col.stream().collect(Collectors.toMap(function, Function.identity(), (e, n) -> e));
    }

    public static <K, V> Map<K, List<V>> toGroupMap(Function<V, K> function, Collection<V> col) {
        return Objects.isNull(col) ? Maps.newHashMap() : col.stream().collect(Collectors.groupingBy(function));
    }

}
