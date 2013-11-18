package com.googlecode.easyec.sika.converters;

/**
 * 列内容转换器类。
 * <p>
 * 实现该类，可以将工作本中获得的值转换为自定义的值类型。
 * </p>
 *
 * @author JunJie
 */
public interface ColumnConverter<T> {

    /**
     * 通过给定的值类型，转换为自定义的值类型。
     *
     * @param val 从工作本中得到的数据
     * @return 返回需要的值类型
     */
    T adorn(Object val);

    /**
     * 将Bean对象中的指定字段的值
     * 转换成工作本可以接受的值类型。
     *
     * @param original 原始值类型
     * @return 工作本值类型
     */
    Object conceal(T original);
}
