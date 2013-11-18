package com.googlecode.easyec.sika.converters;

/**
 * 对象转字符串类型的转换类。
 * 该类支持数字类型转字符串。
 * 不支持日期类型转字符串。
 *
 * @author JunJie
 */
public class Object2StringConverter implements ColumnConverter<String> {

    public String adorn(Object val) {
        if (val == null) return null;
        if (val instanceof String) return ((String) val);
        if (val instanceof Number) return new Number2StringConverter().adorn(val);
        return val.toString();
    }

    public Object conceal(String original) {
        return original;
    }
}
