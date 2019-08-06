package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.DocType;
import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.data.DefaultWorkData;
import com.googlecode.easyec.sika.mappings.annotations.BeanMapping;
import com.googlecode.easyec.sika.mappings.annotations.ColumnMapping;
import com.googlecode.easyec.sika.support.WorkbookStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.googlecode.easyec.sika.Constants.INST_NOT_CONVERTER;
import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.calculateColIndex;
import static com.googlecode.easyec.sika.mappings.ColumnEvaluator.getMaxColIndex;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 数据列注解形式映射的适配器类。
 * <p>
 * 该类的功能为注解映射工作本的列数据到模型对象中。
 * </p>
 *
 * @author JunJie
 * @see ColumnMapping
 * @see BeanMapping
 */
final class AnnotationColumnMappingAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationColumnMappingAdapter.class);

    static void fill(int rowIndex, BeanWrapper bw, List<WorkData> dataList, WorkbookStrategy strategy)
        throws WorkingException {
        try {
            logger.trace("Prepare to populate data of row: [{}].", (rowIndex + 1));

            MappingsException ex = new MappingsException();
            if (ex.hasExceptions()) throw ex;
        } finally {
            logger.trace("Finish populating data of row: [{}].", (rowIndex + 1));
        }
    }

    static List<WorkData> refill(BeanWrapper bw, WorkbookStrategy strategy) throws WorkingException {
        Map<Integer, WorkData> map = new TreeMap<>();
        List<WorkData> list = new ArrayList<>();

        _refill(bw, map, strategy);
        Integer[] keys = map.keySet().toArray(new Integer[0]);
        // get maximum key
        Integer maxKey = keys[keys.length - 1];
        for (int i = 0; i < maxKey; i++) {
            if (map.containsKey(i)) {
                list.add(i, map.get(i));
            } else list.add(i, new DefaultWorkData(null));
        }

        return list;
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

            if (m.isAnnotationPresent(BeanMapping.class)) {
                Object val = bw.getPropertyValue(propertyName);
                if (null == val) {
                    logger.warn("No ModelMapping was set. So ignore process this annotation.");

                    continue;
                }

                BeanMapping mm = m.getAnnotation(BeanMapping.class);
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

                // TODO: 2019-07-25
                String col = null;// cm.columnForWrite();
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

                    // TODO: 2019-08-06 modify here
//                    map.put(colIndex, new DefaultWorkData(cc.conceal(val)));
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
