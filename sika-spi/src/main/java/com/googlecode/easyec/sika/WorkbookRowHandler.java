package com.googlecode.easyec.sika;

import com.googlecode.easyec.sika.event.WorkbookBlankRowListener;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工作本行数据处理器实现类。
 * <p>
 * 该类定义了工作本的处理形式为逐行处理。
 * </p>
 *
 * @author JunJie
 */
public abstract class WorkbookRowHandler extends AbstractWorkbookHandler<Map<Integer, List<WorkData>>> {

    private WorkbookBlankRowListener workbookBlankRowListener;

    protected WorkbookRowHandler() {
        this(new WorkbookHeader(1));
    }

    protected WorkbookRowHandler(WorkbookHeader header) {
        super(header);
    }

    public boolean populate(Map<Integer, List<WorkData>> map) throws WorkingException {
        if (map == null || map.isEmpty()) {
            return false;
        }

        Set<Integer> keys = map.keySet();
        for (Integer key : keys) {
            if (!populate(key, map.get(key))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 此方法按行处理文档数据。
     *
     * @param index 行索引号
     * @param list  一行中的列数据
     * @return 返回true则继续处理余下的数据，否则结束操作
     * @throws WorkingException
     */
    abstract public boolean populate(int index, List<WorkData> list) throws WorkingException;

    /**
     * 设置一个空行数据处理的监听器类
     *
     * @param listener {@link com.googlecode.easyec.sika.event.WorkbookBlankRowListener}
     */
    public void setBlankRowListener(WorkbookBlankRowListener listener) {
        this.workbookBlankRowListener = listener;
    }

    /**
     * 返回此工作本处理类的空行数据处理的监听器类
     *
     * @return {@link WorkbookBlankRowListener}
     */
    public WorkbookBlankRowListener getBlankRowListeners() {
        return workbookBlankRowListener;
    }
}
