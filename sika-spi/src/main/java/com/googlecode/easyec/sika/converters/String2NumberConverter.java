package com.googlecode.easyec.sika.converters;

/**
 * 字符串转成数字的列数据转换器类
 *
 * @author JunJie
 */
public class String2NumberConverter implements ColumnConverter<Double> {

    public Double adorn(Object val) {
        if (val == null) return null;
        if (val instanceof Number) {
            if (val instanceof Double) {
                return (Double) val;
            }

            return ((Number) val).doubleValue();
        }

        if (!(val instanceof String)) return null;

        try {
            return Double.valueOf(
                ((String) val).replaceAll(",", "")
            );
        } catch (Exception e) {
            return null;
        }
    }

    public Object conceal(Double original) {
        return original;
    }
}
