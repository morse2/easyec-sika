package com.googlecode.easyec.sika.ss.data;

import com.googlecode.easyec.sika.data.CellWorkData;
import com.googlecode.easyec.sika.ss.formulas.Formula;

import static com.googlecode.easyec.sika.WorkData.WorkDataType.FORMULA;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public class ExcelData extends CellWorkData {

    private static final long serialVersionUID = -2562606425581287938L;
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

    @Override
    protected void guessWorkDataType() {
        if (getValue() instanceof Formula) {
            setWorkDataType(FORMULA);
        } else super.guessWorkDataType();
    }
}
