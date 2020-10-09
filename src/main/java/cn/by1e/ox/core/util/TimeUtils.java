package cn.by1e.ox.core.util;

import cn.by1e.ox.core.constant.Constants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author bangquan.qian
 * @date 2020-10-09 17:50
 */
public class TimeUtils {

    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern(Constants.TIME_PATTERN);

    public static String now() {
        return dtf.print(System.currentTimeMillis());
    }

}
