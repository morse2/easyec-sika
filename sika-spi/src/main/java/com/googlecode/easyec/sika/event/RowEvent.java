package com.googlecode.easyec.sika.event;

import com.googlecode.easyec.sika.WorkData;

import java.util.EventObject;
import java.util.List;

/**
 * 行数据事件对象
 *
 * @author JunJie
 */
public class RowEvent extends EventObject {

    private static final long serialVersionUID = 498901291436835945L;
    private int numberOfRow;

    public RowEvent(Object source, int numberOfRow) {
        super(source);
        this.numberOfRow = numberOfRow;
    }

    public int getNumberOfRow() {
        return numberOfRow;
    }

    @SuppressWarnings("unchecked")
    public List<WorkData> getWorkData() {
        return (List<WorkData>) source;
    }
}
