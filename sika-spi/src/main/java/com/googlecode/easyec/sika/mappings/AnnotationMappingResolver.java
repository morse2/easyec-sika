package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.support.WorkbookStrategy;

/**
 * @author junjie
 */
@FunctionalInterface
public interface AnnotationMappingResolver<IN, OUT> {

    OUT perform(int rowIndex, WorkbookStrategy strategy, IN data) throws WorkingException;
}
