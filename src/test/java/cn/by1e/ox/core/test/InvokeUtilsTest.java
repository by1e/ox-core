package cn.by1e.ox.core.test;

import cn.by1e.ox.core.internal.Invoker;
import cn.by1e.ox.core.util.ConsoleUtils;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bangquan.qian
 * @date 2020-10-19 20:28
 */
public class InvokeUtilsTest {

    public static void main(String[] args) throws Throwable {
        //test();

        test2();
    }

    private static void test2() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 4, 1, 2, 3);
        List<Integer> list1 = toDistinctList(Function.identity(), list);
        ConsoleUtils.json(list1);
    }
    public static <T, R> List<R> toDistinctList(Function<T, R> function, Collection<T> col) {
        return Objects.isNull(col) ? Lists.newArrayList() : col.stream().map(function).distinct().collect(Collectors.toList());
    }


    public static void test() throws Throwable {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Invoker invoker = prepare(atomicInteger);
        Object result = invoker.invoke();
        atomicInteger.incrementAndGet();
        ConsoleUtils.sout(result);
    }

    private static Invoker prepare(AtomicInteger atomicInteger) {
        //atomicInteger = new AtomicInteger();
        return () -> {
            //atomicInteger = new AtomicInteger();
            atomicInteger.incrementAndGet();
            return atomicInteger;
        };
    }
}
