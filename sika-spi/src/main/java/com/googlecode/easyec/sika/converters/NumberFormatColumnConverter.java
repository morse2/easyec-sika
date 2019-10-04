package com.googlecode.easyec.sika.converters;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NumberFormatColumnConverter implements ColumnConverter<Number, String> {

    private String format;

    public NumberFormatColumnConverter() {
        this("#,##0.##");
    }

    public NumberFormatColumnConverter(String format) {
        if (StringUtils.isNotBlank(format)) {
            this.format = format;
        }
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String adorn(Number val) {
        return val != null
            ? new DecimalFormat(getFormat()).format(val)
            : EMPTY;
    }
}
