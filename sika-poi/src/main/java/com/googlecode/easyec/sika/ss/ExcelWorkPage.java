package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.WorkPage;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelWorkPage extends WorkPage {

    private Sheet sheet;

    public ExcelWorkPage(String sheetName) {
        super(sheetName);
    }

    public ExcelWorkPage(int sheetIndex, String sheetName) {
        super(sheetIndex, sheetName);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }
}
