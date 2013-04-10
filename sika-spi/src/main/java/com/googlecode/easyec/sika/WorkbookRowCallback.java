package com.googlecode.easyec.sika;

import com.googlecode.easyec.sika.event.WorkbookBlankRowListener;

/**
 * DOCUMENT IT
 *
 * @author JunJie.Zhang
 */
public abstract class WorkbookRowCallback<T> extends AbstractWorkbookCallback<T> {

    private WorkbookBlankRowListener workbookBlankRowListener;

    protected WorkbookRowCallback(Grabber<T> grabber) {
        super(new WorkbookHeader(1), grabber);
    }

    protected WorkbookRowCallback(WorkbookHeader header, Grabber<T> grabber) {
        super(header, grabber);
    }

    protected WorkbookRowCallback(WorkPage workPage, Grabber<T> grabber) {
        super(workPage, grabber);
    }

    protected WorkbookRowCallback(WorkbookHeader header, WorkPage workPage, Grabber<T> grabber) {
        super(header, workPage, grabber);
    }

    /**
     * 设置一个空行数据处理的监听器类
     *
     * @param listener {@link com.googlecode.easyec.sika.event.WorkbookBlankRowListener}
     */
    public void setBlankRowListener(WorkbookBlankRowListener listener) {
        this.workbookBlankRowListener = listener;
    }

    /**
     * 返回此工作本处理类的空行数据处理的监听器类
     *
     * @return {@link WorkbookBlankRowListener}
     */
    public WorkbookBlankRowListener getBlankRowListeners() {
        return workbookBlankRowListener;
    }
}
