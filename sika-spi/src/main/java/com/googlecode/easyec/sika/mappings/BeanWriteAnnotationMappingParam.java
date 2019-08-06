package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import org.springframework.beans.BeanWrapper;

import java.util.Map;

public class BeanWriteAnnotationMappingParam extends BeanAnnotationMappingParam {

    private Map<Integer, WorkData> dataMap;

    public BeanWriteAnnotationMappingParam(BeanWrapper parameter, Map<Integer, WorkData> dataMap, MappingsException exception) {
        super(parameter, exception);
        this.dataMap = dataMap;
    }

    public Map<Integer, WorkData> getDataMap() {
        return dataMap;
    }

    @Override
    public void resolved() {
        // no op
    }

    @Override
    public void computeOriginalValue(String column) throws UnknownColumnException {
        // no op
    }
}
