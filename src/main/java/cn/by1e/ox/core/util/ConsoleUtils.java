package cn.by1e.ox.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author bangquan.qian
 * @date 2020-07-16 17:31
 */
public class ConsoleUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static void prettyJsons(Object... objs) {
        prettyJson(objs);
    }

    public static void jsons(Object... objs) {
        json(objs);
    }

    public static void prettyJson(Object objs) {
        sout(InvokeUtils.invokeRe(() -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objs)));
    }

    public static void json(Object objs) {
        sout(InvokeUtils.invokeRe(() -> mapper.writeValueAsString(objs)));
    }

    public static void souts(Object... objs) {
        sout(objs);
    }

    public static void sout(Object obj) {
        System.out.println(obj);
    }

}
