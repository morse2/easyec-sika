package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;
import java.util.List;

import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.calculateColIndex;

public class BeanPropertyReadAnnotationMappingParam extends BeanPropertyAnnotationMappingParam {

    private List<WorkData> dataList;

    public BeanPropertyReadAnnotationMappingParam(BeanWrapper beanWrapper, PropertyDescriptor parameter, List<WorkData> dataList, MappingsException ex) {
        super(beanWrapper, parameter, ex);
        this.dataList = dataList;
    }

    public List<WorkData> getDataList() {
        return dataList;
    }

    @Override
    public void resolved() {
        getBeanWrapper().setPropertyValue(
            getPropertyName(),
            getResolvedValue()
        );
    }

    @Override
    public void computeOriginalValue(String column) throws UnknownColumnException {
        int colIndex = calculateColIndex(column);
        logger.debug("Column name of index: [{}], [{}]. Property: [{}].", column, colIndex, getPropertyName());

        Object val = (colIndex < getDataList().size()) ? getDataList().get(colIndex).getValue() : null;
        logger.debug("The original value is: [{}], property: [{}].", val, getPropertyName());

        setOriginalValue(val);
    }
}
