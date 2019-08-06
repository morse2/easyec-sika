package com.googlecode.easyec.sika.mappings;

import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;

import static org.springframework.beans.PropertyAccessorFactory.forBeanPropertyAccess;

public class BeanReadMappingParamResolver extends AbstractBeanMappingParamResolver<BeanReadAnnotationMappingParam> {

    public BeanReadMappingParamResolver(AnnotationMappingResolverChain<? extends BeanPropertyAnnotationMappingParam, PropertyDescriptor> annotationMappingResolverChain) {
        super(annotationMappingResolverChain);
    }

    @Override
    protected BeanReadAnnotationMappingParam createBeanAnnotationMappingParam(Class<?> type, BeanReadAnnotationMappingParam parent) {
        return new BeanReadAnnotationMappingParam(forBeanPropertyAccess(type), parent.getDataList(), parent.getException());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends BeanPropertyAnnotationMappingParam> T createBeanPropertyAnnotationMappingParam(BeanWrapper bw, PropertyDescriptor pd, BeanReadAnnotationMappingParam param) {
        return (T) new BeanPropertyReadAnnotationMappingParam(bw, pd, param.getDataList(), param.getException());
    }
}
