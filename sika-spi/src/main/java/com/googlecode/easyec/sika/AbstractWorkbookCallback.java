package com.googlecode.easyec.sika;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public abstract class AbstractWorkbookCallback<T> implements WorkbookCallback<T> {

    /**
     * SL4J日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private WorkbookHeader header;
    private Grabber<T>     grabber;
    private DocType        docType;

    protected AbstractWorkbookCallback(WorkbookHeader header, Grabber<T> grabber) {
        Assert.notNull(header, "WorkbookHeader object cannot be null.");
        Assert.notNull(grabber, "Grabber is null.");

        this.header = header;
        this.grabber = grabber;
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
}
