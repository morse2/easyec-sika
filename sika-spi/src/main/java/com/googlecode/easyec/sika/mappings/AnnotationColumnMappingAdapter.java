package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.DocType;
import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.mappings.annotations.ColumnMapping;
import com.googlecode.easyec.sika.mappings.annotations.ModelMapping;
import com.googlecode.easyec.sika.validations.ColumnValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.calculateColIndex;
import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.getMaxColIndex;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-5-8
 * Time: 下午5:39
 * To change this template use File | Settings | File Templates.
 */
final class AnnotationColumnMappingAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationColumnMappingAdapter.class);

    static synchronized void fill(int rowIndex, BeanWrapper bw, List<WorkData> dataList, DocType docType) throws WorkingException {
        try {
            logger.debug("Prepare to populate data of row: [" + (rowIndex + 1) + "].");

            _fill(rowIndex, bw, dataList, docType);
        } finally {
            logger.debug("Finish populating data of row: [" + (rowIndex + 1) + "].");
        }
    }

    @SuppressWarnings({ "unchecked" })
    private static void _fill(int rowIndex, BeanWrapper bw, List<WorkData> dataList, DocType docType) throws WorkingException {
        PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            if (!bw.isReadableProperty(propertyName)) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Property [" + propertyName + "] has no any readable method.");
                }

                continue;
            }

            Method m = pd.getReadMethod();
            // if both ModelMapping and ColumnMapper have been set at same time,
            // then firstly ModelMapping would be processed, and ColumnMapper will be ignored.
            if (m.isAnnotationPresent(ModelMapping.class)) {
                ModelMapping mm = m.getAnnotation(ModelMapping.class);
                Class<?> type = mm.value();
                if (logger.isDebugEnabled()) {
                    logger.debug("Property: [" + propertyName + "], type: [" + type.getName() + "].");
                }

                BeanWrapper bwModel = new BeanWrapperImpl(type);
                _fill(rowIndex, bwModel, dataList, docType);

                try {
                    Object val = bwModel.getWrappedInstance();
                    Class<? extends ColumnValidator>[] validators = mm.validators();
                    for (Class<? extends ColumnValidator> validator : validators) {
                        ColumnValidator cv = BeanUtils.instantiateClass(validator);

                        if (!cv.accept(val)) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("An error occurred when checking type of data. field: [");
                            sb.append(propertyName).append("], Model value: [").append(type.getName());
                            sb.append("] and value: [").append(val).append("]. Error type: [");
                            sb.append(cv.getAlias()).append("]");

                            if (logger.isErrorEnabled()) {
                                logger.error(sb.toString());
                            }

                            MappingException ex = new MappingException(sb.toString(), false, cv.getAlias());
                            ex.setRow(rowIndex);

                            throw ex;
                        }
                    }

                    ColumnConverter cc = BeanUtils.instantiateClass(mm.converter());
                    /*if (cc instanceof AdvancedColumnConverter) {
                        ((AdvancedColumnConverter) cc).setTargetBean(bw.getWrappedInstance());
                    }*/

                    bw.setPropertyValue(propertyName, cc.convert(val));
                } catch (BeanInstantiationException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(e.getMessage(), e);
                    }

                    throw new WorkingException(e, false);
                }

                continue;
            }

            if (m.isAnnotationPresent(ColumnMapping.class)) {
                ColumnMapping cm = m.getAnnotation(ColumnMapping.class);

                int colIndex = cm.index();
                if (colIndex < 0) { // do parse column attribute
                    colIndex = calculateColIndex(cm.column());
                }

                if (colIndex < 0 || (docType != null && colIndex > getMaxColIndex(docType))) {
                    if (logger.isWarnEnabled()) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Annotation ColumnMapper on field: [");
                        sb.append(propertyName).append("] is out of range: [");
                        sb.append(colIndex).append("]. The value must be between in 0 and 254. So ignore it.");
                        logger.warn(sb.toString());
                    }

                    continue;
                }

                try {
                    if (colIndex >= dataList.size()) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("There is expect colIndex: [" + colIndex + "], actual colIndex: ["
                                    + dataList.size() + "].");
                        }

                        continue;
                    }

                    Object val = dataList.get(colIndex).getValue();
                    Class<? extends ColumnValidator>[] validators = cm.validators();
                    for (Class<? extends ColumnValidator> validator : validators) {
                        ColumnValidator cv = BeanUtils.instantiateClass(validator);
                        /*if (cv instanceof AdvancedColumnValidator) {
                            ((AdvancedColumnValidator) cv).setTargetBean(bw.getWrappedInstance());
                        }*/

                        if (!cv.accept(val)) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("An error occurred when checking type of data. field: [");
                            sb.append(propertyName).append("], Value of colIndex: [").append(colIndex);
                            sb.append("] and value: [").append(val).append("]. Error type: [");
                            sb.append(cv.getAlias()).append("]");

                            if (logger.isErrorEnabled()) {
                                logger.error(sb.toString());
                            }

                            MappingException ex = new MappingException(sb.toString(), false, cv.getAlias());
                            ex.setRow(rowIndex);
                            ex.setCol(colIndex);

                            throw ex;
                        }
                    }

                    ColumnConverter cc = BeanUtils.instantiateClass(cm.converter());
                    /*if (cc instanceof AdvancedColumnConverter) {
                        ((AdvancedColumnConverter) cc).setTargetBean(bw.getWrappedInstance());
                    }*/

                    bw.setPropertyValue(propertyName, cc.convert(val));
                } catch (IndexOutOfBoundsException e) {
                    if (logger.isErrorEnabled()) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("An error Occurred when using parameter colIndex [");
                        sb.append(colIndex).append("] on field: [").append(propertyName);
                        sb.append("]. Exception: ").append(e.getMessage());
                        logger.error(sb.toString());
                    }

                    throw new WorkingException(true);
                } catch (BeanInstantiationException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(e.getMessage(), e);
                    }

                    throw new WorkingException(e, true);
                } catch (Exception e) {
                    /*if (e instanceof ColumnMapperException) {
                        throw (ColumnMapperException) e;
                    }*/

                    if (e instanceof WorkingException) {
                        throw (WorkingException) e;
                    }

                    if (logger.isErrorEnabled()) {
                        logger.error("Some errors have been occurred at row: [" + (rowIndex + 1) + "]." +
                                " Check it, please! Exception: " + e.getMessage());
                    }

                    throw new WorkingException(e, true);
                }
            }
        }
    }
}
