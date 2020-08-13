package cn.by1e.ox.core.util;

import cn.by1e.ox.core.internal.Invoker;
import cn.by1e.ox.core.internal.VoidInvoker;

import java.util.function.*;

/**
 * @author bangquan.qian
 * @date 2020-07-16 16:18
 */
public class InvokeUtils {

    public static void voidInvoke(VoidInvoker invoker) throws Throwable {
        invoker.invoke();
    }

    public static void voidInvokeRe(VoidInvoker invoker) {
        try {
            voidInvoke(invoker);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invoke(Invoker<T> invoker) throws Throwable {
        return invoker.invoke();
    }

    public static <T> T invokeRe(Invoker<T> invoker) {
        try {
            return invoke(invoker);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T unaryInvoke2(T t, Consumer<T> consumer) {
        consumer.accept(t);
        return t;
    }

    public static <T> T unaryInvoke(T t, UnaryOperator<T> operator) {
        return operator.apply(t);
    }

    public static <R, T> R function(T t, Function<T, R> function) {
        return function.apply(t);
    }

    public static <T> void consume(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }

    public static <T> T supply(Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T> boolean predicate(T t, Predicate<T> predicate) {
        return predicate.test(t);
    }
}
