package com.googlecode.easyec.sika.mappings;

public abstract class AnnotationMappingParam<T> {

    private T parameter;

    public AnnotationMappingParam(T parameter) {
        this.parameter = parameter;
    }

    public T getParameter() {
        return parameter;
    }

    abstract public void resolved();
}
