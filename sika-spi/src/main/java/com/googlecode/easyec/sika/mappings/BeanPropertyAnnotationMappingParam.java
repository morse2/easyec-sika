package com.googlecode.easyec.sika.mappings;

import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public abstract class BeanPropertyAnnotationMappingParam extends DataValueAnnotationMappingParam<PropertyDescriptor> {

    private BeanWrapper beanWrapper;
    private MappingsException exception;

    public BeanPropertyAnnotationMappingParam(BeanWrapper beanWrapper, PropertyDescriptor parameter, MappingsException ex) {
        super(parameter);
        this.beanWrapper = beanWrapper;
        this.exception = ex;
    }

    public BeanWrapper getBeanWrapper() {
        return beanWrapper;
    }

    public MappingsException getException() {
        return exception;
    }

    @Override
    protected Method getMethod() {
        return getParameter().getReadMethod();
    }

    @Override
    public String getPropertyName() {
        return getParameter().getName();
    }
}
