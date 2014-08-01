package com.googlecode.easyec.sika.validations;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * 数字类型列数据验证器类。
 * <p>
 * 该类用于验证列值为空或属于{@link Number}类型对象实例的时候，返回true。
 * </p>
 *
 * @author JunJie
 */
public class NumberColumnValidator implements ColumnValidator {

    public boolean accept(Object val) {
        return val == null || ((val instanceof String) && isBlank((String) val)) || val instanceof Number;
    }

    public String getAlias() {
        return "NOT_NUMBER";
    }
}
