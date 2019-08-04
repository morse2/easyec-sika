package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.converters.ModelConverter;
import com.googlecode.easyec.sika.mappings.annotations.ModelMapping;
import com.googlecode.easyec.sika.support.WorkbookStrategy;
import com.googlecode.easyec.sika.validations.AbstractColumnValidator;
import com.googlecode.easyec.sika.validations.ColumnValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Map;

import static com.googlecode.easyec.sika.support.WorkbookStrategy.ExceptionBehavior.ThrowAll;
import static org.springframework.beans.PropertyAccessorFactory.forBeanPropertyAccess;

public class BeanMappingParamResolver extends AbstractAnnotationMappingParamResolver<BeanAnnotationMappingParam, BeanWrapper> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private AnnotationMappingResolverChain<BeanPropertyAnnotationMappingParam, PropertyDescriptor> annotationMappingResolverChain;

    public BeanMappingParamResolver(AnnotationMappingResolverChain<BeanPropertyAnnotationMappingParam, PropertyDescriptor> annotationMappingResolverChain) {
        this.annotationMappingResolverChain = annotationMappingResolverChain;
    }

    public AnnotationMappingResolverChain<BeanPropertyAnnotationMappingParam, PropertyDescriptor> getAnnotationMappingResolverChain() {
        return annotationMappingResolverChain;
    }

    @Override
    public Boolean perform(int rowIndex, WorkbookStrategy strategy, BeanAnnotationMappingParam beanParam) throws WorkingException {
        BeanWrapper bw = beanParam.getParameter();

        for (PropertyDescriptor pd : bw.getPropertyDescriptors()) {
            // set current using property for bean parameter
            beanParam.setCurrentProperty(pd);

            String propertyName = beanParam.getPropertyName();
            logger.debug("Prepare to process property. [{}]", propertyName);

            if (!beanParam.isReadableProperty()) {
                logger.warn("Property [{}] has no any readable method.", propertyName);

                continue;
            }

            if (!beanParam.hasAnnotations()) {
                logger.warn("Method [{}] has no any annotations.", propertyName);

                continue;
            }

            if (beanParam.isAnnotated(getAnnotationType())) {
                Map<String, Object> attributes = beanParam.getAnnotationAttributes(getAnnotationType());
                Class<?> type = (Class<?>) attributes.get("value");
                logger.debug("Property: [{}], type: [{}].", propertyName, type.getName());

                BeanWrapper bwModel = forBeanPropertyAccess(type);
                perform(rowIndex, strategy, new BeanAnnotationMappingParam(bwModel, beanParam.getDataList(), beanParam.getException()));

                try {
                    // set original value.
                    beanParam.setOriginalValue(bwModel.getWrappedInstance());
                    // convert original value to resolved.
                    processOriginalValue(getConverter(attributes, ModelConverter.class), beanParam);
                    // validate resolved value.
                    processValidators(getValidators(attributes), rowIndex, strategy, beanParam);
                    // make as resolved.
                    beanParam.resolved();
                } catch (BeanInstantiationException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error(e.getMessage(), e);
                    }

                    throw new WorkingException(e, false);
                }

                continue;
            }

            getAnnotationMappingResolverChain().perform(
                rowIndex, strategy, new BeanPropertyAnnotationMappingParam(bw, pd, beanParam.getDataList(), beanParam.getException())
            );
        }

        return true;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return ModelMapping.class;
    }

    protected void processOriginalValue(ModelConverter<?> converter, BeanAnnotationMappingParam param) {
        Object newVal = param.getOriginalValue();
        if (converter != null) {
            newVal = converter.adorn(newVal);
        }
        logger.debug("The resolved value after converting is: [{}]. Property: [{}].", newVal, param.getCurrentProperty().getName());

        param.setResolvedValue(newVal);
    }

    protected void processValidators(Class<? extends ColumnValidator>[] validators, int rowIndex, WorkbookStrategy strategy, BeanAnnotationMappingParam param) throws WorkingException {
        if (ArrayUtils.isNotEmpty(validators)) {
            for (Class<? extends ColumnValidator> validator : validators) {
                ColumnValidator cv = (ColumnValidator) BeanUtils.instantiateClass(validator);
                if (!cv.accept(param.getResolvedValue())) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("An error occurred when checking type of data. Property name: [");
                    sb.append(param.getPropertyName()).append("], and value: [").append(param.getOriginalValue());
                    sb.append("]. Error type: [").append(cv.getAlias()).append("].");

                    logger.error(sb.toString());

                    MappingException ex = new MappingException(sb.toString(), false, cv.getAlias());
                    ex.setRow(rowIndex + 1);

                    if (cv instanceof AbstractColumnValidator) {
                        ex.setSource(((AbstractColumnValidator) cv).getSource());
                    }

                    if (strategy.getExceptionBehavior() == ThrowAll) {
                        param.getException().add(ex);
                        break;
                    } else throw ex;
                }
            }
        }
    }
}
