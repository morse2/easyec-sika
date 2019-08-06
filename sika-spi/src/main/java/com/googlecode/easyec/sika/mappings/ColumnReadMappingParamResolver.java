package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.mappings.annotations.ColumnReadMapping;

import java.lang.annotation.Annotation;

public class ColumnReadMappingParamResolver extends ColumnMappingParamResolver<BeanPropertyReadAnnotationMappingParam> {

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return ColumnReadMapping.class;
    }
}
