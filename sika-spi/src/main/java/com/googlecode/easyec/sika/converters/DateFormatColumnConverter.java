package com.googlecode.easyec.sika.converters;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Locale;

public class DateFormatColumnConverter implements ColumnConverter<Date, String> {

    private static final Logger logger = LoggerFactory.getLogger(DateFormatColumnConverter.class);

    @Override
    public String adorn(Date val) {
        if (val == null) {
            logger.debug("Value is null.");

            return null;
        }

        return DateFormatUtils.format(val, getDatePattern(), getLocale());
    }

    protected Locale getLocale() {
        return Locale.getDefault();
    }

    protected String getDatePattern() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}
