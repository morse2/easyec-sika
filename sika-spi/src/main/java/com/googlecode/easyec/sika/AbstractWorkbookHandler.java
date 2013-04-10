package com.googlecode.easyec.sika;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-4-25
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractWorkbookHandler<T> implements WorkbookHandler<T> {

    /**
     * SL4J日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private WorkbookHeader header;
    private WorkPage workPage;
    private DocType docType;

    protected AbstractWorkbookHandler(WorkbookHeader header) {
        Assert.notNull(header, "WorkbookHeader object cannot be null.");
        this.header = header;
    }

    public void doInit() {
        logger.trace(getClass().getName() + ".doInit()");
    }

    public void doFinish() {
        logger.trace(getClass().getName() + ".doFinish()");
    }

    public void doFinally() {
        logger.trace(getClass().getName() + ".doFinally()");
    }

    public void doCatch(WorkingException e) {
        logger.trace(getClass().getName() + ".doCatch(WorkingException)");
    }

    public WorkbookHeader getHeader() {
        return header;
    }

    public DocType getDocType() {
        return docType;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }

    public WorkPage getWorkPage() {
        return workPage;
    }

    public void setWorkPage(WorkPage workPage) {
        Assert.notNull(workPage, "WorkPage object is null.");
        this.workPage = workPage;
    }
}
