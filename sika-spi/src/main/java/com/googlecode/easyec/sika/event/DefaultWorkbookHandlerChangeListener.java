package com.googlecode.easyec.sika.event;

import com.googlecode.easyec.sika.WorkPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的工作本表单切换监听器类。
 */
class DefaultWorkbookHandlerChangeListener implements WorkbookHandlerChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(WorkbookHandlerChangeListener.class);

    public boolean accept(WorkbookHandleEvent event) {
        WorkPage page = event.getWorkPage();

        int i = page.getSheetIndex();
        String name = page.getSheetName();

        logger.debug("Sheet name: [" + name + "], sheet index: [" + i + "].");

        return false;
    }
}
