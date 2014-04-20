package com.googlecode.easyec.sika.ss.mappings;

import com.googlecode.easyec.sika.Grabber;
import com.googlecode.easyec.sika.WorkbookHeader;
import com.googlecode.easyec.sika.mappings.AnnotationWorkbookRowCallback;
import com.googlecode.easyec.sika.ss.ExcelCtrl;
import com.googlecode.easyec.sika.WorkPage;
import org.springframework.util.Assert;

import static com.googlecode.easyec.sika.WorkPage.DEFAULT;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public class AnnotationExcelRowCallback<T> extends AnnotationWorkbookRowCallback<T> implements ExcelCtrl {

    private WorkPage workPage = DEFAULT;

    protected AnnotationExcelRowCallback(WorkPage workPage, Grabber<T> grabber) {
        this(new WorkbookHeader(1), workPage, grabber);
    }

    protected AnnotationExcelRowCallback(WorkbookHeader header, WorkPage workPage, Grabber<T> grabber) {
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
}
