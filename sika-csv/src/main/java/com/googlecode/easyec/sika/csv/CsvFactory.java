package com.googlecode.easyec.sika.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.converters.Object2StringConverter;
import com.googlecode.easyec.sika.event.RowEvent;
import com.googlecode.easyec.sika.event.WorkbookBlankRowListener;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
 * Created with IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-4-25
 * Time: 下午3:31
 * To change this template use File | Settings | File Templates.
 */
public final class CsvFactory {

    private static final ThreadLocal<CsvFactory> local = new ThreadLocal<CsvFactory>();
    private static final Logger logger = LoggerFactory.getLogger(CsvFactory.class);

    private CsvFactory() {
    }

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

    public <T> void write(Writer w, WorkbookWriter<T> ww) throws WorkingException {
        doWrite(w, ww, DEFAULT);
    }

    public <T> void write(Writer w, WorkbookWriter<T> ww, CsvSchema schema) throws WorkingException {
        doWrite(w, ww, schema);
    }

    private <T> void doWrite(Writer w, WorkbookWriter<T> ww, CsvSchema schema) throws WorkingException {
        CSVWriter writer = new CSVWriter(
                w,
                schema.getSeparator(),
                schema.getQuotechar(),
                schema.getEscape()
        );

        // do write header of csv
        // only get first WorkbookWriter object
        if (ww.hasMore()) {
            WorkbookCallback<T> callback = ww.get(0);

            callback.setDocType(CSV);

            try {
                // call init method.
                callback.doInit();

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

                List<T> ts = callback.doGrab();

                boolean needDoFinish = true;
                if (!isEmpty(ts)) {
                    for (T t : ts) {
                        try {
                            List<WorkData> data = callback.populate(t);
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

    public void read(Reader reader, WorkbookReader workbook) throws WorkingException {
        doRead(reader, workbook, DEFAULT);
    }

    @Deprecated
    public void read(Reader reader, WorkbookReader workbookReader, char separator) throws WorkingException {
        doRead(reader, workbookReader, new CsvSchema(separator));
    }

    public void read(Reader reader, WorkbookReader workbookReader, CsvSchema schema) throws WorkingException {
        doRead(reader, workbookReader, schema == null ? DEFAULT : schema);
    }

    private void doRead(Reader reader, WorkbookReader workbookReader, CsvSchema schema) throws WorkingException {
        if (!workbookReader.hasMore()) {
            throw new WorkingException("No WorkbookHandler was added.", true);
        }

        WorkbookHandler handler = workbookReader.get(0);
        handler.setDocType(CSV);

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

            handler.doInit();

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
                        boolean isAllNull = true;

                        try {
                            for (int j = 0; j < line.length; j++) {
                                String val = line[j];
                                if (StringUtils.isNotBlank(val)) {
                                    isAllNull = false;
                                }

                                list.add(WorkData.createCellWorkData(val, i, j));
                            }

                            if (isAllNull) {
                                WorkbookBlankRowListener blankRowListeners = rowHandler.getBlankRowListeners();
                                if (blankRowListeners != null) {
                                    boolean b = blankRowListeners.accept(new RowEvent(list, i));
                                    if (!b) continue;
                                }
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
