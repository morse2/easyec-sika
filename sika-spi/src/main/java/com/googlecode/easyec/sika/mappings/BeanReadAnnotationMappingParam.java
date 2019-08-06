package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import org.springframework.beans.BeanWrapper;

import java.util.List;

public class BeanReadAnnotationMappingParam extends BeanAnnotationMappingParam {

    private List<WorkData> dataList;

    public BeanReadAnnotationMappingParam(BeanWrapper parameter, List<WorkData> dataList, MappingsException exception) {
        super(parameter, exception);
        this.dataList = dataList;
    }

    public List<WorkData> getDataList() {
        return dataList;
    }

    @Override
    public void resolved() {
        getParameter().setPropertyValue(
            getCurrentProperty().getName(),
            getResolvedValue()
        );
    }

    @Override
    public void computeOriginalValue(String column) throws UnknownColumnException {
        setOriginalValue(getParameter().getWrappedInstance());
    }
}
