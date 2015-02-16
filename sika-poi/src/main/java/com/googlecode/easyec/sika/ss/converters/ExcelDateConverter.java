package com.googlecode.easyec.sika.ss.converters;

import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.converters.String2DateConverter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Locale;

/**
 * Excel工作表单元格日期类型的转换类
 *
 * @author JunJie
 */
public class ExcelDateConverter implements ColumnConverter<Date> {

    private static final Logger logger = LoggerFactory.getLogger(ExcelDateConverter.class);

    private String pattern;
    private Locale locale;

    public ExcelDateConverter(String pattern) {
        this(pattern, Locale.getDefault());
    }

    public ExcelDateConverter(String pattern, Locale locale) {
        Assert.notNull(pattern, "Date pattern cannot be null.");
        Assert.notNull(locale, "Locale cannot be null.");

        this.pattern = pattern;
        this.locale = locale;
    }

    public Date adorn(Object val) {
        if (val instanceof Double) {
            try {
                return DateUtil.getJavaDate((Double) val);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                return null;
            }
        }

        return new String2DateConverter(pattern, locale).adorn(val);
    }

    public Object conceal(Date original) {
        double date = DateUtil.getExcelDate(original);
        if (date < 0.1) {
            logger.error("It's not a date, value: [{}].", original);
        }

        return date;
    }
}
