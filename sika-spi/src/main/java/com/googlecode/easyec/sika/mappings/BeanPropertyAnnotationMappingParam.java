package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

public class BeanPropertyAnnotationMappingParam extends DataValueAnnotationMappingParam<PropertyDescriptor> {

    private BeanWrapper beanWrapper;

    public BeanPropertyAnnotationMappingParam(BeanWrapper beanWrapper, PropertyDescriptor parameter, List<WorkData> dataList, MappingsException ex) {
        super(parameter, dataList, ex);
        this.beanWrapper = beanWrapper;
    }

    public BeanWrapper getBeanWrapper() {
        return beanWrapper;
    }

    public String getPropertyName() {
        return getParameter().getName();
    }

    @Override
    public void resolved() {
        getBeanWrapper().setPropertyValue(
            getPropertyName(),
            getResolvedValue()
        );
    }

    @Override
    protected Method getMethod() {
        return getParameter().getReadMethod();
    }
}
