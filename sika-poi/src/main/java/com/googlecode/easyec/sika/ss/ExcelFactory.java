package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.converters.Object2StringConverter;
import com.googlecode.easyec.sika.event.*;
import com.googlecode.easyec.sika.ss.data.ExcelData;
import com.googlecode.easyec.sika.ss.event.ExcelSheetEventCtrl;
import com.googlecode.easyec.sika.ss.event.InitializingSheetEvent;
import com.googlecode.easyec.sika.ss.event.InitializingSheetListener;
import com.googlecode.easyec.sika.ss.formulas.Formula;
import com.googlecode.easyec.sika.support.WorkbookStrategy;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.*;

import static com.googlecode.easyec.sika.DocType.EXCEL03;
import static com.googlecode.easyec.sika.DocType.EXCEL07;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.poi.ss.usermodel.Cell.*;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

/**
 * Excel文档处理工厂类。
 *
 * @author JunJie
 */
public final class ExcelFactory {

    private static final ThreadLocal<ExcelFactory> local = new ThreadLocal<ExcelFactory>();
    private Logger logger = LoggerFactory.getLogger(ExcelFactory.class);

    private ExcelFactory() { /* no op */ }

    public static ExcelFactory getInstance() {
        synchronized (local) {
            ExcelFactory factory = local.get();
            if (factory == null) {
                factory = new ExcelFactory();
                local.set(factory);
            }

            return factory;
        }
    }

    public void read(File file, WorkbookReader workbookReader) throws WorkingException {
        try {
            read(new FileInputStream(file), workbookReader);
        } catch (FileNotFoundException e) {
            throw new WorkingException(e, true);
        }
    }

    public void read(InputStream in, WorkbookReader reader) throws WorkingException {
        Workbook wb;

        try {
            assertEmptyWorkbookHandler(reader);

            wb = WorkbookFactory.create(in);

            doRead(wb, reader);
        } catch (IOException e) {
            throw new WorkingException(e, true);
        } catch (InvalidFormatException e) {
            throw new WorkingException(e, true);
        } finally {
            IOUtils.closeQuietly(in);

            wb = null;
        }
    }

    public void write(InputStream in, OutputStream out, WorkbookWriter writer) throws WorkingException {
        Assert.notNull(out, "OutputStream object is null.");

        try {
            IOUtils.write(write(in, writer), out);
        } catch (IOException e) {
            throw new WorkingException(e, true);
        }
    }

    public byte[] write(InputStream in, WorkbookWriter writer) throws WorkingException {
        Assert.notNull(in, "InputStream object is null.");
        Assert.notNull(writer, "WorkbookWriter object is null.");

        Workbook wb;

        try {
            wb = WorkbookFactory.create(in);

            return doWrite(wb, writer);
        } catch (IOException e) {
            throw new WorkingException(e, true);
        } catch (InvalidFormatException e) {
            throw new WorkingException(e, true);
        } finally {
            IOUtils.closeQuietly(in);

            wb = null;
        }
    }

    private void assertEmptyWorkbookHandler(com.googlecode.easyec.sika.Workbook workbook) throws WorkingException {
        if (!workbook.hasMore()) {
            throw new WorkingException("No Workbook was added.", true);
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized byte[] doWrite(Workbook wb, WorkbookWriter writer) throws WorkingException {
        // create a creation helper
        CreationHelper helper = wb.getCreationHelper();

        /*int lastSheetNum = writer.size();
        if (lastSheetNum > wb.getNumberOfSheets()) {
            logger.warn("Excel sheetsToRemove' size are greater than WorkbookCallback's." +
                    " Sheet's size: [" + wb.getNumberOfSheets() + "]," +
                    " WorkbookCallback's size: [" + lastSheetNum + "]." +
                    " So remaining WorkbookCallback will be ignored.");

            lastSheetNum = wb.getNumberOfSheets();
        }*/

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int removeSheetCount = 0; // 需要执行删除工作表的次数

        out:
        for (int i = 0; i < writer.size(); i++) {
            WorkbookCallback<Object> callback = (WorkbookCallback<Object>) writer.get(i);

            // 默认初始化一个工作页对象
            WorkPage workPage = WorkPage.DEFAULT;
            /*
            判断处理回调类的对象类型，如果是ExcelRowCallback
            类的一个实例，则从该类中获取工作页实例对象
            */
            if (callback instanceof ExcelCtrl) {
                workPage = ((ExcelCtrl) callback).getWorkPage();
            }

            // 获取当前Excel模板中的工作页的索引号
            int sheetIndex = workPage.getSheetIndex();
            logger.debug("Sheet index is: [" + sheetIndex + "].");

            Sheet sheet;

            try {
                // 从工作页标识的索引号中获取模板的工作页面
                sheet = wb.cloneSheet(sheetIndex);
                // 累加要删除模板的次数
                removeSheetCount++;
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);

                throw new WorkingException(e, true);
            }

            List<Object> list;

            try {
                if (isNotBlank(workPage.getSheetName())) {
                    wb.setSheetName(wb.getNumberOfSheets() - 1, workPage.getSheetName());
                }

                try {
                    callback.doInit();
                } catch (WorkingException e) {
                    logger.error(e.getMessage(), e);

                    if (e.isStop()) throw e;
                }

                WorkbookStrategy strategy = callback.getStrategy();
                if (strategy == null) {
                    strategy = WorkbookStrategy.DEFAULT;

                    callback.setStrategy(strategy);
                }

                strategy.setDocType((wb instanceof HSSFWorkbook) ? EXCEL03 : EXCEL07);

                try {
                    list = callback.doGrab();
                } catch (WorkingException e) {
                    callback.doCatch(e);

                    // If WorkingException.isStop,
                    // break out all of workbook's callback,
                    // else continue handling others callback.
                    if (e.isStop()) break;
                    else continue;
                }

                int j = 0;
                WorkbookHeader header = callback.getHeader();
                if (header != null && header.hasHeader()) {
                    List<WorkData[]> headerList = header.getHeaderList();
                    for (int m = j; m < headerList.size(); m++) {
                        WorkData[] dataList = headerList.get(m);

                        if (ArrayUtils.isNotEmpty(dataList)) {
                            Row row = sheet.getRow(m);
                            if (row == null) row = sheet.createRow(m);

                            for (int n = 0; n < dataList.length; n++) {
                                WorkData workData = dataList[n];

                                Cell cell = row.getCell(n, CREATE_NULL_AS_BLANK);

                                switch (workData.getWorkDataType()) {
                                    case NUMBER:
                                        cell.setCellValue(((Number) workData.getValue()).doubleValue());
                                        break;
                                    case DATE:
                                        cell.setCellValue(((Date) workData.getValue()));
                                        break;
                                    case FORMULA:
                                        Object formulaObj = workData.getValue();
                                        if (formulaObj instanceof Formula) {
                                            cell.setCellFormula(
                                                ((Formula) formulaObj).encode()
                                            );

                                            break;
                                        }
                                    default:
                                        if ((workData instanceof ExcelData) && ((ExcelData) workData).isWrapText()) {
                                            CellStyle cs = wb.createCellStyle();
                                            cs.setWrapText(true);
                                            cell.setCellStyle(cs);
                                        }

                                        cell.setCellValue(
                                            helper.createRichTextString(
                                                workData.getValue(new Object2StringConverter())
                                            )
                                        );
                                }
                            }
                        }
                    }

                    j = header.getHeaderCount();
                }

                if (CollectionUtils.isEmpty(list)) {
                    logger.debug("No records were found.");
                    continue;
                }

                if (callback instanceof ExcelSheetEventCtrl) {
                    // 出发初始化工作页的事件监听
                    InitializingSheetListener li = ((ExcelSheetEventCtrl) callback).getInitializingSheetListener();
                    if (null != li) li.doInit(new InitializingSheetEvent(sheet, j + list.size()));
                }

                // 获取数据行的样式
                List<Cell> firstRowCells = new ArrayList<Cell>();

                Row firstRow = sheet.getRow(j);
                if (null != firstRow) {
                    int lastCellNum = firstRow.getLastCellNum();
                    if (lastCellNum < firstRow.getPhysicalNumberOfCells()) {
                        lastCellNum = firstRow.getPhysicalNumberOfCells();
                    }

                    for (int k = 0; k < lastCellNum; k++) {
                        firstRowCells.add(firstRow.getCell(k, CREATE_NULL_AS_BLANK));
                    }
                }

                for (int k = 0; k < list.size(); k++, j++) {
                    Row row = sheet.getRow(j);
                    if (row == null) row = sheet.createRow(j);

                    // 设置行的样式
                    if (null != firstRow && strategy.isCopyRowStyleOnWrite()) {
                        try {
                            CellStyle rowStyle = firstRow.getRowStyle();
                            if (null != rowStyle) row.setRowStyle(rowStyle);
                        } catch (Exception e) {
                            // ignore this error
                        }
                    }

                    List<WorkData> dataList = null;

                    try {
                        dataList = callback.populate(k, list.get(k));
                        if (dataList == null) continue;

                        for (int m = 0; m < dataList.size(); m++) {
                            Cell cell = row.getCell(m, CREATE_NULL_AS_BLANK);

                            if (strategy.isCopyRowStyleOnWrite()) {
                                if (!firstRowCells.isEmpty() && m < firstRowCells.size()) {
                                    // 设置单元格样式
                                    CellStyle cellStyle = firstRowCells.get(m).getCellStyle();
                                    if (null != cellStyle) cell.setCellStyle(cellStyle);
                                }
                            }

                            WorkData workData = dataList.get(m);
                            if (workData != null) {
                                switch (workData.getWorkDataType()) {
                                    case NUMBER:
                                        cell.setCellValue(((Number) workData.getValue()).doubleValue());
                                        break;
                                    case DATE:
                                        cell.setCellValue(((Date) workData.getValue()));
                                        break;
                                    case FORMULA:
                                        Object formulaObj = workData.getValue();
                                        if (formulaObj instanceof Formula) {
                                            cell.setCellFormula(
                                                ((Formula) formulaObj).encode()
                                            );

                                            break;
                                        }
                                    default:
                                        if ((workData instanceof ExcelData) && ((ExcelData) workData).isWrapText()) {
                                            CellStyle cs = cell.getCellStyle();
                                            if (null == cs) cs = wb.createCellStyle();
                                            cs.setWrapText(true);
                                            cell.setCellStyle(cs);
                                        }

                                        cell.setCellValue(helper.createRichTextString(workData.getValue(new Object2StringConverter())));
                                }
                            }
                        }
                    } catch (WorkingException e) {
                        callback.doCatch(e);

                        if (e.isStop()) break out;
                    } finally {
                        if (dataList != null) {
                            dataList.clear();
                            dataList = null;
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
        } catch (IOException e) {
            logger.error(e.getMessage(), e);

            throw new WorkingException(e, true);
        }

        return out.toByteArray();
    }

    private synchronized void doRead(Workbook wb, WorkbookReader reader) throws WorkingException {
        // create a formula evaluator
        FormulaEvaluator fe = wb.getCreationHelper().createFormulaEvaluator();

        /*int lastSheetNum = reader.size();
        if (lastSheetNum > wb.getNumberOfSheets()) {
            logger.warn("Excel sheets' size are greater than WorkbookHandler's." +
                    " Sheet's size: [" + wb.getNumberOfSheets() + "]," +
                    " WorkbookHandler's size: [" + lastSheetNum + "]." +
                    " So remaining WorkbookHandlers will be ignored.");

            lastSheetNum = wb.getNumberOfSheets();
        }*/

        int lastSheetNum = wb.getNumberOfSheets();

        for (int i = 0; i < lastSheetNum; i++) {
            WorkbookHandler handler;

            // 为每个sheet取相应索引的Handler。
            // 但是当Handler的可以用数量少于sheet数量，
            // 那么就跳出此sheet及后面的sheet的处理。
            try {
                handler = reader.get(i);
            } catch (Exception e) {
                logger.trace(e.getMessage(), e);

                break;
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
                如果处理器对象类型是ExcelCtrl，
                则设置工作页对象实例
                */
                if (handler instanceof ExcelCtrl) {
                    ((ExcelCtrl) handler).setWorkPage(workPage);
                }

                try {
                    handler.doInit();
                } catch (WorkingException e) {
                    logger.error(e.getMessage(), e);

                    if (e.isStop()) throw e;
                }

                // 获取文档策略对象
                WorkbookStrategy strategy = handler.getStrategy();
                if (strategy == null) {
                    strategy = WorkbookStrategy.DEFAULT;
                    handler.setStrategy(strategy);
                }

                strategy.setDocType((wb instanceof HSSFWorkbook) ? EXCEL03 : EXCEL07);

                if (handler instanceof WorkbookRowHandler) {
                    processSheetPerRow((WorkbookRowHandler) handler, sheet, fe);
                }

                handler.doFinish();

                /* 工作本后置处理事件 */
                WorkbookPostHandleListener postHandleListener = reader.getWorkbookPostHandleListener();
                if (postHandleListener != null) {
                    postHandleListener.postProcess(new WorkbookHandleEvent(workPage));
                }
            } finally {
                handler.doFinally();
            }
        }
    }

    private void processSheetPerRow(WorkbookRowHandler handler, Sheet sheet, FormulaEvaluator fe) throws WorkingException {
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum < sheet.getPhysicalNumberOfRows()) {
            lastRowNum = sheet.getPhysicalNumberOfRows();
        }

        for (int j = 0; j < lastRowNum; j++) {
            WorkbookHeader header = handler.getHeader();
            if (header.hasHeader() && ((j + 1) <= header.getHeaderCount())) {
                // TODO finish it
                continue;
            }

            Row row = sheet.getRow(j);

            List<WorkData> list = new ArrayList<WorkData>();
            Map<Integer, List<WorkData>> map = new HashMap<Integer, List<WorkData>>();

            try {
                if (row == null) {
                    logger.debug("Row object is null, so break out.");

                    break;
                }

                int lastCellNum = row.getLastCellNum();
                if (lastCellNum < row.getPhysicalNumberOfCells()) {
                    lastCellNum = row.getPhysicalNumberOfCells();
                }

                for (int k = 0; k < lastCellNum; k++) {
                    list.add(
                        WorkData.createCellWorkData(getCellContent(row.getCell(k), fe), j, k)
                    );
                }

                WorkbookBlankRowListener blankRowListeners = handler.getBlankRowListeners();
                if (blankRowListeners != null) {
                    boolean b = blankRowListeners.accept(new RowEvent(list, j + 1));
                    if (!b) break;
                }

                map.put(j, list);

                if (!handler.populate(map)) break;
            } catch (WorkingException e) {
                handler.doCatch(e);
                if (e.isStop()) {
                    break;
                }
            } finally {
                if (map != null) {
                    map.clear();
                    map = null;
                }

                list.clear();
                list = null;
            }
        }
    }

    private Object getCellContent(Cell cell, FormulaEvaluator fe) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case CELL_TYPE_BLANK:
                    return "";
                case CELL_TYPE_BOOLEAN:
                    return cell.getBooleanCellValue();
                case CELL_TYPE_ERROR:
                    return cell.getErrorCellValue();
                case CELL_TYPE_FORMULA:
                    return getCellContent(fe.evaluate(cell));
                case CELL_TYPE_NUMERIC:
                    if (isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    }

                    return cell.getNumericCellValue();
                case CELL_TYPE_STRING:
                    return cell.getRichStringCellValue().getString();
            }
        }

        return null;
    }

    private Object getCellContent(CellValue cellVal) {
        if (cellVal != null) {
            switch (cellVal.getCellType()) {
                case CELL_TYPE_BLANK:
                    return "";
                case CELL_TYPE_BOOLEAN:
                    return cellVal.getBooleanValue();
                case CELL_TYPE_ERROR:
                    return cellVal.getErrorValue();
                case CELL_TYPE_NUMERIC:
                    return cellVal.getNumberValue();
                case CELL_TYPE_STRING:
                    return cellVal.getStringValue();
            }
        }

        return null;
    }
}
