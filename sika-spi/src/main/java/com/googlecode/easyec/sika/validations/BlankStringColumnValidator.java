package com.googlecode.easyec.sika.validations;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 空字串列数据验证器类。
 * <p>
 * 该类可以检查列数据是否为空指针，并且进一步判断是否是字符串。
 * </p>
 *
 * @author JunJie
 */
public class BlankStringColumnValidator implements ColumnValidator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public boolean accept(Object val) {
        if (val != null) {
            if (val instanceof String) {
                return StringUtils.isNotBlank((String) val);
            }

            if (logger.isWarnEnabled()) {
                logger.warn("Value: [" + val + "] isn't empty, but it also isn't a string value.");
            }
        }

        return false;
    }

    public String getAlias() {
        return "BLANK_STRING_COLUMN";
    }
}
