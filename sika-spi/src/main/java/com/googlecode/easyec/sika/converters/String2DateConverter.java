package com.googlecode.easyec.sika.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * 字符串转日期类型的转换器类。
 *
 * @author JunJie
 */
public class String2DateConverter implements ColumnConverter<Date> {

    private static final Logger logger = LoggerFactory.getLogger(String2DateConverter.class);

    private String pattern;
    private Locale locale;

    public String2DateConverter(String pattern) {
        this(pattern, Locale.getDefault());
    }

    public String2DateConverter(String pattern, Locale locale) {
        Assert.notNull(pattern, "Date pattern cannot be null.");
        Assert.notNull(locale, "Locale cannot be null.");

        this.pattern = pattern;
        this.locale = locale;
    }

    public Date convert(Object val) {
        String v = new Object2StringConverter().convert(val);

        if (isNotBlank(v)) {
            DateFormat df = new SimpleDateFormat(pattern, locale);
            try {
                return df.parse((String) val);
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return null;
    }
}
