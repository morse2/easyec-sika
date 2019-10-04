package com.googlecode.easyec.sika.csv.impl;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.converters.DateFormatColumnConverter;
import com.googlecode.easyec.sika.converters.NumberFormatColumnConverter;
import com.googlecode.easyec.sika.csv.CSVWriteProcess;
import com.opencsv.CSVWriter;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public abstract class AbstractCSVWriteProcess extends AbstractCSVProcess implements CSVWriteProcess {

    @Override
    public void write(CSVWriter csvWriter, WorkbookWriter writer) throws WorkingException {
        // do write header of csv
        // only get first WorkbookWriter object
        WorkbookCallback callback = writer.get(0);
        // check callback's type
        if (!accept(callback)) return;

        try {
            // do init
            doInit(csvWriter, callback);
            // process headers
            processHeaders(csvWriter, callback);
            // process csv data
            boolean success = processPerRowData(callback, csvWriter);
            // do finish
            if (success) callback.doFinish();
        } finally {
            callback.doFinally();
        }
    }

    protected void processHeaders(CSVWriter csvWriter, WorkbookCallback callback) throws WorkingException {
        // write headers
        WorkbookHeader header = callback.getHeader();
        if (header == null) return;

        List<WorkData[]> headerList = header.getHeaderList();
        if (headerList == null) return;

        for (WorkData[] data : headerList) {
            csvWriter.writeNext(
                Stream.of(data)
                    .map(this::getValue)
                    .toArray(String[]::new)
            );
        }
    }

    protected String getValue(WorkData d) {
        switch (d.getWorkDataType()) {
            case DATE:
                return createDateConverter().adorn((Date) d.getValue());
            case NUMBER:
                return createNumberConverter().adorn((Number) d.getValue());
            case STRING:
                return (String) d.getValue();
        }

        return EMPTY;
    }

    protected ColumnConverter<Date, String> createDateConverter() {
        return new DateFormatColumnConverter();
    }

    protected ColumnConverter<Number, String> createNumberConverter() {
        return new NumberFormatColumnConverter();
    }

    /**
     * 判断<code>WorkbookCallback</code>
     * 类型是否与需要处理的一致
     *
     * @param callback <code>WorkbookCallback</code>
     * @return boolean值
     */
    abstract protected boolean accept(WorkbookCallback callback);

    /**
     * 处理sheet的每一行数据的方法
     *
     * @param callback  <code>WorkbookCallback</code>
     * @param csvWriter <code>CSVWriter</code>
     * @return 表示方法是否成功执行
     * @throws WorkingException 处理异常
     */
    abstract protected boolean processPerRowData(WorkbookCallback callback, CSVWriter csvWriter) throws WorkingException;
}
