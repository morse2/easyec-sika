package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.validations.ColumnValidator;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class AbstractAnnotationMappingParamResolver<IN extends AnnotationMappingParam<T>, T> implements AnnotationMappingParamResolver<IN, T> {

    abstract protected Class<? extends Annotation> getAnnotationType();

    protected String getColumn(Map<String, Object> attributes) {
        return (String) attributes.get("column");
    }

    protected <C> C getConverter(Map<String, Object> attributes, Class<C> type) {
        //noinspection unchecked
        Class<C> converter = (Class<C>) attributes.get("converter");
        return converter != null && type.isAssignableFrom(converter)
            ? BeanUtils.instantiateClass(converter) : null;
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends ColumnValidator>[] getValidators(Map<String, Object> attributes) {
        Class<? extends ColumnValidator>[] validators = (Class<? extends ColumnValidator>[]) attributes.get("validators");
        return validators != null ? validators : new Class[0];
    }
}
