package com.googlecode.easyec.sika.validations;

import com.googlecode.easyec.sika.Constants;
import org.apache.commons.lang3.math.NumberUtils;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 数字类型列数据验证器类。
 * <p>
 * 该类用于验证列值为空或属于{@link Number}类型对象实例的时候，返回true。
 * </p>
 *
 * @author JunJie
 */
public class NumberColumnValidator extends AbstractColumnValidator {

    protected boolean doAccept(Object val) {
        if (val == null) return true;
        if (val instanceof String) {
            return isBlank((String) val) || NumberUtils.isNumber(((String) val).trim());
        }

        return val instanceof Number;
    }

    public String getAlias() {
        return Constants.ALIAS_NOT_NUMBER;
    }
}
