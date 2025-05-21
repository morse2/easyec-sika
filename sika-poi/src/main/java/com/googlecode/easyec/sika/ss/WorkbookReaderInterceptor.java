package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.WorkingException;

import java.util.List;

/**
 * Workbook读取拦截器接口类
 */
@FunctionalInterface
public interface WorkbookReaderInterceptor {

    void beforeRead(List<String> sheetNames, int numberOfSheets) throws WorkingException;
}
