package com.googlecode.easyec.sika;

import java.util.List;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public interface WorkbookCallback<T> extends WorkbookProcessAware {

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
     * @param index 行索引号
     * @param o     数据对象
     * @return 返回一个列表形式的工作本数据集合
     * @throws WorkingException
     */
    List<WorkData> populate(int index, T o) throws WorkingException;
}
