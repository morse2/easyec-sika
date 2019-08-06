package com.googlecode.easyec.sika.text.test;

import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.mappings.AnnotationWorkbookRowHandler;
import com.googlecode.easyec.sika.mappings.annotations.ColumnMapping;
import com.googlecode.easyec.sika.mappings.annotations.ColumnReadMapping;
import com.googlecode.easyec.sika.text.mappings.annotations.PaddingColumnMapping;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TextTestCase {

    @Test
    public void test1() throws Exception {
        class A {

            private String col1;

            @PaddingColumnMapping(position = 1, columnForRead = "A")
            @ColumnReadMapping(column = "C")
            public String getCol1() {
                return col1;
            }

            public String getCol2() {
                return "Hello col2";
            }
        }

        AnnotationWorkbookRowHandler<A> handler
            = new AnnotationWorkbookRowHandler<A>() {

            @Override
            public boolean processObject(int index, A o) throws WorkingException {
                return false;
            }
        };

        BeanWrapperImpl bw = new BeanWrapperImpl(new A());
        Method method = bw.getPropertyDescriptor("col1").getReadMethod();
        Annotation[] anns = AnnotationUtils.getAnnotations(method);
        Assert.assertNotNull(anns);

        AnnotatedElement annotatedElement = AnnotatedElementUtils.forAnnotations(anns);
        ColumnMapping ma = AnnotatedElementUtils.findMergedAnnotation(annotatedElement, ColumnMapping.class);
        AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotatedElement, ma);
        Assert.assertNotNull(annotatedElement);
        Set<ColumnReadMapping> allMergedAnnotations = AnnotatedElementUtils.findAllMergedAnnotations(annotatedElement, ColumnReadMapping.class);
        Assert.assertNotNull(allMergedAnnotations);

        Map<String, Object> allAttributes = new HashMap<>();
        for (ColumnReadMapping mergedAnnotation : allMergedAnnotations) {
            Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(mergedAnnotation);
            allAttributes.putAll(attributes);
        }

        System.out.println(allAttributes);
    }
}
