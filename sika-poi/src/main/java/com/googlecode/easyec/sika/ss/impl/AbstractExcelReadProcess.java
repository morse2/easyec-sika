package com.googlecode.easyec.sika.ss.impl;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.event.WorkbookHandleEvent;
import com.googlecode.easyec.sika.event.WorkbookHandlerChangeListener;
import com.googlecode.easyec.sika.event.WorkbookPostHandleListener;
import com.googlecode.easyec.sika.ss.ExcelCtrl;
import com.googlecode.easyec.sika.ss.ExcelReadProcess;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author junjie
 */
public abstract class AbstractExcelReadProcess extends AbstractExcelProcess implements ExcelReadProcess {

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

                // check type of WorkbookHandler
                if (accept(i, handler)) {
                    // process per row data
                    processPerRowDataOfSheet(handler, sheet, fe);
                }

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

    /**
     * 判断sheet的位置的处理的
     * <code>WorkbookHandler</code>
     * 类型是否一致
     *
     * @param sheetPos sheet位置，从1开始
     * @param handler  <code>WorkbookHandler</code>
     * @return boolean值
     */
    abstract protected boolean accept(int sheetPos, WorkbookHandler handler);

    /**
     * 处理sheet的每一行数据的方法
     *
     * @param handler <code>WorkbookHandler</code>
     * @param sheet   <code>Sheet</code>
     * @param fe      <code>FormulaEvaluator</code>
     */
    abstract protected void processPerRowDataOfSheet(WorkbookHandler handler, Sheet sheet, FormulaEvaluator fe);
}
