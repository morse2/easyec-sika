package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.WorkbookProcess;
import com.googlecode.easyec.sika.WorkbookWriter;
import com.googlecode.easyec.sika.WorkingException;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelWriteProcess extends WorkbookProcess {

    byte[] write(Workbook wb, WorkbookWriter writer) throws WorkingException;
}
