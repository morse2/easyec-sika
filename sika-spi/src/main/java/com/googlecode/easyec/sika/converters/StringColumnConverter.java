package com.googlecode.easyec.sika.converters;

import java.util.Date;

public class StringColumnConverter implements ColumnConverter<Object, String> {

    @Override
    public String adorn(Object val) {
        if (val == null) return null;
        if (val instanceof String) return ((String) val);
        if (val instanceof Number) {
            Number num = new NumberColumnConverter().adorn(val);
            return num != null ? String.valueOf(num) : null;
        }
        if (val instanceof Date) {
            return new DateFormatColumnConverter().adorn((Date) val);
        }

        return val.toString();
    }
}
