package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.WorkPage;
import com.googlecode.easyec.sika.WorkbookHeader;
import com.googlecode.easyec.sika.WorkbookRowHandler;
import org.springframework.util.Assert;

import static com.googlecode.easyec.sika.WorkPage.DEFAULT;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public abstract class ExcelRowHandler extends WorkbookRowHandler implements ExcelCtrl {

    private WorkPage workPage = DEFAULT;

    protected ExcelRowHandler() {
        super(new WorkbookHeader(1));
    }

    protected ExcelRowHandler(WorkbookHeader header) {
        super(header);
    }

    public WorkPage getWorkPage() {
        return workPage;
    }

    public void setWorkPage(WorkPage workPage) {
        Assert.notNull(workPage, "WorkPage object is null.");
        this.workPage = workPage;
    }
}
