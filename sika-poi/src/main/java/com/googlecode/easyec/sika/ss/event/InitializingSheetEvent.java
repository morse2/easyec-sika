package com.googlecode.easyec.sika.ss.event;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.EventObject;

/**
 * 初始化工作页的事件对象
 *
 * @author JunJie
 */
public class InitializingSheetEvent extends EventObject {

    private static final long serialVersionUID = 1516999270970951334L;
    private long totalRows;

    public InitializingSheetEvent(Object source, long totalRows) {
        super(source);
        this.totalRows = totalRows;
    }

    /**
     * 返回事件对象关联的工作页对象
     *
     * @return <code>Sheet</code>对象
     */
    public Sheet getSheet() {
        return (Sheet) source;
    }

    /**
     * 返回当前工作页的总行数
     *
     * @return 总行数，包括头+数据
     */
    public long getTotalRows() {
        return totalRows;
    }
}
