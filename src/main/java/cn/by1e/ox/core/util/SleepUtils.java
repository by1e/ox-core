package cn.by1e.ox.core.util;

import java.util.concurrent.TimeUnit;

/**
 * @author bangquan.qian
 * @date 2020-07-16 18:03
 */
public class SleepUtils {

    public static void sleep(TimeUnit timeUnit, long timeOut) {
        InvokeUtils.voidInvokeRe(() -> timeUnit.sleep(timeOut));
    }

    public static void sleepSeconds(int seconds) {
        sleep(TimeUnit.SECONDS, seconds);
    }

}
