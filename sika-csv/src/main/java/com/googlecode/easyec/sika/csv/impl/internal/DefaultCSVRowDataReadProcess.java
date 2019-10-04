package com.googlecode.easyec.sika.csv.impl.internal;

import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkbookHandler;
import com.googlecode.easyec.sika.WorkbookRowHandler;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.csv.impl.AbstractCSVReadProcess;
import com.googlecode.easyec.sika.event.RowEvent;
import com.googlecode.easyec.sika.event.WorkbookBlankRowListener;
import com.googlecode.easyec.sika.mappings.*;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.ArrayUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultCSVRowDataReadProcess extends AbstractCSVReadProcess {

    @Override
    protected boolean accept(WorkbookHandler handler) {
        return handler instanceof WorkbookRowHandler;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean processPerRowData(WorkbookHandler handler, CSVReader csvReader) throws WorkingException {
        if (handler instanceof AnnotationWorkbookRowHandler) {
            AnnotationWorkbookRowHandler anHandler = (AnnotationWorkbookRowHandler) handler;
            AbstractBeanMappingParamResolver resolver = anHandler.getBeanReadMappingParamResolver();
            if (resolver == null) {
                anHandler.setBeanReadMappingParamResolver(createBeanMappingParamResolver());
            }
        }

        int hc = handler.getHeader().getHeaderCount();
        for (int i = hc; ; i++) {
            String[] line;

            try {
                line = csvReader.readNext();
                if (ArrayUtils.isEmpty(line)) break;
            } catch (IOException e) {
                WorkingException ex = new WorkingException(e, true);
                handler.doCatch(ex);
                throw ex;
            }

            List<WorkData> list = new ArrayList<>();

            for (int j = 0; j < line.length; j++) {
                list.add(WorkData.createCellWorkData(line[j], i, j));
            }

            WorkbookBlankRowListener blankRowListeners = ((WorkbookRowHandler) handler).getBlankRowListeners();
            if (blankRowListeners != null) {
                if (!blankRowListeners.accept(new RowEvent(list, i))) break;
            }

            try {
                if (!handler.populate(Collections.singletonMap(i, list))) break;
            } catch (WorkingException e) {
                handler.doCatch(e);
                if (e.isStop()) return false;
            }
        }

        return true;
    }

    protected BeanReadMappingParamResolver createBeanMappingParamResolver() {
        return new BeanReadMappingParamResolver(createAnnotationMappingResolverChain());
    }

    @SuppressWarnings("unchecked")
    protected AnnotationMappingResolverChain<? extends BeanPropertyAnnotationMappingParam, PropertyDescriptor> createAnnotationMappingResolverChain() {
        return new AnnotationMappingResolverChain<>(
            new ColumnReadMappingParamResolver(),
            new ColumnMappingParamResolver()
        );
    }
}
