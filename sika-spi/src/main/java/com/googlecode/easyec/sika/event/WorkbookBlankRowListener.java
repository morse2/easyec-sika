package com.googlecode.easyec.sika.event;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-1-29
 * Time: 上午11:42
 * To change this template use File | Settings | File Templates.
 */
public interface WorkbookBlankRowListener extends EventListener {

    /**
     * 默认空行监听器类
     */
    WorkbookBlankRowListener DEFAULT = new DefaultWorkbookBlankRowListener();

    boolean accept(RowEvent event);
}
