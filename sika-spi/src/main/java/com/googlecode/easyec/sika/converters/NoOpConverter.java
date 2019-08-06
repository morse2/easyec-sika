package com.googlecode.easyec.sika.converters;

/**
 * 默认不做任何工作的模型或列数据转换器，框架默认使用。
 *
 * @author JunJie
 */
public class NoOpConverter implements BeanConverter<Object>, ColumnConverter<Object, Object> {

    public Object adorn(Object val) {
        return val;
    }
}
