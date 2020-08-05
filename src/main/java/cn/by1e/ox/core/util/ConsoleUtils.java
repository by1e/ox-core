package cn.by1e.ox.core.util;

/**
 * @author bangquan.qian
 * @date 2020-07-16 17:31
 */
public class ConsoleUtils {

    public static void prettyJsons(Object... objs) {
        prettyJson(objs);
    }

    public static void jsons(Object... objs) {
        json(objs);
    }

    public static void prettyJson(Object objs) {
        sout(JsonUtils.toPrettyJsonString(objs));
    }

    public static void json(Object objs) {
        sout(JsonUtils.toJsonString(objs));
    }

    public static void souts(Object... objs) {
        sout(objs);
    }

    public static void sout(Object obj) {
        System.out.println(obj);
    }

    public static void sout() {
        System.out.println();
    }

}
