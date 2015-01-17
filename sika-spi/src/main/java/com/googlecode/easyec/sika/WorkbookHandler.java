package com.googlecode.easyec.sika;

import com.googlecode.easyec.sika.support.WorkbookStrategy;

/**
 * 工作本处理器类。
 * <p>
 * 该类定义了一个工作本处理的生命周期.
 * </p>
 *
 * @author JunJie
 */
public interface WorkbookHandler<T> {

    /**
     * 初始化方法,该方法在populate调用前,会被调用一次并且仅调用一次.
     *
     * @throws WorkingException
     */
    void doInit() throws WorkingException;

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
     * 返回当前处理的策略
     */
    WorkbookStrategy getStrategy();

    /**
     * 设置当前处理的策略
     *
     * @param strategy 策略对象
     */
    void setStrategy(WorkbookStrategy strategy);
}
