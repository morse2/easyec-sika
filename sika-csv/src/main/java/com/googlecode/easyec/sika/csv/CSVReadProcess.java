package com.googlecode.easyec.sika.csv;

import com.googlecode.easyec.sika.WorkbookProcess;
import com.googlecode.easyec.sika.WorkbookReader;
import com.googlecode.easyec.sika.WorkingException;
import com.opencsv.CSVReader;

public interface CSVReadProcess extends WorkbookProcess {

    void read(CSVReader csvReader, WorkbookReader reader) throws WorkingException;
}
