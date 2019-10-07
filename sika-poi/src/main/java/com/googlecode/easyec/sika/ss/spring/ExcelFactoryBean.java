package com.googlecode.easyec.sika.ss.spring;

import com.googlecode.easyec.sika.ss.ExcelFactory;
import org.springframework.beans.factory.SmartFactoryBean;

public class ExcelFactoryBean implements SmartFactoryBean<ExcelFactory> {

    @Override
    public ExcelFactory getObject() throws Exception {
        return ExcelFactory.getInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return ExcelFactory.class;
    }

    @Override
    public boolean isPrototype() {
        return true;
    }
}
