package com.googlecode.easyec.sika.ss.data;

import com.googlecode.easyec.sika.data.CellWorkData;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public class ExcelData extends CellWorkData {

    private static final long serialVersionUID = -7186513457743966646L;
    private boolean wrapText;

    public ExcelData(int x, int y) {
        this(null, x, y);
    }

    public ExcelData(Object value, int x, int y) {
        this(value, false, x, y);
    }

    public ExcelData(Object value, boolean wrapText, int x, int y) {
        super(value, x, y);

        this.wrapText = wrapText;
    }

    public boolean isWrapText() {
        return wrapText;
    }
}
