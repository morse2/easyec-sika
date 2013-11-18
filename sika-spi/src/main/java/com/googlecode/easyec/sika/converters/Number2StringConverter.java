package com.googlecode.easyec.sika.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * 数字到字符串类型的列数据转换器类。
 * <p>
 * 该类可以将数字类型的列数据转换成标准的字符串类型。转换规则如下：<br/>
 * <ol>
 * <li>
 * 如果是指数数字类型：1.23E5，则转换后的字符串类型为：123000
 * </li>
 * <li>
 * 如果是小数数字类型：<br/>
 * <ul>
 * <li>
 * 小数都为0，则自动忽略这些小数位。例：123.0，转换后：123
 * </li>
 * <li>
 * 小数不都为0，则直接转换这些小数位。例：123.45，转换后：123.45
 * </li>
 * </ul>
 * </li>
 * <li>如果是空指针或不是数字类型，则不做任何转换操作。</li>
 * </ol>
 * </p>
 *
 * @author JunJie
 */
public class Number2StringConverter implements ColumnConverter<String> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public String adorn(Object val) {
        if (val == null) {
            logger.debug("value is null.");

            return null;
        }

        if (!Number.class.isAssignableFrom(val.getClass())) {
            logger.warn("value isn't a number object, so ignore to adorn.");

            return String.valueOf(val);
        }

        BigDecimal bd = new BigDecimal(val.toString());
        if (bd.compareTo(new BigDecimal(bd.longValue())) == 0) {
            return String.valueOf(bd.longValue());
        }

        return String.valueOf(bd.doubleValue());
    }

    public Object conceal(String original) {
        if (isNotBlank(original)) {
            try {
                return Double.valueOf(original);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return null;
    }
}
