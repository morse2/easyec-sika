package com.googlecode.easyec.sika.ss.internal;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.event.*;
import com.googlecode.easyec.sika.ss.AbstractExcelProcess;
import com.googlecode.easyec.sika.ss.ExcelCtrl;
import com.googlecode.easyec.sika.ss.ExcelReadProcess;
import com.googlecode.easyec.sika.ss.data.ExcelData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;

public class DefaultExcelRowDataReadProcess extends AbstractExcelProcess implements ExcelReadProcess {

    @Override
    public void read(Workbook wb, WorkbookReader reader) throws WorkingException {
        // create a formula evaluator
        FormulaEvaluator fe = wb.getCreationHelper().createFormulaEvaluator();

        int lastSheetNum = wb.getNumberOfSheets();
        for (int i = 0; i < lastSheetNum; i++) {
            WorkbookHandler handler;

            /*
             * 为每个sheet取相应索引的Handler。
             * 但是当Handler的可以用数量少于sheet数量，
             * 那么就跳出此sheet及后面的sheet的处理。
             */
            try {
                handler = reader.get(i);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                break;
            }

            if (!(handler instanceof WorkbookRowHandler)) {
                logger.warn("WorkbookHandler must be instanceof WorkbookRowHandler. Actual type is: [{}]", handler);

                continue;
            }

            try {
                Sheet sheet = wb.getSheetAt(i);

                WorkPage workPage = new WorkPage(i, sheet.getSheetName());

                if (i > 0) {
                    WorkbookHandlerChangeListener workbookHandlerChangeListener = reader.getWorkbookHandlerChangeListener();
                    if (workbookHandlerChangeListener != null) {
                        if (!workbookHandlerChangeListener.accept(new WorkbookHandleEvent(workPage)))
                            break;
                    }
                }

                /*
                 * 如果处理器对象类型是ExcelCtrl，
                 * 则设置工作页对象实例
                 */
                if (handler instanceof ExcelCtrl) {
                    ((ExcelCtrl) handler).setWorkPage(workPage);
                }

                // do init method
                doInit(wb, handler);

                // process per row data
                processPerRowDataOfSheet((WorkbookRowHandler) handler, sheet, fe);

                // do finish
                handler.doFinish();

                // post event listener
                WorkbookPostHandleListener postHandleListener = reader.getWorkbookPostHandleListener();
                if (postHandleListener != null) {
                    postHandleListener.postProcess(new WorkbookHandleEvent(workPage));
                }
            } finally {
                // do finally
                handler.doFinally();
            }
        }
    }

    protected void processPerRowDataOfSheet(WorkbookRowHandler handler, Sheet sheet, FormulaEvaluator fe) {
        WorkbookHeader head = handler.getHeader();
        int headCount = head.getHeaderCount();
        int lastRowNum = getRowNum(sheet);

        ConcurrentMap<Integer, List<WorkData>> dataMap = new ConcurrentHashMap<>(1);
        for (int j = 0; j < lastRowNum; j++) {
            /*
             * 如果未能得到Row of Sheet的对象信息，
             * 则表示该sheet有问题或没有Row的数据。
             * 则跳出循环，结束解析的操作。
             */
            Row row = sheet.getRow(j);
            if (row == null) {
                logger.debug("Row object is null, so break out.");

                break;
            }

            // 计算业务上的头数据
            if (j < headCount) {
                // 回填业务的头数据信息
                head.addHeader(getRowData(row, fe));

                continue;
            }

            try {
                List<WorkData> rowData = getRowData(row, fe);
                WorkbookBlankRowListener blankRowListeners = handler.getBlankRowListeners();
                if (blankRowListeners != null) {
                    boolean b = blankRowListeners.accept(new RowEvent(rowData, j + 1));
                    if (!b) break;
                }

                dataMap.put(j, rowData);

                if (!handler.populate(dataMap)) break;
            } catch (WorkingException e) {
                handler.doCatch(e);
                if (e.isStop()) break;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                WorkingException ex = new WorkingException(e, true);
                handler.doCatch(ex);
                if (ex.isStop()) break;
            } finally {
                dataMap.remove(j);
            }
        }
    }

    protected List<WorkData> getRowData(Row row, FormulaEvaluator fe) {
        int lastCellNum = row.getLastCellNum();
        if (lastCellNum < row.getPhysicalNumberOfCells()) {
            lastCellNum = row.getPhysicalNumberOfCells();
        }

        int i = row.getRowNum() - 1;
        List<WorkData> list = new ArrayList<>();
        for (int j = 0; j < lastCellNum; j++) {
            list.add(new ExcelData(resolveCell(row.getCell(j), fe), i, j));
        }

        return list;
    }

    protected Object resolveCell(Cell cell, FormulaEvaluator fe) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case BLANK:
                    return EMPTY;
                case BOOLEAN:
                    return cell.getBooleanCellValue();
                case ERROR:
                    return cell.getErrorCellValue();
                case FORMULA:
                    return resolveCellValue(fe.evaluate(cell));
                case NUMERIC:
                    if (isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    }

                    return cell.getNumericCellValue();
                case STRING:
                    return cell.getRichStringCellValue().getString();
            }
        }

        return null;
    }

    private Object resolveCellValue(CellValue cellVal) {
        if (cellVal != null) {
            switch (cellVal.getCellType()) {
                case BLANK:
                    return EMPTY;
                case BOOLEAN:
                    return cellVal.getBooleanValue();
                case ERROR:
                    return cellVal.getErrorValue();
                case NUMERIC:
                    return cellVal.getNumberValue();
                case STRING:
                    return cellVal.getStringValue();
            }
        }

        return null;
    }
}
