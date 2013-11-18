package com.googlecode.easyec.sika.event;

import java.util.EventObject;

/**
 * 工作本处理的事件对象
 *
 * @author JunJie
 */
public class WorkbookHandleEvent extends EventObject {

    private static final long serialVersionUID = 3559397874359176147L;

    public WorkbookHandleEvent(Object source) {
        super(source);
    }
}
