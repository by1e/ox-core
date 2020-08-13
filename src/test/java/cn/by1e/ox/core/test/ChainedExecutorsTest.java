package cn.by1e.ox.core.test;

import cn.by1e.ox.core.util.ChainedExecutors;
import cn.by1e.ox.core.util.ConsoleUtils;
import org.junit.Test;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bangquan.qian
 * @date 2020-08-13 18:02
 */
public class ChainedExecutorsTest {

    @Test
    public void test() throws Throwable {
        AtomicInteger num = new AtomicInteger(0);

        ChainedExecutors.Executor executor = ChainedExecutors.newExecutor();

        executor.ahead(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("ahead");
        }).then(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("then1");
        }).then(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("then2");
        }).asyncThen(() -> {
            int i = 1 / 0;
            num.incrementAndGet();
            ConsoleUtils.sout("asyncThen1");
        }).then(() -> {
            int i = 1 / 0;
            num.incrementAndGet();
            ConsoleUtils.sout("then3");
        }).asyncThen(() -> {
            int i = 1 / 0;
            num.incrementAndGet();
            ConsoleUtils.sout("asyncThen2");
        }).onCatch(e -> {
            num.incrementAndGet();
            ConsoleUtils.sout("onCatch");
        }).onFinal(() -> {
            num.incrementAndGet();
            ConsoleUtils.sout("onFinal");
        });

        num.getAndSet(0);
        ChainedExecutors.Result result1 = executor.execute();
        ConsoleUtils.sout(num);

        num.getAndSet(0);
        Future<ChainedExecutors.Result> future = executor.asyncExecute();
        ChainedExecutors.Result result2 = future.get();
        ConsoleUtils.sout(num);

        num.getAndSet(0);
        boolean result3 = executor.executeFailFast();
        ConsoleUtils.sout(num);

    }

}
