package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkbookHeader;
import com.googlecode.easyec.sika.WorkbookRowHandler;
import com.googlecode.easyec.sika.WorkingException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 工作本行数据的注解映射方式的处理器实现类。
 *
 * @author JunJie
 */
public abstract class AnnotationWorkbookRowHandler<T> extends WorkbookRowHandler {

    private BeanMappingParamResolver beanMappingParamResolver;

    protected AnnotationWorkbookRowHandler() {
        super();
    }

    protected AnnotationWorkbookRowHandler(WorkbookHeader header) {
        super(header);
    }

    public BeanMappingParamResolver getBeanMappingParamResolver() {
        return beanMappingParamResolver;
    }

    public void setBeanMappingParamResolver(BeanMappingParamResolver beanMappingParamResolver) {
        this.beanMappingParamResolver = beanMappingParamResolver;
    }

    @Override
    public boolean populate(int index, List<WorkData> data) throws WorkingException {
        Assert.notNull(getBeanMappingParamResolver(), "BeanMappingParamResolver cannot be null.");

        T ret;

        try {
            logger.trace("Prepare to perform data of row: [{}].", (index + 1));

            Class<?> cls = getStrategy().getGenericType(0);
            BeanWrapper bw = new BeanWrapperImpl(cls);
            MappingsException ex = new MappingsException();
            getBeanMappingParamResolver().perform(
                index, getStrategy(),
                new BeanAnnotationMappingParam(bw, data, ex)
            );

            if (ex.hasExceptions()) throw ex;

            //noinspection unchecked
            ret = (T) bw.getWrappedInstance();
        } finally {
            logger.trace("Finish perform data of row: [{}].", (index + 1));
        }

        // process resolved object.
        return processObject(index, ret);
    }

    /**
     * 泛型对象处理的方法。
     * 子类继承此类，并且实现此方法后，
     * 用以处理具体的对象类型的数据。
     *
     * @param index 行号索引
     * @param o     泛型对象
     * @return 标识是否处理余下的行数据
     * @throws WorkingException 工作本解析的异常
     */
    abstract public boolean processObject(int index, T o) throws WorkingException;
}
