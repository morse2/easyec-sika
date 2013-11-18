package com.googlecode.easyec.sika.event;

import java.util.EventListener;

/**
 * 工作本处理器之间切换的事件监听类
 *
 * @author JunJie
 */
public interface WorkbookHandlerChangeListener extends EventListener {

    /**
     * 默认工作本表单切换监听器类
     */
    WorkbookHandlerChangeListener DEFAULT = new DefaultWorkbookHandlerChangeListener();

    /**
     * 工作本切换时触发校验的方法。
     * 如果此方法返回真，表示程序继续处理后续的工作本信息。
     * 哦负责略过余下的工作本信息的处理工作。
     *
     * @param event 工作本处理对象信息
     * @return 真或假
     */
    boolean accept(WorkbookHandleEvent event);
}
