package com.googlecode.easyec.sika.validations;

/**
 * 列数据内容的验证器类。
 * <p>
 * 实现该类可以为列数据做自定义验证。
 * </p>
 *
 * @author JunJie
 */
public interface ColumnValidator {

    /**
     * 接受方法。通过返回值判断是否应该抛出{@link com.googlecode.easyec.sika.WorkingException}异常。
     *
     * @param val 当前行的一列数据内容
     * @return 返回false则表示需要拒绝接受该数据，表明该数据不合法；返回true则表示接受该数据。
     */
    boolean accept(Object val);

    /**
     * 获得验证器的别名。该别名用于显示验证器的验证的内容。
     *
     * @return 当前验证器的别名
     */
    String getAlias();
}
