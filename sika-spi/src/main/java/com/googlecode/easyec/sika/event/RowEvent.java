package com.googlecode.easyec.sika.event;

import com.googlecode.easyec.sika.WorkData;

import java.util.EventObject;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-1-29
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class RowEvent extends EventObject {

    private int numberOfRow;

    public RowEvent(Object source, int numberOfRow) {
        super(source);
        this.numberOfRow = numberOfRow;
    }

    public int getNumberOfRow() {
        return numberOfRow;
    }

    public List<WorkData> getWorkData() {
        return (List<WorkData>) source;
    }
}
