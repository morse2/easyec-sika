package com.googlecode.easyec.sika;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-5
 * Time: 下午12:26
 * To change this template use File | Settings | File Templates.
 */
public interface WorkbookCallback<T> {

    /**
     * 初始化方法,该方法在doGrab调用前,会被调用一次并且仅调用一次.
     */
    void doInit();

    /**
     * 实现数据抓取的方法
     *
     * @return 抓取到的数据对象
     * @throws WorkingException
     */
    List<T> doGrab() throws WorkingException;

    /**
     * 执行数据处理并转换成文档数据的方法
     *
     * @param o 数据对象
     * @return 返回一个列表形式的工作本数据集合
     * @throws WorkingException
     */
    List<WorkData> populate(T o) throws WorkingException;

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
     * 返回当前工作本的工作页面对象
     *
     * @return {@link WorkPage}
     */
    WorkPage getWorkPage();
}
