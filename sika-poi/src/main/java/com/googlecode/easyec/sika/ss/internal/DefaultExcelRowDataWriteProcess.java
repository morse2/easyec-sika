package com.googlecode.easyec.sika.ss.internal;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.converters.Object2StringConverter;
import com.googlecode.easyec.sika.ss.AbstractExcelProcess;
import com.googlecode.easyec.sika.ss.ExcelCtrl;
import com.googlecode.easyec.sika.ss.ExcelWriteProcess;
import com.googlecode.easyec.sika.ss.data.ExcelData;
import com.googlecode.easyec.sika.ss.event.ExcelSheetEventCtrl;
import com.googlecode.easyec.sika.ss.event.InitializingSheetEvent;
import com.googlecode.easyec.sika.ss.event.InitializingSheetListener;
import com.googlecode.easyec.sika.ss.formulas.Formula;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

public class DefaultExcelRowDataWriteProcess extends AbstractExcelProcess implements ExcelWriteProcess {

    @Override
    public byte[] write(Workbook wb, WorkbookWriter writer) throws WorkingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int removeSheetCount = 0;  // 需要执行删除工作表的次数

        out:
        for (int i = 0; i < writer.size(); i++) {
            @SuppressWarnings("unchecked")
            WorkbookCallback<Object> callback = (WorkbookCallback<Object>) writer.get(i);

            // 默认初始化一个工作页对象
            WorkPage workPage = null;
            /*
             * 判断处理回调类的对象类型，如果是ExcelCtrl
             * 类的一个实例，则从该类中获取工作页实例对象
             */
            if (callback instanceof ExcelCtrl) {
                workPage = ((ExcelCtrl) callback).getWorkPage();
            }

            if (workPage == null) {
                workPage = WorkPage.DEFAULT;
            }

            // 获取当前Excel模板中的工作页的索引号
            int sheetIndex = workPage.getSheetIndex();
            logger.debug("Index of sheet is: [{}].", sheetIndex);

            Sheet sheet;

            try {
                // 从工作页标识的索引号中获取模板的工作页面
                sheet = wb.cloneSheet(sheetIndex);
                // 累加要删除模板的次数
                removeSheetCount++;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                throw new WorkingException(e, true);
            }

            List<?> list;

            // set custom sheet name
            if (isNotBlank(workPage.getSheetName())) {
                wb.setSheetName(wb.getSheetIndex(sheet), workPage.getSheetName());
            }

            // do init method
            doInit(wb, callback);

            try {
                list = callback.doGrab();
            } catch (WorkingException e) {
                callback.doCatch(e);

                /*
                 * If WorkingException.isStop,
                 * break out all of workbook's callback,
                 * else continue handling others callback.
                 */
                if (e.isStop()) break;
                else continue;
            }

            try {
                // process workbook header
                int j = processHeader(wb, sheet, callback.getHeader());
                logger.debug("How many rows for header? [{}].", j);

                // check whether has data list
                if (CollectionUtils.isEmpty(list)) {
                    logger.debug("No records were found.");

                    continue;
                }

                if (callback instanceof ExcelSheetEventCtrl) {
                    // fire initialized sheet event listener
                    InitializingSheetListener li = ((ExcelSheetEventCtrl) callback).getInitializingSheetListener();
                    if (null != li) li.doInit(new InitializingSheetEvent(sheet, j + list.size()));
                }

                // 获取数据行的样式
                List<Cell> firstRowCells = new ArrayList<>();
                Row firstRow = sheet.getRow(j);
                if (null != firstRow) {
                    int lastCellNum = getCellNum(firstRow);
                    for (int k = 0; k < lastCellNum; k++) {
                        firstRowCells.add(firstRow.getCell(k, CREATE_NULL_AS_BLANK));
                    }
                }

                for (int k = 0; k < list.size(); k++, j++) {
                    Row row = getOrCreate(sheet, k);
                    // 设置行的样式
                    if (null != firstRow && callback.getStrategy().isCopyRowStyleOnWrite()) {
                        try {
                            CellStyle rowStyle = firstRow.getRowStyle();
                            if (null != rowStyle) row.setRowStyle(rowStyle);
                        } catch (Exception e) {
                            // ignore this error
                        }
                    }

                    List<WorkData> dataToWrite = null;

                    try {
                        dataToWrite = callback.populate(k, list.get(k));
                        if (CollectionUtils.isEmpty(dataToWrite)) {
                            logger.warn("No data returns after method 'populate' invoked.");

                            continue;
                        }

                        for (int m = 0; m < dataToWrite.size(); m++) {
                            Cell cell = row.getCell(m, CREATE_NULL_AS_BLANK);

                            CellStyle curStyle = null;
                            if (callback.getStrategy().isCopyRowStyleOnWrite()) {
                                Cell firstRowCell = null;

                                try {
                                    firstRowCell = firstRowCells.get(m);
                                } catch (Exception e) {
                                    // ignore this error
                                }

                                if (firstRowCell != null) {
                                    // 设置单元格样式
                                    curStyle = firstRowCell.getCellStyle();
                                }
                            }

                            setCellValue(wb, cell, curStyle, dataToWrite.get(m));
                        }
                    } catch (WorkingException e) {
                        callback.doCatch(e);

                        if (e.isStop()) continue out;
                    } finally {
                        if (dataToWrite != null) {
                            dataToWrite.clear();
                        }
                    }
                }

                callback.doFinish();
            } finally {
                callback.doFinally();
            }
        }

        try {
            for (int i = 0; i < removeSheetCount; i++) {
                wb.removeSheetAt(0);
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new WorkingException(e, true);
        }

        return out.toByteArray();
    }

    protected int processHeader(Workbook wb, Sheet sheet, WorkbookHeader header) {
        if (header != null && header.hasHeader()) {
            List<WorkData[]> headData = header.getHeaderList();
            for (int i = 0; i < headData.size(); i++) {
                WorkData[] data = headData.get(i);

                if (isNotEmpty(data)) {
                    Row row = getOrCreate(sheet, i);
                    for (int j = 0; j < data.length; j++) {
                        setCellValue(wb, row.getCell(j, CREATE_NULL_AS_BLANK), null, data[j]);
                    }
                }
            }

            return header.getHeaderCount();
        }

        return 0;
    }

    private void setCellValue(Workbook wb, Cell cell, CellStyle style, WorkData data) {
        if (data != null) {
            if (style != null) {
                getOrCreate(cell).cloneStyleFrom(style);
            }

            switch (data.getWorkDataType()) {
                case NUMBER:
                    cell.setCellValue(((Number) data.getValue()).doubleValue());
                    break;
                case DATE:
                    cell.setCellValue(((Date) data.getValue()));
                    break;
                case FORMULA:
                    Object formulaObj = data.getValue();
                    if (formulaObj instanceof Formula) {
                        cell.setCellFormula(
                            ((Formula) formulaObj).encode()
                        );

                        break;
                    }
                default:
                    if ((data instanceof ExcelData) && ((ExcelData) data).isWrapText()) {
                        getOrCreate(cell).setWrapText(true);
                    }

                    cell.setCellValue(
                        wb.getCreationHelper().createRichTextString(
                            data.getValue(new Object2StringConverter())
                        )
                    );
            }
        }
    }
}
