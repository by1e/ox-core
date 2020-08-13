package cn.by1e.ox.core.test;

import cn.by1e.ox.core.util.ChainedExecutors;
import cn.by1e.ox.core.util.ConsoleUtils;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bangquan.qian
 * @date 2020-08-13 18:02
 */
public class ChainedExecutorsTest {

    @Test
    public void test() {
        AtomicInteger num = new AtomicInteger(0);

        ChainedExecutors.newExecutor().ahead(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("ahead");
        }).then(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("then1");
        }).then(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("then2");
        }).asyncThen(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("asyncThen1");
        }).then(() -> {
            int i = 1 / 0;
            num.incrementAndGet();
            ConsoleUtils.sout("then3");
        }).asyncThen(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("asyncThen2");
        }).onCatch(e -> {
            num.incrementAndGet();
            ConsoleUtils.sout("onCatch");
        }).onFinal(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("onFinal");
        }).execute();

        ConsoleUtils.sout(num);
    }

}
