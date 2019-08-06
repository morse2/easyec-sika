package com.googlecode.easyec.sika.mappings;

import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;

import static org.springframework.beans.PropertyAccessorFactory.forBeanPropertyAccess;

public class BeanWriteMappingParamResolver extends AbstractBeanMappingParamResolver<BeanWriteAnnotationMappingParam> {

    public BeanWriteMappingParamResolver(AnnotationMappingResolverChain<? extends BeanPropertyAnnotationMappingParam, PropertyDescriptor> annotationMappingResolverChain) {
        super(annotationMappingResolverChain);
    }

    @Override
    protected BeanWriteAnnotationMappingParam createBeanAnnotationMappingParam(Class<?> type, BeanWriteAnnotationMappingParam parent) {
        BeanWrapper bw = forBeanPropertyAccess(parent.getParameter().getPropertyValue(parent.getPropertyName()));
        return new BeanWriteAnnotationMappingParam(bw, parent.getDataMap(), parent.getException());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends BeanPropertyAnnotationMappingParam> T createBeanPropertyAnnotationMappingParam(BeanWrapper bw, PropertyDescriptor pd, BeanWriteAnnotationMappingParam param) {
        return (T) new BeanPropertyWriteAnnotationMappingParam(bw, pd, param.getDataMap(), param.getException());
    }
}
