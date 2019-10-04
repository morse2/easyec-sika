package com.googlecode.easyec.sika.csv;

import com.googlecode.easyec.sika.WorkbookProcess;
import com.googlecode.easyec.sika.WorkbookWriter;
import com.googlecode.easyec.sika.WorkingException;
import com.opencsv.CSVWriter;

public interface CSVWriteProcess extends WorkbookProcess {

    void write(CSVWriter csvWriter, WorkbookWriter writer) throws WorkingException;
}
