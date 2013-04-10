package com.googlecode.easyec.sika;

/**
 * Created by IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-1-28
 * Time: 下午10:40
 * To change this template use File | Settings | File Templates.
 */
public interface WorkbookHandler<T> {

    /**
     * 初始化方法,该方法在populate调用前,会被调用一次并且仅调用一次.
     */
    void doInit();

    /**
     * 结束方法,该方法在完成文档操作后会被调用并且仅调用一次.
     */
    void doFinish();

    /**
     * 当Excel数据都被处理完后,该方法会被调用.
     */
    void doFinally();

    /**
     * 当有<code>WorkingException</code>错误发生时,该方法会被调用.
     *
     * @param e <code>WorkingException</code>
     * @see WorkingException
     */
    void doCatch(WorkingException e);

    /**
     * 执行文档数据处理的方法
     *
     * @param o 工作本数据对象
     * @return 返回true则继续处理余下的数据，否则结束操作
     * @throws WorkingException
     */
    boolean populate(T o) throws WorkingException;

    /**
     * 返回当前工作本的头信息
     *
     * @return {@link WorkbookHeader}
     */
    WorkbookHeader getHeader();

    /**
     * 得到当前处理的文档的类型
     *
     * @return 文档类型枚举
     * @see DocType
     */
    DocType getDocType();

    /**
     * 设置当前处理的文档的类型
     *
     * @param docType 文档类型的枚举
     */
    void setDocType(DocType docType);

    /**
     * 返回工作本页面信息
     *
     * @return {@link WorkPage}
     */
    WorkPage getWorkPage();

    /**
     * 设置工作本页面对象信息
     *
     * @param workPage {@link WorkPage}
     */
    void setWorkPage(WorkPage workPage);
}
