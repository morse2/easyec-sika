package com.googlecode.easyec.sika.mappings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AnnotationMappingParam<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private T parameter;

    public AnnotationMappingParam(T parameter) {
        this.parameter = parameter;
    }

    public T getParameter() {
        return parameter;
    }

    abstract public void resolved();
}
