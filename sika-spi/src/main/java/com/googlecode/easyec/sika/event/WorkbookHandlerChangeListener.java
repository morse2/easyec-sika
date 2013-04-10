package com.googlecode.easyec.sika.event;

import com.googlecode.easyec.sika.WorkPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-1-29
 * Time: 上午8:45
 * To change this template use File | Settings | File Templates.
 */
public interface WorkbookHandlerChangeListener extends EventListener {

    /**
     * 默认工作本表单切换监听器类
     */
    WorkbookHandlerChangeListener DEFAULT = new DefaultWorkbookHandlerChangeListener();

    boolean accept(WorkbookHandleEvent event);
}
