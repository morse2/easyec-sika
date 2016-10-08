package com.googlecode.easyec.sika.validations;

import com.googlecode.easyec.sika.Constants;

/**
 * 空指针验证器类。
 * <p>
 * 使用该类，可以帮助判断从工作本中读取的一列值内容是否为空指针。
 * </p>
 *
 * @author JunJie
 */
public class BlankColumnValidator extends AbstractColumnValidator {

    protected boolean doAccept(Object val) {
        return null != val;
    }

    public String getAlias() {
        return Constants.ALIAS_BLANK_COLUMN;
    }
}
