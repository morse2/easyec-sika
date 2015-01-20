package com.googlecode.easyec.sika;

/**
 * DOCUMENT IT
 *
 * @author JunJie.Zhang
 */
public abstract class WorkbookRowCallback<T> extends AbstractWorkbookCallback<T> {

    protected WorkbookRowCallback(Grabber<T> grabber) {
        super(new WorkbookHeader(1), grabber);
    }

    protected WorkbookRowCallback(WorkbookHeader header, Grabber<T> grabber) {
        super(header, grabber);
    }
}
