package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.WorkbookReader;

/**
 * Workbook读取拦截器接口类
 */
@FunctionalInterface
public interface WorkbookReaderInterceptor {

    void beforeRead(WorkbookReader reader, int numberOfSheets);
}
