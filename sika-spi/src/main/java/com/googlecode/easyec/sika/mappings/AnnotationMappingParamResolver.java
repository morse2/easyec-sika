package com.googlecode.easyec.sika.mappings;

@FunctionalInterface
public interface AnnotationMappingParamResolver<IN extends AnnotationMappingParam<T>, T> extends AnnotationMappingResolver<IN, Boolean> {
}
