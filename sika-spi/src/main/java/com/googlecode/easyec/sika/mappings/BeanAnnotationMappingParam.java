package com.googlecode.easyec.sika.mappings;

import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public abstract class BeanAnnotationMappingParam extends DataValueAnnotationMappingParam<BeanWrapper> {

    private MappingsException exception;
    private PropertyDescriptor currentProperty;

    public BeanAnnotationMappingParam(BeanWrapper parameter, MappingsException exception) {
        super(parameter);
        this.exception = exception;
    }

    public MappingsException getException() {
        return exception;
    }

    public PropertyDescriptor getCurrentProperty() {
        return currentProperty;
    }

    public void setCurrentProperty(PropertyDescriptor currentProperty) {
        this.currentProperty = currentProperty;
    }

    public boolean isReadableProperty() {
        return getParameter().isReadableProperty(getPropertyName());
    }

    @Override
    protected Method getMethod() {
        return getCurrentProperty().getReadMethod();
    }

    @Override
    public String getPropertyName() {
        return getCurrentProperty().getName();
    }
}
