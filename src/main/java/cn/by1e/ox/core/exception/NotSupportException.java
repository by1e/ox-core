package cn.by1e.ox.core.exception;

/**
 * @author bangquan.qian
 * @date 2020-07-16 12:51
 */
public class NotSupportException extends RuntimeException {
    private static final long serialVersionUID = 5698204238135153004L;

    public NotSupportException() {
    }

    public NotSupportException(String message) {
        super(message);
    }

    public NotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportException(Throwable cause) {
        super(cause);
    }
}
