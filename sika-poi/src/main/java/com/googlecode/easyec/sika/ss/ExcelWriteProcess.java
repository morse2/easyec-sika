package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.WorkbookProcess;
import com.googlecode.easyec.sika.WorkbookWriter;
import com.googlecode.easyec.sika.WorkingException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.OutputStream;

public interface ExcelWriteProcess extends WorkbookProcess {

    void write(Workbook wb, WorkbookWriter writer, OutputStream out) throws WorkingException;
}
