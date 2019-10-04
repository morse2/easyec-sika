package com.googlecode.easyec.sika.csv.impl;

import com.googlecode.easyec.sika.WorkbookProcess;
import com.googlecode.easyec.sika.WorkbookProcessAware;
import com.googlecode.easyec.sika.WorkingException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.googlecode.easyec.sika.DocType.CSV;

public abstract class AbstractCSVProcess implements WorkbookProcess {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected void doInit(CSVReader csvReader, WorkbookProcessAware aware) throws WorkingException {
        try {
            doInit(aware);

            aware.getStrategy().setDocType(CSV);
        } catch (WorkingException e) {
            logger.error(e.getMessage(), e);

            if (e.isStop()) throw e;
        }
    }

    protected void doInit(CSVWriter csvWriter, WorkbookProcessAware aware) throws WorkingException {
        try {
            doInit(aware);

            aware.getStrategy().setDocType(CSV);
        } catch (WorkingException e) {
            logger.error(e.getMessage(), e);

            if (e.isStop()) throw e;
        }
    }
}
