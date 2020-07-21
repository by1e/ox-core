package cn.by1e.ox.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

/**
 * @author bangquan.qian
 * @date 2020-07-21 18:53
 */
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static String toJsonString(Object obj) {
        return InvokeUtils.invokeRe(() -> mapper.writeValueAsString(obj));
    }

    public static String toPrettyJsonString(Object obj) {
        return InvokeUtils.invokeRe(() -> mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    }

    public static <T> T parseJavaObject(String json, Class<T> clz) {
        return InvokeUtils.invokeRe(() -> mapper.readValue(json, clz));
    }

    public static <T> T parseJavaObject(String json, TypeReference<T> ref) {
        return InvokeUtils.invokeRe(() -> mapper.readValue(json, ref));
    }

    public static <T> List<T> parseJavaArray(String json, Class<T> clz) {
        return parseJavaObject(json, new TypeReference<List<T>>() {
        });
    }

}
