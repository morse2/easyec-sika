package com.googlecode.easyec.sika;

/**
 * 工作本处理器类。
 * <p>
 * 该类定义了一个工作本处理的生命周期.
 * </p>
 *
 * @author JunJie
 */
public interface WorkbookHandler<T> extends WorkbookProcessAware {

    /**
     * 执行文档数据处理的方法
     *
     * @param o 工作本数据对象
     * @return 返回true则继续处理余下的数据，否则结束操作
     * @throws WorkingException
     */
    boolean populate(T o) throws WorkingException;
}
