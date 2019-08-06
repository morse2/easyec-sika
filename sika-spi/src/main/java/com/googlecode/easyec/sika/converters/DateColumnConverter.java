package com.googlecode.easyec.sika.converters;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DateColumnConverter implements ColumnConverter<Object, Date> {

    private static final Logger logger = LoggerFactory.getLogger(DateColumnConverter.class);

    @Override
    public Date adorn(Object val) {
        if (val == null) {
            logger.warn("Value is null.");

            return null;
        }

        if (val instanceof Date) {
            return ((Date) val);
        }

        if (val instanceof String) {
            try {
                return DateUtils.parseDate(((String) val), getLocale(), getDatePatterns());
            } catch (ParseException e) {
                logger.warn(e.getMessage(), e);

                return null;
            }
        }

        logger.warn("Value cannot cast to 'java.util.Date'. [{}], type: [{}].", val, val.getClass());

        return null;
    }

    protected Locale getLocale() {
        return Locale.getDefault();
    }

    protected String[] getDatePatterns() {
        return new String[] {
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "MM/dd/yyyy",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyyMMddHHmmss",
            "yyyyMMddHHmm",
            "yyyyMMdd"
        };
    }
}
