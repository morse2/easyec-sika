package com.googlecode.easyec.sika.csv.impl.internal;

import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkbookCallback;
import com.googlecode.easyec.sika.WorkbookRowCallback;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.csv.impl.AbstractCSVWriteProcess;
import com.googlecode.easyec.sika.mappings.*;
import com.opencsv.CSVWriter;

import java.beans.PropertyDescriptor;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

public class DefaultCSVRowDataWriteProcess extends AbstractCSVWriteProcess {

    @Override
    protected boolean accept(WorkbookCallback callback) {
        return callback instanceof WorkbookRowCallback;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean processPerRowData(WorkbookCallback callback, CSVWriter csvWriter) throws WorkingException {
        if (callback instanceof AnnotationWorkbookRowCallback) {
            AnnotationWorkbookRowCallback<Object> anCallback = (AnnotationWorkbookRowCallback<Object>) callback;
            BeanWriteMappingParamResolver resolver = anCallback.getBeanWriteMappingParamResolver();
            if (resolver == null) {
                anCallback.setBeanWriteMappingParamResolver(createBeanMappingParamResolver());
            }
        }

        // prepare to csv data
        List<?> ts = callback.doGrab();
        if (!isEmpty(ts)) {
            for (int i = 0; i < ts.size(); i++) {
                try {
                    List<WorkData> data = callback.populate(i, ts.get(i));
                    if (data == null) {
                        throw new WorkingException("List of WorkData mustn't be null.", true);
                    }

                    csvWriter.writeNext(
                        data.stream()
                            .map(this::getValue)
                            .toArray(String[]::new)
                    );
                } catch (WorkingException e) {
                    callback.doCatch(e);

                    if (e.isStop()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    protected BeanWriteMappingParamResolver createBeanMappingParamResolver() {
        return new BeanWriteMappingParamResolver(createAnnotationMappingResolverChain());
    }

    @SuppressWarnings("unchecked")
    protected AnnotationMappingResolverChain<BeanPropertyWriteAnnotationMappingParam, PropertyDescriptor> createAnnotationMappingResolverChain() {
        return new AnnotationMappingResolverChain<>(
            new ColumnWriteMappingParamResolver(),
            new ColumnMappingParamResolver()
        );
    }
}
