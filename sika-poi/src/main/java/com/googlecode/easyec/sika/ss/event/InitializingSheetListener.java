package com.googlecode.easyec.sika.ss.event;

import java.util.EventListener;

/**
 * 初始化Excel工作页的事件监听类
 *
 * @author JunJie
 */
public interface InitializingSheetListener extends EventListener {

    /**
     * 初始化Excel的工作页的方法
     *
     * @param event 初始化工作页事件对象
     */
    void doInit(InitializingSheetEvent event);
}
