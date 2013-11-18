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

    private static final long serialVersionUID = -1344018283364581902L;
    private String  code;
    private Object  source;
    private boolean stop;

    /**
     * 无参数构造方法
     */
    public WorkingException() {
        // no op
    }

    /**
     * 带是否继续处理标识的构造方法
     *
     * @param stop 布尔值，标识是否需要继续处理剩余的记录
     */
    public WorkingException(boolean stop) {
        this.stop = stop;
    }

    /**
     * 构造方法。
     * 带错误消息和是否继续处理标识参数
     *
     * @param message 错误消息
     * @param stop    布尔值，标识是否需要继续处理剩余的记录
     */
    public WorkingException(String message, boolean stop) {
        super(message);
        this.stop = stop;
    }

    /**
     * 构造方法。
     * 带错误消息，异常对象和是否继续处理标识参数
     *
     * @param message 错误消息
     * @param cause   异常对象
     * @param stop    布尔值，标识是否需要继续处理剩余的记录
     */
    public WorkingException(String message, Throwable cause, boolean stop) {
        super(message, cause);
        this.stop = stop;
    }

    /**
     * 构造方法。
     * 带异常对象和是否继续处理标识参数
     *
     * @param cause 异常对象
     * @param stop  布尔值，标识是否需要继续处理剩余的记录
     */
    public WorkingException(Throwable cause, boolean stop) {
        super(cause);
        this.stop = stop;
    }

    /**
     * 返回标识是否继续处理剩余的记录的方法。
     *
     * @return 返回真，表示不处理剩余的记录；返回假，则继续处理记录
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * 返回错误代码。
     *
     * @return 错误代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置错误代码。
     *
     * @param code 错误代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 设置此异常关联的数据来源。
     *
     * @return 数据来源
     */
    public Object getSource() {
        return source;
    }

    /**
     * 设置此异常关联的数据来源。
     *
     * @param source 数据来源
     */
    public void setSource(Object source) {
        this.source = source;
    }

    /**
     * 此方法标识此异常被抛出后，
     * 后续操作应该被终止
     */
    public void setStop() {
        this.stop = true;
    }

    /**
     * 调用此方法，标识后续操作会继续执行
     */
    public void setContinue() {
        this.stop = false;
    }
}
