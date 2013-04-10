package com.googlecode.easyec.sika.data;

import com.googlecode.easyec.sika.WorkData;

/**
 * 单元格模式的工作本数据类。
 *
 * @author JunJie
 */
public class CellWorkData extends WorkData {

    private static final long serialVersionUID = 5722949818885850918L;
    private int x;
    private int y;

    public CellWorkData(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CellWorkData(Object value, int x, int y) {
        super(value);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
