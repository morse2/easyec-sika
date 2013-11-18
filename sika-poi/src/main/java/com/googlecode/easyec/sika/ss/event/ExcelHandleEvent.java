package com.googlecode.easyec.sika.ss.event;

import com.googlecode.easyec.sika.event.WorkbookHandleEvent;
import com.googlecode.easyec.sika.ss.WorkPage;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public class ExcelHandleEvent extends WorkbookHandleEvent {

    private static final long serialVersionUID = 5413129239302636438L;

    public ExcelHandleEvent(Object source) {
        super(source);
    }

    /**
     * 返回当前正在处理的工作页面对象信息
     *
     * @return <code>WorkPage</code>对象
     */
    public WorkPage getWorkPage() {
        return (WorkPage) source;
    }
}
