package com.googlecode.easyec.sika.ss.converters;

import com.googlecode.easyec.sika.converters.DateColumnConverter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Excel工作表单元格日期类型的转换类
 *
 * @author JunJie
 */
public class ExcelDateColumnConverter extends DateColumnConverter {

    private static final Logger logger = LoggerFactory.getLogger(ExcelDateColumnConverter.class);

    @Override
    public Date adorn(Object val) {
        if (val instanceof Double) {
            try {
                return DateUtil.getJavaDate((Double) val);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                return null;
            }
        }

        return super.adorn(val);
    }
}
