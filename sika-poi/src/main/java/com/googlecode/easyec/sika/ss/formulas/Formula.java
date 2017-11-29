package com.googlecode.easyec.sika.ss.formulas;

public interface Formula {

    /**
     * 编码单元格的公式为
     * Excel可解析的字符串
     *
     * @return Excel公式字符串
     */
    String encode();
}
