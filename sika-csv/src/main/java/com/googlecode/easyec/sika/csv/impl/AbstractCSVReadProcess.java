package com.googlecode.easyec.sika.csv.impl;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.csv.CSVReadProcess;
import com.googlecode.easyec.sika.data.DefaultWorkData;
import com.googlecode.easyec.sika.event.WorkbookHandleEvent;
import com.googlecode.easyec.sika.event.WorkbookPostHandleListener;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractCSVReadProcess extends AbstractCSVProcess implements CSVReadProcess {

    @Override
    public void read(CSVReader csvReader, WorkbookReader reader) throws WorkingException {
        // loop and process handler
        WorkbookHandler handler = reader.get(0);
        // check handler's type
        if (!accept(handler)) return;

        try {
            // do init
            doInit(csvReader, handler);
            // process csv headers
            processHeaders(handler, csvReader);
            // process per data
            boolean success = processPerRowData(handler, csvReader);
            // do finish
            if (success) handler.doFinish();
            /* 工作本后置处理事件 */
            WorkbookPostHandleListener postHandleListener = reader.getWorkbookPostHandleListener();
            if (postHandleListener != null) {
                postHandleListener.postProcess(new WorkbookHandleEvent(new WorkPage(0, null)));
            }
        } finally {
            // do finally
            handler.doFinally();
        }
    }

    protected void processHeaders(WorkbookHandler handler, CSVReader csvReader) throws WorkingException {
        WorkbookHeader header = handler.getHeader();
        int rawHeaderCount = header.getRawHeaderCount();

        for (int i = 0; i < rawHeaderCount; i++) {
            String[] headerData = null;

            try {
                headerData = csvReader.readNext();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

            if (headerData == null) {
                logger.error("No data of head was resolved.");

                headerData = new String[0];
            }

            header.addHeader(
                Stream.of(headerData)
                    .map(DefaultWorkData::new)
                    .collect(Collectors.toList())
            );
        }
    }

    /**
     * 判断<code>WorkbookHandler</code>
     * 类型是否与需要处理的一致
     *
     * @param handler <code>WorkbookHandler</code>
     * @return boolean值
     */
    abstract protected boolean accept(WorkbookHandler handler);

    /**
     * 处理sheet的每一行数据的方法
     *
     * @param handler   <code>WorkbookHandler</code>
     * @param csvReader <code>CSVReader</code>
     * @return 表示方法是否成功执行
     * @throws WorkingException 处理异常
     */
    abstract protected boolean processPerRowData(WorkbookHandler handler, CSVReader csvReader) throws WorkingException;
}
