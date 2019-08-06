package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.core.annotation.AnnotatedElementUtils.forAnnotations;

public abstract class DataValueAnnotationMappingParam<T> extends AnnotationMappingParam<T> {

    private List<WorkData> dataList;
    private MappingsException exception;

    private Object originalValue;
    private Object resolvedValue;

    public DataValueAnnotationMappingParam(T parameter, List<WorkData> dataList, MappingsException exception) {
        super(parameter);
        this.dataList = dataList;
        this.exception = exception;
    }

    public List<WorkData> getDataList() {
        return dataList;
    }

    public MappingsException getException() {
        return exception;
    }

    public Object getOriginalValue() {
        return originalValue;
    }

    public Object getResolvedValue() {
        return resolvedValue;
    }

    public void setOriginalValue(Object originalValue) {
        this.originalValue = originalValue;
    }

    public void setResolvedValue(Object resolvedValue) {
        this.resolvedValue = resolvedValue;
    }

    public boolean hasAnnotations() {
        return getAnnotatedElement() != null;
    }

    public boolean isAnnotated(Class<? extends Annotation> aType) {
        AnnotatedElement anElement = getAnnotatedElement();
        return anElement != null && AnnotatedElementUtils.isAnnotated(anElement, aType);
    }

    public Map<String, Object> getAnnotationAttributes(Class<? extends Annotation> cls) {
        AnnotatedElement anElement = getAnnotatedElement();
        if (anElement == null) return Collections.emptyMap();
        Annotation anno = AnnotatedElementUtils.findMergedAnnotation(anElement, cls);
        return anno != null ? AnnotationUtils.getAnnotationAttributes(anElement, anno) : Collections.emptyMap();
    }

    protected AnnotatedElement getAnnotatedElement() {
        Annotation[] annotations = AnnotationUtils.getAnnotations(getMethod());
        return ArrayUtils.isNotEmpty(annotations)
            ? forAnnotations(annotations)
            : null;
    }

    abstract protected Method getMethod();
}