package com.googlecode.easyec.sika.converters;

/**
 * 默认不做任何工作的列数据转换器，框架默认使用。
 *
 * @author JunJie
 */
public class NoOpColumnConverter implements ColumnConverter<Object> {

    public Object convert(Object val) {
        return val;
    }
}
