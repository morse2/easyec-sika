package com.googlecode.easyec.sika.event;

import java.util.EventListener;

/**
 * 工作本中空行数据的事件监听类
 *
 * @author JunJie
 */
public interface WorkbookBlankRowListener extends EventListener {

    /**
     * 默认空行监听器类
     */
    WorkbookBlankRowListener DEFAULT = new DefaultWorkbookBlankRowListener();

    /**
     * 检查是否真是空行数据。
     * 如果是空行，则应该返回假，
     * 程序则不做后续处理。
     * 如果不是空行，则返回真，
     * 程序则会继续处理余下的行数据。
     *
     * @param event 行数据的事件对象
     * @return 检查判断返回真或假以告知是否继续处理行数据
     */
    boolean accept(RowEvent event);
}
