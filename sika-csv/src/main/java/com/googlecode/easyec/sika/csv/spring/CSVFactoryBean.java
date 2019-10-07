package com.googlecode.easyec.sika.csv.spring;

import com.googlecode.easyec.sika.csv.CSVFactory;
import org.springframework.beans.factory.SmartFactoryBean;

public class CSVFactoryBean implements SmartFactoryBean<CSVFactory> {

    @Override
    public CSVFactory getObject() throws Exception {
        return CSVFactory.getInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return CSVFactory.class;
    }

    @Override
    public boolean isPrototype() {
        return true;
    }
}
