package cn.by1e.ox.core.util.urlparams;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author bangquan.qian
 * @Date 2019-03-06 12:43
 */
public class UrlParams {

    // protocol://host:port/path?query#fragment

    @Getter
    @Setter
    private String protocol;

    @Getter
    @Setter
    private String host;

    @Getter
    @Setter
    private String port;

    @Getter
    @Setter
    private String path;

    @Getter
    @Setter
    private Map<String, String> query;

    @Getter
    @Setter
    private String fragment;

    private UrlParams() {
    }

    private UrlParams(URL url) {
        if (url == null) {
            throw new RuntimeException("url is null");
        }

        this.protocol = url.getProtocol();
        this.host = url.getHost();
        this.port = parsePort(url.getPort());
        this.path = url.getPath();
        this.query = parseQuery(url.getQuery());
    }

    private String parsePort(int port) {
        if (port < 0) {
            return StringUtils.EMPTY;
        }
        return String.valueOf(port);
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(query)) {
            return map;
        }

        String[] entrys = query.split("&");
        if (ArrayUtils.isEmpty(entrys)) {
            return map;
        }

        int idx = 0;
        for (; idx < entrys.length; idx++) {
            String entry = entrys[idx];
            if (StringUtils.isBlank(entry)) {
                break;
            }
            String[] kvs = entry.split("=");
            if (ArrayUtils.isEmpty(kvs) || kvs.length > 2) {
                break;
            }
            String key = kvs[0].trim();
            String val = kvs.length > 1 ? kvs[1].trim() : StringUtils.EMPTY;
            map.put(key, val);
        }

        if (idx < entrys.length) {
            throw new RuntimeException("url query part is invalid, query: " + query);
        }

        return map;
    }

    public String toUrlString() {
        return toNotNullString(this.getProtocol()) + "://"
                + toNotNullString(this.getHost())
                + toNotNullString(StringUtils.isNotBlank(this.getPort()) ? ":" + this.getPort() : this.getPort())
                + toNotNullString(StringUtils.isNotBlank(this.getPath()) ? (this.getPath().startsWith("/") ? this.getPath() : "/" + this.getPath()) : this.getPath())
                + toNotNullString(StringUtils.isNotBlank(this.getFragment()) ? "#" + this.getFragment() : this.getFragment())
                + toNotNullString(toUrlParam(this.getQuery()));
    }

    private String toNotNullString(Object obj) {
        return obj == null ? StringUtils.EMPTY : String.valueOf(obj);
    }

    private static String toUrlParam(Map<String, String> query) {
        if (MapUtils.isEmpty(query)) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String urlParam = sb.toString();
        return StringUtils.isNotBlank(urlParam) ? "?" + urlParam : urlParam;
    }

    public static UrlParams of(URL url) {
        return new UrlParams(url);
    }

    public static UrlParams of(String url) {
        try {
            return new UrlParams(new URL(url));
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
