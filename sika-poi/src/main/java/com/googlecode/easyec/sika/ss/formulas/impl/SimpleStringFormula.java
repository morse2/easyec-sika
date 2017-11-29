package com.googlecode.easyec.sika.ss.formulas.impl;

import com.googlecode.easyec.sika.ss.formulas.Formula;

/**
 * 简单字符串格式的公式对象类
 *
 * @author junjie
 */
public class SimpleStringFormula implements Formula {

    private String _f;

    public SimpleStringFormula(String f) {
        this._f = f;
    }

    @Override
    public String encode() {
        return _f;
    }
}
