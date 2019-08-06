package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.data.DefaultWorkData;
import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;
import java.util.Map;

import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.calculateColIndex;

public class BeanPropertyWriteAnnotationMappingParam extends BeanPropertyAnnotationMappingParam {

    private Map<Integer, WorkData> dataMap;
    private int columnIndex;

    public BeanPropertyWriteAnnotationMappingParam(BeanWrapper beanWrapper, PropertyDescriptor parameter, Map<Integer, WorkData> dataMap, MappingsException ex) {
        super(beanWrapper, parameter, ex);
        this.dataMap = dataMap;
    }

    public Map<Integer, WorkData> getDataMap() {
        return dataMap;
    }

    protected int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public void resolved() {
        getDataMap().put(
            getColumnIndex(),
            new DefaultWorkData(getResolvedValue())
        );
    }

    @Override
    public void computeOriginalValue(String column) throws UnknownColumnException {
        int colIndex = calculateColIndex(column);
        logger.debug("Column name of index: [{}], [{}]. Property: [{}].", column, colIndex, getPropertyName());

        this.columnIndex = colIndex;
        setOriginalValue(getBeanWrapper().getPropertyValue(getPropertyName()));
    }
}
