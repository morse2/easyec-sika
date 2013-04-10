package com.googlecode.easyec.sika.event;

import com.googlecode.easyec.sika.WorkPage;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-1-29
 * Time: 上午8:46
 * To change this template use File | Settings | File Templates.
 */
public class WorkbookHandleEvent extends EventObject {

    private static final long serialVersionUID = -6653761506739545594L;

    public WorkbookHandleEvent(WorkPage source) {
        super(source);
    }

    @Deprecated
    public int getIndex() {
        return getWorkPage().getSheetIndex();
    }

    public WorkPage getWorkPage() {
        return (WorkPage) source;
    }
}
