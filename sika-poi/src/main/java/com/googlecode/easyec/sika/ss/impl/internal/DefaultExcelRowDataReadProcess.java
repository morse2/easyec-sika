package com.googlecode.easyec.sika.ss.impl.internal;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.event.RowEvent;
import com.googlecode.easyec.sika.event.WorkbookBlankRowListener;
import com.googlecode.easyec.sika.mappings.*;
import com.googlecode.easyec.sika.ss.data.ExcelData;
import com.googlecode.easyec.sika.ss.impl.AbstractExcelReadProcess;
import org.apache.poi.ss.usermodel.*;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;

public class DefaultExcelRowDataReadProcess extends AbstractExcelReadProcess {

    @Override
    protected boolean accept(int sheetPos, WorkbookHandler handler) {
        return handler instanceof WorkbookRowHandler;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean processPerRowDataOfSheet(WorkbookHandler handler, Sheet sheet, FormulaEvaluator fe) {
        WorkbookHeader head = handler.getHeader();
        int headCount = head.getHeaderCount();
        int lastRowNum = getRowNum(sheet);

        if (handler instanceof AnnotationWorkbookRowHandler) {
            AnnotationWorkbookRowHandler anHandler = (AnnotationWorkbookRowHandler) handler;
            AbstractBeanMappingParamResolver resolver = anHandler.getBeanReadMappingParamResolver();
            if (resolver == null) {
                anHandler.setBeanReadMappingParamResolver(createBeanMappingParamResolver());
            }
        }

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
                WorkbookBlankRowListener blankRowListeners = ((WorkbookRowHandler) handler).getBlankRowListeners();
                if (blankRowListeners != null) {
                    if (!blankRowListeners.accept(new RowEvent(rowData, j + 1))) break;
                }

                dataMap.put(j, rowData);

                if (!handler.populate(dataMap)) break;
            } catch (WorkingException e) {
                handler.doCatch(e);
                if (e.isStop()) return false;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                WorkingException ex = new WorkingException(e, true);
                handler.doCatch(ex);
                if (ex.isStop()) return false;
            } finally {
                dataMap.remove(j);
            }
        }

        return true;
    }

    protected BeanReadMappingParamResolver createBeanMappingParamResolver() {
        return new BeanReadMappingParamResolver(createAnnotationMappingResolverChain());
    }

    @SuppressWarnings("unchecked")
    protected AnnotationMappingResolverChain<? extends BeanPropertyAnnotationMappingParam, PropertyDescriptor> createAnnotationMappingResolverChain() {
        return new AnnotationMappingResolverChain<>(
            new ColumnReadMappingParamResolver(),
            new ColumnMappingParamResolver()
        );
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
