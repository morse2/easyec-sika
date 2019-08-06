package com.googlecode.easyec.sika.converters;

/**
 * 列内容转换器类。
 * <p>
 * 实现该类，可以将工作本中获得的值转换为自定义的值类型。
 * </p>
 *
 * @author JunJie
 */
public interface ColumnConverter<IN, OUT> {

    /**
     * 将给定的值转换成用户程序或工作本可识别
     * 的对象类型。
     *
     * @param val 需要转换的值
     * @return 转换后的值
     */
    OUT adorn(IN val);
}
