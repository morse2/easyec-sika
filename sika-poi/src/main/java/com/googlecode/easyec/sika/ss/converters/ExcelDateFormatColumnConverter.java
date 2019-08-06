package com.googlecode.easyec.sika.ss.converters;

import com.googlecode.easyec.sika.converters.ColumnConverter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Excel工作表单元格日期类型的转换类
 *
 * @author JunJie
 */
public class ExcelDateFormatColumnConverter implements ColumnConverter<Date, Double> {

    private static final Logger logger = LoggerFactory.getLogger(ExcelDateFormatColumnConverter.class);

    @Override
    public Double adorn(Date val) {
        if (val == null) return null;
        double date = DateUtil.getExcelDate(val);
        if (date < 0.1) {
            logger.error("It's not a date, value: [{}].", date);
        }

        return date;
    }
}
