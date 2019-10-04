package com.googlecode.easyec.sika.csv;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.csv.impl.internal.DefaultCSVRowDataReadProcess;
import com.googlecode.easyec.sika.csv.impl.internal.DefaultCSVRowDataWriteProcess;
import com.opencsv.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;

/**
 * CSV文本操作的工厂类。
 * <p>
 * 该工厂类提供了处理CSV文件的一般方法
 * </p>
 *
 * @author JunJie
 */
public final class CsvFactory {

    private static final Logger logger = LoggerFactory.getLogger(CsvFactory.class);
    private static final ThreadLocal<CsvFactory> local = new ThreadLocal<>();

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

    public void readLines(Reader reader, WorkbookRowHandler handler) throws WorkingException {
        readLines(reader, handler, null);
    }

    public void readLines(Reader reader, WorkbookRowHandler handler, CSVParser csvParser) throws WorkingException {
        read(reader, new DefaultCSVRowDataReadProcess(), handler, csvParser);
    }

    public void read(Reader reader, CSVReadProcess process, WorkbookHandler handler) throws WorkingException {
        read(reader, process, handler, new CSVParserBuilder().build());
    }

    public void read(Reader reader, CSVReadProcess process, WorkbookHandler handler, CSVParser csvParser) throws WorkingException {
        if (csvParser == null) csvParser = new CSVParserBuilder().build();

        WorkbookReader ww = new WorkbookReader();
        ww.add(handler);

        doRead(reader, ww, process, csvParser);
    }

    private void doRead(Reader reader, WorkbookReader ww, CSVReadProcess proc, CSVParser csvParser) throws WorkingException {
        CSVReader csvReader = null;

        try {
            assertEmptyWorkbookHandler(ww);

            csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(csvParser)
                .build();

            proc.read(csvReader, ww);
        } catch (WorkingException e) {
            throw e;
        } catch (Exception e) {
            throw new WorkingException(e, true);
        } finally {
            IOUtils.closeQuietly(csvReader);
        }
    }

    public <T> void writeLines(Writer writer, WorkbookRowCallback<T> callback) throws WorkingException {
        writeLines(writer, callback, null);
    }

    public <T> void writeLines(Writer writer, WorkbookRowCallback<T> callback, CSVParser csvParser) throws WorkingException {
        writeLines(writer, new DefaultCSVRowDataWriteProcess(), callback, csvParser);
    }

    public <T> void writeLines(Writer writer, CSVWriteProcess process, WorkbookRowCallback<T> callback) throws WorkingException {
        writeLines(writer, process, callback, null);
    }

    public <T> void writeLines(Writer writer, CSVWriteProcess process, WorkbookRowCallback<T> callback, CSVParser csvParser) throws WorkingException {
        write(writer, process, callback, csvParser);
    }

    public <T> void write(Writer writer, CSVWriteProcess process, WorkbookCallback<T> callback) throws WorkingException {
        write(writer, process, callback, null);
    }

    public <T> void write(Writer writer, CSVWriteProcess process, WorkbookCallback<T> callback, CSVParser csvParser) throws WorkingException {
        if (csvParser == null) csvParser = new CSVParserBuilder().build();

        WorkbookWriter ww = new WorkbookWriter();
        ww.add(callback);

        doWrite(writer, ww, process, csvParser);
    }

    private void doWrite(Writer writer, WorkbookWriter ww, CSVWriteProcess proc, CSVParser csvParser) throws WorkingException {
        CSVWriter csvWriter = null;

        try {
            assertEmptyWorkbookHandler(ww);

            csvWriter = new CSVWriter(writer,
                csvParser.getSeparator(),
                csvParser.getQuotechar(),
                csvParser.getEscape()
            );

            proc.write(csvWriter, ww);
            csvWriter.flushQuietly();
        } catch (WorkingException e) {
            throw e;
        } catch (Exception e) {
            throw new WorkingException(e, true);
        } finally {
            IOUtils.closeQuietly(csvWriter);
        }
    }

    private void assertEmptyWorkbookHandler(com.googlecode.easyec.sika.Workbook workbook) throws WorkingException {
        if (!workbook.hasMore()) {
            throw new WorkingException("No Workbook was added.", true);
        }
    }
}
