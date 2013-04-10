package com.googlecode.easyec.sika;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

import static com.googlecode.easyec.sika.WorkPage.DEFAULT;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-5
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractWorkbookCallback<T> implements WorkbookCallback<T> {

    /**
     * SL4J日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private WorkbookHeader header;
    private Grabber<T> grabber;
    private DocType docType;
    private WorkPage workPage = DEFAULT;

    protected AbstractWorkbookCallback(WorkbookHeader header, Grabber<T> grabber) {
        this(header, null, grabber);
    }

    protected AbstractWorkbookCallback(WorkPage workPage, Grabber<T> grabber) {
        this(new WorkbookHeader(1), workPage, grabber);
    }

    protected AbstractWorkbookCallback(WorkbookHeader header, WorkPage workPage, Grabber<T> grabber) {
        Assert.notNull(header, "WorkbookHeader object cannot be null.");
        Assert.notNull(grabber, "Grabber is null.");

        this.header = header;
        this.grabber = grabber;
        if (workPage != null) {
            this.workPage = workPage;
        }
    }

    /**
     * 返回当前的<code>Grabber</code>对象
     *
     * @return {@link Grabber}
     */
    protected Grabber<T> getGrabber() {
        return grabber;
    }

    public List<T> doGrab() throws WorkingException {
        return grabber.grab();
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
}
