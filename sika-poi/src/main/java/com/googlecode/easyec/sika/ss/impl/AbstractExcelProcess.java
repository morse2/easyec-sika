package com.googlecode.easyec.sika.ss.impl;

import com.googlecode.easyec.sika.WorkbookProcess;
import com.googlecode.easyec.sika.WorkbookProcessAware;
import com.googlecode.easyec.sika.WorkingException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.googlecode.easyec.sika.DocType.EXCEL03;
import static com.googlecode.easyec.sika.DocType.EXCEL07;

public class AbstractExcelProcess implements WorkbookProcess {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected void doInit(Workbook wb, WorkbookProcessAware aware) throws WorkingException {
        try {
            doInit(aware);

            aware.getStrategy().setDocType(
                (wb instanceof HSSFWorkbook) ? EXCEL03 : EXCEL07
            );
        } catch (WorkingException e) {
            logger.error(e.getMessage(), e);

            if (e.isStop()) throw e;
        }
    }

    protected int getRowNum(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        return lastRowNum < sheet.getPhysicalNumberOfRows()
            ? sheet.getPhysicalNumberOfRows()
            : lastRowNum;
    }

    protected int getCellNum(Row row) {
        int lastCellNum = row.getLastCellNum();
        return lastCellNum < row.getPhysicalNumberOfCells()
            ? row.getPhysicalNumberOfCells()
            : lastCellNum;
    }

    protected Row getOrCreate(Sheet sheet, int i) {
        Row row = sheet.getRow(i);
        return row == null ? sheet.createRow(i) : row;
    }

    protected CellStyle getOrCreate(Cell cell) {
        CellStyle style = cell.getCellStyle();
        if (style == null) {
            style = cell.getSheet().getWorkbook().createCellStyle();
            cell.setCellStyle(style);
        }

        return style;
    }
}
