package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

public class BeanAnnotationMappingParam extends DataValueAnnotationMappingParam<BeanWrapper> {

    private PropertyDescriptor currentProperty;

    public BeanAnnotationMappingParam(BeanWrapper parameter, List<WorkData> dataList, MappingsException exception) {
        super(parameter, dataList, exception);
    }

    public PropertyDescriptor getCurrentProperty() {
        return currentProperty;
    }

    public void setCurrentProperty(PropertyDescriptor currentProperty) {
        this.currentProperty = currentProperty;
    }

    public String getPropertyName() {
        return getCurrentProperty().getName();
    }

    public boolean isReadableProperty() {
        return getParameter().isReadableProperty(getPropertyName());
    }

    @Override
    public void resolved() {
        getParameter().setPropertyValue(
            getCurrentProperty().getName(),
            getResolvedValue()
        );
    }

    @Override
    protected Method getMethod() {
        return getCurrentProperty().getReadMethod();
    }
}
