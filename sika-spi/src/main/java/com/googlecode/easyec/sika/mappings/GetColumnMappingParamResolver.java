package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.mappings.annotations.GetColumnMapping;

import java.lang.annotation.Annotation;

public class GetColumnMappingParamResolver extends ColumnMappingParamResolver {

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return GetColumnMapping.class;
    }
}
