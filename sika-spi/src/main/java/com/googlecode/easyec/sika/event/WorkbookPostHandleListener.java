package com.googlecode.easyec.sika.event;

import java.util.EventListener;

/**
 * 工作本处理结束的后置监听类
 *
 * @author JunJie
 */
public interface WorkbookPostHandleListener extends EventListener {

    /**
     * 后置处理方法
     *
     * @param event 工作本处理事件对象
     */
    void postProcess(WorkbookHandleEvent event);
}
