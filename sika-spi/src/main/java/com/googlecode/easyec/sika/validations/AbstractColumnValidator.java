package com.googlecode.easyec.sika.validations;

/**
 * 抽象的工作本列的验证器类
 *
 * @author JunJie
 */
public abstract class AbstractColumnValidator implements ColumnValidator {

    private Object source;

    /**
     * 获取源对象值
     *
     * @return 原始对象值
     */
    public Object getSource() {
        return source;
    }

    /**
     * 设置源对象值
     *
     * @param source 源对象值
     */
    public void setSource(Object source) {
        this.source = source;
    }

    public final boolean accept(Object val) {
        boolean accept = doAccept(val);
        if (!accept) setSource(val);
        return accept;
    }

    /**
     * 执行验证值的方法
     *
     * @param val 工作本列的值
     * @return 是否接收该值
     */
    abstract protected boolean doAccept(Object val);
}
