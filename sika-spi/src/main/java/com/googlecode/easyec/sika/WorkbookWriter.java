package com.googlecode.easyec.sika;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public class WorkbookWriter implements Workbook<WorkbookCallback<?>> {

    private List<WorkbookCallback<?>> workbookCallbackList = synchronizedList(new ArrayList<WorkbookCallback<?>>());

    /**
     * 为此工作本添加一个工作页面。
     * <p>
     * 工作页面主要负责处理工作本中每页的数据。
     * 调用此方法，工作页面应该以调用顺序被添加。
     * </p>
     *
     * @param callback 工作页面对象
     * @see WorkbookHandler
     */
    public void add(WorkbookCallback<?> callback) {
        if (callback != null) {
            this.workbookCallbackList.add(callback);
        }
    }

    /**
     * 判断此工作本中是否有至少一个工作页面对象。
     *
     * @return 有工作页面对象，则返回true；否则返回false
     */
    public boolean hasMore() {
        return !this.workbookCallbackList.isEmpty();
    }

    /**
     * 返回当前被添加到此工作本中的工作页面的数量。
     *
     * @return 数量应该大于或等于0
     */
    public int size() {
        return this.workbookCallbackList.size();
    }

    /**
     * 得到给定索引上的工作页面对象。
     *
     * @param i 索引，从0开始
     * @return 返回一个工作页面对象，如果没有，则返回null
     * @see WorkbookHandler
     */
    public WorkbookCallback<?> get(int i) {
        return this.workbookCallbackList.get(i);
    }
}
