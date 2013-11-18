package com.googlecode.easyec.sika.event;

/**
 * 默认的工作本表单切换监听器类。
 */
class DefaultWorkbookHandlerChangeListener implements WorkbookHandlerChangeListener {

    public boolean accept(WorkbookHandleEvent event) {
        return false;
    }
}
