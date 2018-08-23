package com.googlecode.easyec.sika.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.converters.Object2StringConverter;
import com.googlecode.easyec.sika.event.RowEvent;
import com.googlecode.easyec.sika.event.WorkbookBlankRowListener;
import com.googlecode.easyec.sika.event.WorkbookHandleEvent;
import com.googlecode.easyec.sika.event.WorkbookPostHandleListener;
import com.googlecode.easyec.sika.support.WorkbookStrategy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.easyec.sika.DocType.CSV;
import static com.googlecode.easyec.sika.csv.CsvSchema.DEFAULT;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * CSV文本操作的工厂类。
 * <p>
 * 该工厂类提供了处理CSV文件的一般方法
 * </p>
 *
 * @author JunJie
 */
public final class CsvFactory {

    private static final ThreadLocal<CsvFactory> local = new ThreadLocal<CsvFactory>();
    private static final Logger logger = LoggerFactory.getLogger(CsvFactory.class);

    private CsvFactory() { }

    /**
     * 获得当前工厂类的实例对象。
     * 该实例对象是线程安全的。
     *
     * @return <code>CsvFactory</code>对象
     */
    public static CsvFactory getInstance() {
        synchronized (local) {
            CsvFactory factory = local.get();
            if (factory == null) {
                factory = new CsvFactory();
                local.set(factory);
            }

            return factory;
        }
    }

    /**
     * 写方法。
     * <p>
     * 此方法往给定的输出流中写入文本内容。
     * </p>
     *
     * @param w  输出流对象
     * @param ww 工作本写出器对象
     * @throws WorkingException
     * @see #write(Writer, WorkbookWriter)
     */
    public void write(Writer w, WorkbookWriter ww) throws WorkingException {
        doWrite(w, ww, DEFAULT);
    }

    /**
     * 写方法。
     * <p>
     * 此方法往给定的输出流中写入文本内容，
     * 并且可以定义CSV文件内容格式的概要信息。
     * </p>
     *
     * @param w      输出流对象
     * @param ww     工作本写出器对象
     * @param schema CSV文件内容概要信息对象
     * @throws WorkingException
     * @see #write(Writer, WorkbookWriter)
     */
    public void write(Writer w, WorkbookWriter ww, CsvSchema schema) throws WorkingException {
        doWrite(w, ww, schema);
    }

    @SuppressWarnings("unchecked")
    private void doWrite(Writer w, WorkbookWriter ww, CsvSchema schema) throws WorkingException {
        CSVWriter writer = new CSVWriter(
            w,
            schema.getSeparator(),
            schema.getQuotechar(),
            schema.getEscape(),
            schema.getLineEnd()
        );

        // do write header of csv
        // only get first WorkbookWriter object
        if (ww.hasMore()) {
            WorkbookCallback<Object> callback = (WorkbookCallback<Object>) ww.get(0);

            try {
                try {
                    // call init method.
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

                strategy.setDocType(CSV);

                // write headers
                WorkbookHeader header = callback.getHeader();
                List<WorkData[]> headerList = header.getHeaderList();

                for (WorkData[] data : headerList) {
                    List<String> list = new ArrayList<String>();

                    for (WorkData d : data) {
                        switch (d.getWorkDataType()) {
                            case DATE:
                                list.add(d.getValue(schema.getDateColumnConverter()));
                                break;
                            case NUMBER:
                                list.add(d.getValue(new Object2StringConverter()));
                                break;
                            case STRING:
                                list.add((String) d.getValue());
                                break;
                            case NULL:
                            default:
                                list.add("");
                        }
                    }

                    writer.writeNext(list.toArray(new String[list.size()]));
                }

                List<Object> ts = callback.doGrab();

                boolean needDoFinish = true;
                if (!isEmpty(ts)) {
                    for (int i = 0; i < ts.size(); i++) {
                        try {
                            List<WorkData> data = callback.populate(i, ts.get(i));
                            List<String> list = new ArrayList<String>();

                            for (WorkData d : data) {
                                switch (d.getWorkDataType()) {
                                    case DATE:
                                        list.add(d.getValue(schema.getDateColumnConverter()));
                                        break;
                                    case NUMBER:
                                        list.add(d.getValue(new Object2StringConverter()));
                                        break;
                                    case STRING:
                                        list.add((String) d.getValue());
                                        break;
                                    case NULL:
                                    default:
                                        list.add("");
                                }
                            }

                            writer.writeNext(list.toArray(new String[list.size()]));
                        } catch (WorkingException e) {
                            callback.doCatch(e);

                            if (e.isStop()) {
                                needDoFinish = false;
                                break;
                            }
                        }
                    }
                }

                if (needDoFinish) callback.doFinish();
                w.flush();
            } catch (WorkingException e) {
                callback.doCatch(e);

                throw e;
            } catch (IOException e) {
                throw new WorkingException(e, true);
            } finally {
                callback.doFinally();
                IOUtils.closeQuietly(w);
            }
        }
    }

    /**
     * 读方法。该方法用以读取CSV文件的内容。
     *
     * @param reader   读入流对象
     * @param workbook 工作本读取器对象
     * @throws WorkingException
     */
    public void read(Reader reader, WorkbookReader workbook) throws WorkingException {
        doRead(reader, workbook, DEFAULT);
    }

    /**
     * 读方法。该方法用以读取CSV文件的内容。
     * <p>
     * 该方法同时可以定义CSV文件内容概要信息。
     * </p>
     *
     * @param reader         读入流对象
     * @param workbookReader 工作本读取器对象
     * @param schema         CSV文件内容概要信息对象
     * @throws WorkingException
     */
    public void read(Reader reader, WorkbookReader workbookReader, CsvSchema schema) throws WorkingException {
        doRead(reader, workbookReader, schema == null ? DEFAULT : schema);
    }

    private void doRead(Reader reader, WorkbookReader workbookReader, CsvSchema schema) throws WorkingException {
        if (!workbookReader.hasMore()) {
            throw new WorkingException("No WorkbookHandler was added.", true);
        }

        WorkbookHandler handler = workbookReader.get(0);

        try {
            CSVReader csvReader = new CSVReader(
                reader,
                schema.getSeparator(),
                schema.getQuotechar(),
                schema.getEscape(),
                0,
                schema.isStrictQuotes(),
                schema.isIgnoreLeadingWhiteSpace()
            );

            try {
                handler.doInit();
            } catch (WorkingException e) {
                logger.error(e.getMessage(), e);

                if (e.isStop()) throw e;
            }

            // 获取策略对象
            WorkbookStrategy strategy = handler.getStrategy();
            if (strategy == null) {
                strategy = WorkbookStrategy.DEFAULT;
                handler.setStrategy(strategy);
            }

            strategy.setDocType(CSV);

            try {
                if (handler instanceof WorkbookRowHandler) {
                    WorkbookRowHandler rowHandler = (WorkbookRowHandler) handler;

                    WorkbookHeader header = rowHandler.getHeader();
                    int rawHeaderCount = header.getRawHeaderCount();

                    for (int i = 0; i < rawHeaderCount; i++) {
                        String[] headerData = csvReader.readNext();
                        if (headerData == null) {
                            logger.debug("No data of head was resolved.");

                            break;
                        }

                        List<WorkData> data = new ArrayList<WorkData>();
                        try {
                            for (int j = 0; j < headerData.length; j++) {
                                data.add(WorkData.createCellWorkData(headerData[j], i, j));
                            }

                            header.addHeader(data);
                        } finally {
                            data.clear();
                            data = null;
                        }
                    }

                    String[] line;
                    for (int i = header.getHeaderCount(); (line = csvReader.readNext()) != null; i++) {
                        List<WorkData> list = new ArrayList<WorkData>();

                        try {
                            for (int j = 0; j < line.length; j++) {
                                list.add(WorkData.createCellWorkData(line[j], i, j));
                            }

                            WorkbookBlankRowListener blankRowListeners = rowHandler.getBlankRowListeners();
                            if (blankRowListeners != null) {
                                boolean b = blankRowListeners.accept(new RowEvent(list, i));
                                if (!b) break;
                            }

                            Map<Integer, List<WorkData>> map = new LinkedHashMap<Integer, List<WorkData>>();
                            map.put(i, list);

                            try {
                                if (!rowHandler.populate(map)) break;
                            } finally {
                                if (map != null) {
                                    map.clear();
                                    map = null;
                                }
                            }
                        } catch (WorkingException e) {
                            handler.doCatch(e);
                            if (e.isStop()) {
                                break;
                            }
                        } finally {
                            list.clear();
                            list = null;
                        }
                    }
                }
            } finally {
                csvReader.close();
            }

            handler.doFinish();

            /* 工作本后置处理事件 */
            WorkbookPostHandleListener postHandleListener = workbookReader.getWorkbookPostHandleListener();
            if (postHandleListener != null) {
                postHandleListener.postProcess(new WorkbookHandleEvent(new WorkPage(0, null)));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            if (e instanceof WorkingException) {
                throw (WorkingException) e;
            }

            throw new WorkingException(e, true);
        } finally {
            handler.doFinally();

            IOUtils.closeQuietly(reader);
        }
    }
}
