package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.data.DefaultWorkData;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.beans.PropertyAccessorFactory.forBeanPropertyAccess;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public class AnnotationWorkbookRowCallback<T> extends WorkbookRowCallback<T> {

    private BeanWriteMappingParamResolver beanWriteMappingParamResolver;

    public AnnotationWorkbookRowCallback(Grabber<T> grabber) {
        super(grabber);
    }

    public AnnotationWorkbookRowCallback(WorkbookHeader header, Grabber<T> grabber) {
        super(header, grabber);
    }

    public BeanWriteMappingParamResolver getBeanWriteMappingParamResolver() {
        return beanWriteMappingParamResolver;
    }

    public void setBeanWriteMappingParamResolver(BeanWriteMappingParamResolver beanWriteMappingParamResolver) {
        this.beanWriteMappingParamResolver = beanWriteMappingParamResolver;
    }

    public List<WorkData> populate(int index, T o) throws WorkingException {
        Assert.notNull(getBeanWriteMappingParamResolver(), "BeanWriteMappingParamResolver cannot be null.");

        Map<Integer, WorkData> resultMap = new HashMap<>();
        List<WorkData> list = new ArrayList<>();

        MappingsException ex = new MappingsException();
        getBeanWriteMappingParamResolver().perform(index, getStrategy(),
            new BeanWriteAnnotationMappingParam(forBeanPropertyAccess(o), resultMap, ex)
        );

        if (ex.hasExceptions()) throw ex;

        int maxVal
            = resultMap.keySet()
            .stream()
            .mapToInt(Integer::intValue)
            .max()
            .orElse(-1);

        for (int i = 0; i <= maxVal; i++) {
            list.add(resultMap.getOrDefault(i, new DefaultWorkData()));
        }

        return list;
    }
}
