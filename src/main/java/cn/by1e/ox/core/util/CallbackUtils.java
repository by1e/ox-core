package cn.by1e.ox.core.util;

import cn.by1e.ox.core.internal.Callback;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author bangquan.qian
 * @date 2020-10-21 16:20
 */
public class CallbackUtils {

    public static void execute(Callback... callbacks) {
        if (ArrayUtils.isEmpty(callbacks)) {
            return;
        }
        for (Callback callback : callbacks) {
            callback.execute();
        }
    }
}
