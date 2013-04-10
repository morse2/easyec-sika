package com.googlecode.easyec.sika.validations;

import java.util.Date;

/**
 * 日期类型列验证器类。
 * <p>
 * 该类判断列值为空或属于{@link java.util.Date}对象实例的时候，才返回true。
 * </p>
 *
 * @author JunJie
 */
public class DateColumnValidator implements ColumnValidator {

    public boolean accept(Object val) {
        return val == null || val instanceof Date;
    }

    public String getAlias() {
        return "NOT_DATE";
    }
}
