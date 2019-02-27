package com.googlecode.easyec.sika.ss;

import com.googlecode.easyec.sika.WorkbookProcess;
import com.googlecode.easyec.sika.WorkbookReader;
import com.googlecode.easyec.sika.WorkingException;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelReadProcess extends WorkbookProcess {

    void read(Workbook wb, WorkbookReader reader) throws WorkingException;
}
