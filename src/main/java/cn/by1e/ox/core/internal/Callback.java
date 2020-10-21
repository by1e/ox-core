package cn.by1e.ox.core.internal;

/**
 * @author bangquan.qian
 * @date 2020-10-21 15:14
 */
@FunctionalInterface
public interface Callback<T> {

    T execute();

}
