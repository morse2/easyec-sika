package com.googlecode.easyec.sika;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 工作本抽象的实现类。
 * <p>
 * 该类实现并定义了工作本处理的一般方法。
 * </p>
 *
 * @author JunJie
 */
public abstract class AbstractWorkbookHandler<T> implements WorkbookHandler<T> {

    /**
     * SL4J日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private WorkbookHeader header;
    private DocType        docType;

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
}
