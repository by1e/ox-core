package cn.by1e.ox.core.internal;

/**
 * @author bangquan.qian
 * @date 2020-07-16 16:22
 */
@FunctionalInterface
public interface Invoker<T> {

    T invoke() throws Throwable;

}
