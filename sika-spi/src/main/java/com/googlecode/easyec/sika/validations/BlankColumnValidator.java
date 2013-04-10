package com.googlecode.easyec.sika.validations;

/**
 * 空指针验证器类。
 * <p>
 * 使用该类，可以帮助判断从工作本中读取的一列值内容是否为空指针。
 * </p>
 *
 * @author JunJie
 */
public class BlankColumnValidator implements ColumnValidator {

    public boolean accept(Object val) {
        return val != null;
    }

    public String getAlias() {
        return "BLANK_COLUMN";
    }
}
