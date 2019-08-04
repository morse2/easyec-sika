package com.googlecode.easyec.sika.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 默认的日期转字符串的转换器类。
 *
 * @author JunJie
 */
public class Date2StringConverter implements ColumnConverter<String> {

    private static final Logger logger = LoggerFactory.getLogger(String2DateConverter.class);

    private String pattern;
    private Locale locale;

    public Date2StringConverter(String pattern) {
        this(pattern, Locale.getDefault());
    }

    public Date2StringConverter(String pattern, Locale locale) {
        Assert.notNull(pattern, "Date pattern cannot be null.");
        Assert.notNull(locale, "Locale cannot be null.");

        this.pattern = pattern;
        this.locale = locale;
    }

    public String adorn(Object val) {
        String v = new Object2StringConverter().adorn(val);

        if (isNotBlank(v)) {
            try {
                return new SimpleDateFormat(pattern, locale).format(val);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return null;
    }

    public Object conceal(String original) {
        if (isBlank(original)) return null;

        try {
            return new SimpleDateFormat(pattern, locale).parse(original);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
