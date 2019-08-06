package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.DocType;
import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.mappings.annotations.ColumnMapping;
import com.googlecode.easyec.sika.support.WorkbookStrategy;
import com.googlecode.easyec.sika.validations.AbstractColumnValidator;
import com.googlecode.easyec.sika.validations.ColumnValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.*;
import static com.googlecode.easyec.sika.support.WorkbookStrategy.ExceptionBehavior.ThrowAll;

public class ColumnMappingParamResolver extends AbstractAnnotationMappingParamResolver<BeanPropertyAnnotationMappingParam, PropertyDescriptor> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Boolean perform(int rowIndex, WorkbookStrategy strategy, BeanPropertyAnnotationMappingParam propParam) throws WorkingException {
        if (propParam.isAnnotated(getAnnotationType())) {
            Map<String, Object> attributes = propParam.getAnnotationAttributes(getAnnotationType());
            String col = getColumn(attributes);
            if (!checkColumn(col, strategy, propParam)) {
                return false;
            }

            int colIndex = calculateColIndex(col);
            logger.debug("Column name of index: [{}], [{}]. Property: [{}].", col, colIndex, propParam.getPropertyName());

            try {
                /*
                 * 获取文档单元格中的数据，
                 * 如果期望的列索引值大于
                 * 单元格的列索引数量，则
                 * 直接返回null
                 */
                List<WorkData> dataList = propParam.getDataList();
                Object val = (colIndex < dataList.size()) ? dataList.get(colIndex).getValue() : null;
                logger.debug("The original value is: [{}], property: [{}].", val, propParam.getPropertyName());

                propParam.setOriginalValue(val);
                // convert original value to resolved.
                processOriginalValue(getConverter(attributes, ColumnConverter.class), propParam);
                // validate resolved value.
                processValidators(getValidators(attributes), col, rowIndex, strategy, propParam);
                // make as property resolved.
                propParam.resolved();
                // stop continue.
                return false;
            } catch (IndexOutOfBoundsException e) {
                if (logger.isErrorEnabled()) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("An error Occurred when using parameter colIndex [");
                    sb.append(colIndex).append("] on field: [").append(propParam.getPropertyName());
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
                if (e instanceof WorkingException) {
                    throw (WorkingException) e;
                }

                if (logger.isErrorEnabled()) {
                    logger.error(
                        new StringBuffer()
                            .append("Some errors have been occurred at row: [")
                            .append(rowIndex + 1)
                            .append("], col: [")
                            .append(colIndex + 1)
                            .append("]. Check it, please! Exception: ")
                            .append(e.getMessage())
                            .toString()
                    );
                }

                throw new MappingException(e, true, "INTERNAL_ERR");
            }
        }

        // to be continue
        return true;
    }

    @Override
    protected Class<? extends Annotation> getAnnotationType() {
        return ColumnMapping.class;
    }

    protected boolean checkColumn(String col, WorkbookStrategy strategy, BeanPropertyAnnotationMappingParam propParam) throws UnknownColumnException {
        logger.debug("Column [{}] will be to resolve. Property: [{}]", col, propParam.getPropertyName());

        if (StringUtils.isBlank(col)) {
            if (logger.isTraceEnabled()) {
                StringBuffer sb = new StringBuffer();
                sb.append("Annotation ColumnMapper on field: [");
                sb.append(propParam.getPropertyName()).append("] is empty. [");
                sb.append(col).append("].");
                logger.trace(sb.toString());
            }

            return false;
        }

        int colIndex = calculateColIndex(col);
        if (colIndex < 0) {
            if (logger.isTraceEnabled()) {
                StringBuffer sb = new StringBuffer();
                sb.append("Annotation ColumnMapper on field: [");
                sb.append(propParam.getPropertyName()).append("] is out of range: [");
                sb.append(colIndex).append("]. The value must be greater than 0. So ignore it.");
                logger.trace(sb.toString());
            }

            return false;
        }

        DocType docType = strategy.getDocType();
        if (docType != null && colIndex > getMaxColIndex(docType)) {
            if (logger.isTraceEnabled()) {
                StringBuffer sb = new StringBuffer();
                sb.append("Annotation ColumnMapper on field: [");
                sb.append(propParam.getPropertyName()).append("]. The value must be between in 0 and ");
                sb.append(getMaxColIndex(docType)).append(". So ignore it.");
                logger.trace(sb.toString());
            }

            return false;
        }

        // 判断当前取值索引号是否在已配置的策略范围内
        if (!strategy.isColumnConfigured(colIndex)) {
            if (logger.isTraceEnabled()) {
                logger.trace(
                    new StringBuffer()
                        .append("Current column index isn't in strategy which configured columns. Index: [")
                        .append(colIndex)
                        .append("], so ignore it.")
                        .toString()
                );
            }

            return false;
        }

        return true;
    }

    protected void processOriginalValue(ColumnConverter<Object, ?> converter, BeanPropertyAnnotationMappingParam param) {
        Object newVal = param.getOriginalValue();
        if (converter != null) {
            newVal = converter.adorn(newVal);
        }
        logger.debug("The resolved value after converting is: [{}]. Property: [{}].", newVal, param.getPropertyName());

        param.setResolvedValue(newVal);
    }

    protected void processValidators(Class<? extends ColumnValidator>[] validators, String col, int rowIndex, WorkbookStrategy strategy, BeanPropertyAnnotationMappingParam param) throws WorkingException {
        if (ArrayUtils.isNotEmpty(validators)) {
            for (Class<? extends ColumnValidator> validator : validators) {
                ColumnValidator cv = (ColumnValidator) BeanUtils.instantiateClass(validator);
                if (!cv.accept(param.getResolvedValue())) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("An error occurred when checking type of data. Property name: [");
                    sb.append(param.getPropertyName()).append("], and value: [").append(param.getOriginalValue());
                    sb.append("], column name is: [").append(col);
                    sb.append("]. Error type: [").append(cv.getAlias()).append("].");

                    logger.error(sb.toString());

                    MappingException ex = new MappingException(sb.toString(), false, cv.getAlias());
                    ex.setCol(calculateCol(col));
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
