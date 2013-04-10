package com.googlecode.easyec.sika;

import com.googlecode.easyec.sika.event.WorkbookHandlerChangeListener;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

/**
 * 工作本读操作基本类。
 * <p>
 * 工作本的类型可以有：
 * <ul>
 * <li>Excel文件</li>
 * <li>csv文件</li>
 * </ul>
 * </p>
 *
 * @author JunJie.Zhang
 */
public class WorkbookReader implements Workbook<WorkbookHandler> {

    private WorkbookHandlerChangeListener workbookHandlerChangeListener;
    private List<WorkbookHandler> workbookHandlerList = synchronizedList(new ArrayList<WorkbookHandler>());

    /**
     * 向当前的工作本设置一个事件监听器。
     * <p>
     * 当工作本中有大于1个<code>WorkbookHandler</code>实现，
     * 且被顺序执行时，此监听器则在第二个<code>WorkbookHandler</code>
     * 执行之前被执行。
     * </p>
     *
     * @param listener {@link WorkbookHandlerChangeListener}
     */
    public void setWorkbookHandlerChangeListener(WorkbookHandlerChangeListener listener) {
        this.workbookHandlerChangeListener = listener;
    }

    /**
     * 得到当前添加的<code>WorkbookHandlerChangeListener</code>对象。
     *
     * @return {@link WorkbookHandlerChangeListener}
     */
    public WorkbookHandlerChangeListener getWorkbookHandlerChangeListener() {
        return workbookHandlerChangeListener;
    }

    /**
     * 为此工作本添加一个工作页面。
     * <p>
     * 工作页面主要负责处理工作本中每页的数据。
     * 调用此方法，工作页面应该以调用顺序被添加。
     * </p>
     *
     * @param handler 工作页面对象
     * @see WorkbookHandler
     */
    public void add(WorkbookHandler handler) {
        if (handler != null) {
            this.workbookHandlerList.add(handler);
        }
    }

    /**
     * 判断此工作本中是否有至少一个工作页面对象。
     *
     * @return 有工作页面对象，则返回true；否则返回false
     */
    public boolean hasMore() {
        return !this.workbookHandlerList.isEmpty();
    }

    /**
     * 返回当前被添加到此工作本中的工作页面的数量。
     *
     * @return 数量应该大于或等于0
     */
    public int size() {
        return this.workbookHandlerList.size();
    }

    /**
     * 得到给定索引上的工作页面对象。
     *
     * @param i 索引，从0开始
     * @return 返回一个工作页面对象，如果没有，则返回null
     * @see WorkbookHandler
     */
    public WorkbookHandler get(int i) {
        return this.workbookHandlerList.get(i);
    }
}
