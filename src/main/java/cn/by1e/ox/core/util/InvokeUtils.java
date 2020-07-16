package cn.by1e.ox.core.util;

import cn.by1e.ox.core.internal.Invoker;
import cn.by1e.ox.core.internal.VoidInvoker;

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
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
