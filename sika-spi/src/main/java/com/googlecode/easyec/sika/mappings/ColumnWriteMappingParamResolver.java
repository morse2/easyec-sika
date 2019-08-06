package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.mappings.annotations.ColumnWriteMapping;

import java.lang.annotation.Annotation;

public class ColumnWriteMappingParamResolver extends ColumnMappingParamResolver<BeanPropertyWriteAnnotationMappingParam> {

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return ColumnWriteMapping.class;
    }
}
