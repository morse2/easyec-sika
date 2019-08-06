package com.googlecode.easyec.sika.converters;

/**
 * 模型内容转换器类。
 * <p>
 * 实现该类，可以将工作本中获得的值转换为自定义的值类型。
 * </p>
 *
 * @author JunJie
 */
public interface BeanConverter<T> {

    /**
     * 通过给定的值类型，转换为自定义的值类型。
     *
     * @param val 从工作本中得到的数据
     * @return 返回需要的值类型
     */
    T adorn(Object val);
}
