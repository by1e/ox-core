package cn.by1e.ox.core.util;

import java.util.Objects;

/**
 * @author bangquan.qian
 * @date 2020-07-16 16:55
 */
public class AssertUtils {

    public static void notNull(Object obj) {
        if (Objects.isNull(obj)) {
            throw new IllegalArgumentException("obj is null");
        }
    }

    public static void isTrue(boolean res) {
        if (!res) {
            throw new RuntimeException("res not true");
        }
    }

}
