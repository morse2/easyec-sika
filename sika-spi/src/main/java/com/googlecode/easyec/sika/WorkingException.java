package com.googlecode.easyec.sika;

/**
 * 工作本工作异常类。
 * <p>
 * 此类描述了操作一个工作本时，当遇到错误，都应当抛出此类或子类。
 * </p>
 *
 * @author JunJie
 * @since 0.1
 */
public class WorkingException extends Exception {

    private static final long serialVersionUID = -7918495853691121673L;
    private String code;
    private Object source;
    private boolean isStop;

    public WorkingException(boolean stop) {
        isStop = stop;
    }

    public WorkingException(String message, boolean stop) {
        super(message);
        isStop = stop;
    }

    public WorkingException(String message, Throwable cause, boolean stop) {
        super(message, cause);
        isStop = stop;
    }

    public WorkingException(Throwable cause, boolean stop) {
        super(cause);
        isStop = stop;
    }

    public final boolean isStop() {
        return isStop;
    }

    public final String getCode() {
        return code;
    }

    public final void setCode(String code) {
        this.code = code;
    }

    public final Object getSource() {
        return source;
    }

    public final void setSource(Object source) {
        this.source = source;
    }
}
