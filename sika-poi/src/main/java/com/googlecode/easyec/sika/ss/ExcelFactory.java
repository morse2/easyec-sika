package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.event.WorkbookHandlerChangeListener;
import com.googlecode.easyec.sika.event.WorkbookPostHandleListener;
import com.googlecode.easyec.sika.ss.impl.internal.DefaultExcelRowDataReadProcess;
import com.googlecode.easyec.sika.ss.impl.internal.DefaultExcelRowDataWriteProcess;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Excel文档处理工厂类。
 *
 * @author JunJie
 */
public final class ExcelFactory {

    private ExcelFactory() { /* no op */ }

    public static ExcelFactory getInstance() {
        return new ExcelFactory();
    }

    @Deprecated
    public void read(File file, WorkbookReader reader) throws WorkingException {
        try {
            read(new FileInputStream(file), reader);
        } catch (FileNotFoundException e) {
            throw new WorkingException(e, true);
        }
    }

    @Deprecated
    public void read(InputStream in, WorkbookReader reader) throws WorkingException {
        doRead(in, reader, new DefaultExcelRowDataReadProcess());
    }

    public void read(InputStream in, ExcelReadProcess proc, WorkbookRowHandler... handlers) throws WorkingException {
        read(in, proc, null, null, handlers);
    }

    public void read(InputStream in, ExcelReadProcess proc, WorkbookHandlerChangeListener lsnr, WorkbookRowHandler... handlers) throws WorkingException {
        read(in, proc, lsnr, null, handlers);
    }

    public void read(InputStream in, ExcelReadProcess proc, WorkbookPostHandleListener lsnr, WorkbookRowHandler... handlers) throws WorkingException {
        read(in, proc, null, lsnr, handlers);
    }

    public void read(InputStream in, ExcelReadProcess proc, WorkbookHandlerChangeListener lsnr1, WorkbookPostHandleListener lsnr2, WorkbookRowHandler... handlers) throws WorkingException {
        Assert.isTrue(isNotEmpty(handlers), "WorkbookRowHandler should provide one or more at least.");
        Assert.notNull(proc, "ExcelReadProcess must be provided.");

        final WorkbookReader reader = new WorkbookReader();
        Stream.of(handlers).forEach(reader::add);

        // set listeners
        reader.setWorkbookHandlerChangeListener(lsnr1);
        reader.setWorkbookPostHandleListener(lsnr2);

        doRead(in, reader, proc);
    }

    public void readLines(InputStream in, WorkbookRowHandler... handlers) throws WorkingException {
        readLines(in, null, null, handlers);
    }

    public void readLines(InputStream in, WorkbookHandlerChangeListener lsnr, WorkbookRowHandler... handlers) throws WorkingException {
        readLines(in, lsnr, null, handlers);
    }

    public void readLines(InputStream in, WorkbookPostHandleListener lsnr, WorkbookRowHandler... handlers) throws WorkingException {
        readLines(in, null, lsnr, handlers);
    }

    public void readLines(InputStream in, WorkbookHandlerChangeListener lsnr1, WorkbookPostHandleListener lsnr2, WorkbookRowHandler... handlers) throws WorkingException {
        read(in, new DefaultExcelRowDataReadProcess(), lsnr1, lsnr2, handlers);
    }

    @Deprecated
    public void write(InputStream in, OutputStream out, WorkbookWriter writer) throws WorkingException {
        Assert.notNull(out, "OutputStream object is null.");

        try {
            IOUtils.write(write(in, writer), out);
        } catch (IOException e) {
            throw new WorkingException(e, true);
        }
    }

    @Deprecated
    public byte[] write(InputStream in, WorkbookWriter writer) throws WorkingException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        doWrite(in, bos, writer, new DefaultExcelRowDataWriteProcess());
        return bos.toByteArray();
    }

    public byte[] writeLines(InputStream in, WorkbookCallback<?>... cbs) throws WorkingException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        writeLines(in, bos, cbs);
        return bos.toByteArray();
    }

    public void writeLines(InputStream in, OutputStream out, WorkbookCallback<?>... cbs) throws WorkingException {
        write(in, out, new DefaultExcelRowDataWriteProcess(), cbs);
    }

    public void write(InputStream in, OutputStream out, ExcelWriteProcess proc, WorkbookCallback<?>... cbs) throws WorkingException {
        Assert.isTrue(isNotEmpty(cbs), "WorkbookCallback should provide one or more at least.");
        Assert.notNull(proc, "ExcelWriteProcess must be provided.");

        final WorkbookWriter writer = new WorkbookWriter();
        Stream.of(cbs).forEach(writer::add);

        doWrite(in, out, writer, proc);
    }

    private void doRead(InputStream in, WorkbookReader reader, ExcelReadProcess proc) throws WorkingException {
        Workbook wb = null;

        try {
            assertEmptyWorkbookHandler(reader);

            wb = WorkbookFactory.create(in);

            proc.read(wb, reader);
        } catch (WorkingException e) {
            throw e;
        } catch (Exception e) {
            throw new WorkingException(e, true);
        } finally {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(in);
        }
    }

    private void doWrite(InputStream in, OutputStream out, WorkbookWriter writer, ExcelWriteProcess proc) throws WorkingException {
        Workbook wb = null;

        try {
            assertEmptyWorkbookHandler(writer);
            wb = WorkbookFactory.create(in);
            proc.write(wb, writer, out);
        } catch (WorkingException e) {
            throw e;
        } catch (Exception e) {
            throw new WorkingException(e, true);
        } finally {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(in);
        }
    }

    private void assertEmptyWorkbookHandler(com.googlecode.easyec.sika.Workbook workbook) throws WorkingException {
        if (!workbook.hasMore()) {
            throw new WorkingException("No Workbook was added.", true);
        }
    }
}
