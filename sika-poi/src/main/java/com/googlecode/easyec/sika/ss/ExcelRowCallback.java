package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.Grabber;
import com.googlecode.easyec.sika.WorkPage;
import com.googlecode.easyec.sika.WorkbookHeader;
import com.googlecode.easyec.sika.WorkbookRowCallback;
import com.googlecode.easyec.sika.ss.event.ExcelSheetEventCtrl;
import com.googlecode.easyec.sika.ss.event.InitializingSheetListener;
import org.springframework.util.Assert;

import static com.googlecode.easyec.sika.WorkPage.DEFAULT;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public abstract class ExcelRowCallback<T> extends WorkbookRowCallback<T> implements ExcelCtrl, ExcelSheetEventCtrl {

    private WorkPage workPage = DEFAULT;
    private InitializingSheetListener initializingSheetListener;

    protected ExcelRowCallback(WorkPage workPage, Grabber<T> grabber) {
        this(new WorkbookHeader(1), workPage, grabber);
    }

    protected ExcelRowCallback(WorkbookHeader header, WorkPage workPage, Grabber<T> grabber) {
        super(header, grabber);

        if (null != workPage) this.workPage = workPage;
    }

    public WorkPage getWorkPage() {
        return workPage;
    }

    public void setWorkPage(WorkPage workPage) {
        Assert.notNull(workPage, "WorkPage object is null.");
        this.workPage = workPage;
    }

    public InitializingSheetListener getInitializingSheetListener() {
        return initializingSheetListener;
    }

    public void setInitializingSheetListener(InitializingSheetListener initializingSheetListener) {
        this.initializingSheetListener = initializingSheetListener;
    }
}
