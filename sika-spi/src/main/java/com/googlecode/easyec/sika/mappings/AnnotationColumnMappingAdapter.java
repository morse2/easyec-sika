package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.DocType;
import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.converters.ModelConverter;
import com.googlecode.easyec.sika.data.DefaultWorkData;
import com.googlecode.easyec.sika.mappings.annotations.ColumnMapping;
import com.googlecode.easyec.sika.mappings.annotations.ModelMapping;
import com.googlecode.easyec.sika.support.WorkbookStrategy;
import com.googlecode.easyec.sika.validations.AbstractColumnValidator;
import com.googlecode.easyec.sika.validations.ColumnValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.googlecode.easyec.sika.Constants.INST_NOT_CONVERTER;
import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.calculateColIndex;
import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.getMaxColIndex;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * 数据列注解形式映射的适配器类。
 * <p>
 * 该类的功能为注解映射工作本的列数据到模型对象中。
 * </p>
 *
 * @author JunJie
 * @see ColumnMapping
 * @see ModelMapping
 */
final class AnnotationColumnMappingAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationColumnMappingAdapter.class);

    static synchronized void fill(int rowIndex, BeanWrapper bw, List<WorkData> dataList, WorkbookStrategy strategy)
        throws WorkingException {
        try {
            logger.trace("Prepare to populate data of row: [" + (rowIndex + 1) + "].");

            _fill(rowIndex, bw, dataList, strategy);
        } finally {
            logger.trace("Finish populating data of row: [" + (rowIndex + 1) + "].");
        }
    }

    static synchronized List<WorkData> refill(BeanWrapper bw, WorkbookStrategy strategy) throws WorkingException {
        Map<Integer, WorkData> map = new TreeMap<Integer, WorkData>();
        List<WorkData> list = new ArrayList<WorkData>();

        _refill(bw, map, strategy);
        Integer[] keys = map.keySet().toArray(new Integer[map.size()]);
        // get maximum key
        Integer maxKey = keys[keys.length - 1];
        for (int i = 0; i < maxKey; i++) {
            if (map.containsKey(i)) {
                list.add(i, map.get(i));
            } else list.add(i, new DefaultWorkData(null));
        }

        return list;
    }

    @SuppressWarnings({ "unchecked" })
    private static void _fill(int rowIndex, BeanWrapper bw, List<WorkData> dataList, WorkbookStrategy strategy)
        throws WorkingException {
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
            // then firstly ModelMapping would be processed, and ColumnMapping will be ignored.
            if (m.isAnnotationPresent(ModelMapping.class)) {
                ModelMapping mm = m.getAnnotation(ModelMapping.class);
                Class<?> type = mm.value();
                if (logger.isDebugEnabled()) {
                    logger.debug("Property: [" + propertyName + "], type: [" + type.getName() + "].");
                }

                BeanWrapper bwModel = new BeanWrapperImpl(type);
                _fill(rowIndex, bwModel, dataList, strategy);

                try {
                    // 获取封装的对象实例
                    Object val = bwModel.getWrappedInstance();
                    // 初始化ModelConverter对象
                    ModelConverter mc = BeanUtils.instantiateClass(mm.converter());
                    // 执行转换的操作
                    Object newVal = mc.adorn(val);
                    logger.debug("The new value after converting is: [{}].", newVal);

                    Class<? extends ColumnValidator>[] validators = mm.validators();
                    for (Class<? extends ColumnValidator> validator : validators) {
                        ColumnValidator cv = (ColumnValidator) BeanUtils.instantiateClass(validator);

                        if (!cv.accept(newVal)) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("An error occurred when checking type of data. field: [");
                            sb.append(propertyName).append("], Model value: [").append(type.getName());
                            sb.append("] and value: [").append(val).append("]. Error type: [");
                            sb.append(cv.getAlias()).append("]");

                            if (logger.isErrorEnabled()) {
                                logger.error(sb.toString());
                            }

                            MappingException ex = new MappingException(sb.toString(), false, cv.getAlias());
                            ex.setRow(rowIndex + 1);

                            if (cv instanceof AbstractColumnValidator) {
                                ex.setSource(((AbstractColumnValidator) cv).getSource());
                            }

                            throw ex;
                        }
                    }

                    bw.setPropertyValue(propertyName, newVal);
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

                String col = cm.columnForRead();
                if (isBlank(col)) col = cm.column();
                logger.debug("Column [{}] will be read.", col);

                int colIndex = calculateColIndex(col);
                if (colIndex < 0) {
                    if (logger.isTraceEnabled()) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Annotation ColumnMapper on field: [");
                        sb.append(propertyName).append("] is out of range: [");
                        sb.append(colIndex).append("]. The value must be greater than 0. So ignore it.");
                        logger.trace(sb.toString());
                    }

                    continue;
                }

                DocType docType = strategy.getDocType();
                if (docType != null && colIndex > getMaxColIndex(docType)) {
                    if (logger.isTraceEnabled()) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Annotation ColumnMapper on field: [");
                        sb.append(propertyName).append("]. The value must be between in 0 and ");
                        sb.append(getMaxColIndex(docType)).append(". So ignore it.");
                        logger.trace(sb.toString());
                    }

                    continue;
                }

                try {
                    if (colIndex >= dataList.size()) {
                        if (logger.isTraceEnabled()) {
                            logger.trace(
                                new StringBuffer()
                                    .append("There is expect colIndex: [")
                                    .append(colIndex)
                                    .append("], actual colIndex: [")
                                    .append(dataList.size())
                                    .append("].")
                                    .toString()
                            );
                        }

                        continue;
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

                        continue;
                    }

                    Object val = dataList.get(colIndex).getValue();
                    // 先执行convert动作，而后在对convert后的数据进行校验
                    Object newVal = ((ColumnConverter) BeanUtils.instantiateClass(cm.converter())).adorn(val);
                    logger.debug("The new value after converting is: [{}].", newVal);

                    Class<? extends ColumnValidator>[] validators = cm.validators();
                    for (Class<? extends ColumnValidator> validator : validators) {
                        ColumnValidator cv = (ColumnValidator) BeanUtils.instantiateClass(validator);
                        if (!cv.accept(newVal)) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("An error occurred when checking type of data. field: [");
                            sb.append(propertyName).append("], Value of colIndex: [").append(colIndex);
                            sb.append("] and value: [").append(newVal).append("]. Error type: [");
                            sb.append(cv.getAlias()).append("]");

                            if (logger.isErrorEnabled()) {
                                logger.error(sb.toString());
                            }

                            MappingException ex = new MappingException(sb.toString(), false, cv.getAlias());
                            ex.setRow(rowIndex + 1);
                            ex.setCol(colIndex + 1);

                            if (cv instanceof AbstractColumnValidator) {
                                ex.setSource(((AbstractColumnValidator) cv).getSource());
                            }

                            throw ex;
                        }
                    }

                    bw.setPropertyValue(propertyName, newVal);
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
                    if (e instanceof WorkingException) {
                        throw (WorkingException) e;
                    }

                    if (logger.isErrorEnabled()) {
                        logger.error(
                            new StringBuffer()
                                .append("Some errors have been occurred at row: [")
                                .append(colIndex + 1)
                                .append("]. Check it, please! Exception: ")
                                .append(e.getMessage())
                                .toString()
                        );
                    }

                    throw new MappingException(e, true, "INTERNAL_ERR");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void _refill(BeanWrapper bw, Map<Integer, WorkData> map, WorkbookStrategy strategy) throws WorkingException {
        PropertyDescriptor[] descriptors = bw.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            String propertyName = descriptor.getName();
            if (!bw.isReadableProperty(propertyName)) {
                logger.warn("Property propertyName: [" + propertyName + "], has no any readable method.");

                continue;
            }

            Method m = descriptor.getReadMethod();

            if (m.isAnnotationPresent(ModelMapping.class)) {
                Object val = bw.getPropertyValue(propertyName);
                if (null == val) {
                    logger.warn("No ModelMapping was set. So ignore process this annotation.");

                    continue;
                }

                ModelMapping mm = m.getAnnotation(ModelMapping.class);
                Class<?> type = mm.value();
                if (logger.isDebugEnabled()) {
                    logger.debug("Property: [" + propertyName + "], type: [" + type.getName() + "].");
                }

                BeanWrapperImpl beanWrapper = new BeanWrapperImpl(type);
                beanWrapper.setWrappedInstance(val);
                _refill(beanWrapper, map, strategy);

                continue;
            }

            if (m.isAnnotationPresent(ColumnMapping.class)) {
                ColumnMapping cm = m.getAnnotation(ColumnMapping.class);

                String col = cm.columnForWrite();
                if (isBlank(col)) col = cm.column();
                logger.debug("Column [{}] will be wrote.", col);

                int colIndex = calculateColIndex(col);
                if (colIndex < 0) {
                    if (logger.isWarnEnabled()) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Annotation ColumnMapper on field: [");
                        sb.append(propertyName).append("] is out of range: [");
                        sb.append(colIndex).append("]. The value must be greater than 0. So ignore it.");
                        logger.warn(sb.toString());
                    }

                    continue;
                }

                DocType docType = strategy.getDocType();
                if (docType != null && colIndex > getMaxColIndex(docType)) {
                    if (logger.isWarnEnabled()) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Annotation ColumnMapper on field: [");
                        sb.append(propertyName).append("] is out of range: [");
                        sb.append(colIndex).append("]. The value must be between in 0 and ");
                        sb.append(getMaxColIndex(docType)).append(". So ignore it.");
                        logger.warn(sb.toString());
                    }

                    continue;
                }

                try {
                    Object val = bw.getPropertyValue(propertyName);
                    if (null == val) {
                        map.put(colIndex, new DefaultWorkData(null));

                        continue;
                    }

                    ColumnConverter cc = (ColumnConverter) BeanUtils.instantiateClass(cm.converter());
                    Class aClass = ClassUtils.resolveGenericType(cc, 0);
                    if (!aClass.isInstance(val)) {
                        StringBuffer sb = new StringBuffer();
                        sb.append("Object ColumnConverter cannot conceal the type of [");
                        sb.append(val.getClass().getName()).append("].");

                        logger.warn(sb.toString());

                        throw new MappingException(sb.toString(), true, INST_NOT_CONVERTER);
                    }

                    map.put(colIndex, new DefaultWorkData(cc.conceal(val)));
                } catch (BeansException e) {
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
                                .append("Some errors have been occurred at column: [")
                                .append((colIndex + 1))
                                .append("]. Check it, please! Exception: ")
                                .append(e.getMessage())
                                .toString()
                        );
                    }

                    throw new WorkingException(e, true);
                }
            }
        }
    }
}
