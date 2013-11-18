package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkbookHeader;
import com.googlecode.easyec.sika.WorkbookRowHandler;
import com.googlecode.easyec.sika.WorkingException;
import org.springframework.beans.BeanWrapperImpl;

import java.util.List;

/**
 * 工作本行数据的注解映射方式的处理器实现类。
 *
 * @author JunJie
 */
public abstract class AnnotationWorkbookRowHandler<T> extends WorkbookRowHandler {

    private Class genericClass;

    protected AnnotationWorkbookRowHandler() {
        super();
        _init();
    }

    protected AnnotationWorkbookRowHandler(WorkbookHeader header) {
        super(header);
        _init();
    }

    private void _init() {
        try {
            genericClass = ClassUtils.resolveGenericType(this, 0);
        } catch (WorkingException e) {
            logger.error(e.getMessage(), e);

            throw new IllegalArgumentException("Undefined generic class.");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean populate(int index, List<WorkData> list) throws WorkingException {
        BeanWrapperImpl bw = new BeanWrapperImpl(genericClass);
        AnnotationColumnMappingAdapter.fill(index, bw, list, getDocType());
        return processObject(index, (T) bw.getWrappedInstance());
    }

    /**
     * 泛型对象处理的方法。
     * 子类继承此类，并且实现此方法后，
     * 用以处理具体的对象类型的数据。
     *
     * @param index 行号索引
     * @param o     泛型对象
     * @return 标识是否处理余下的行数据
     * @throws WorkingException
     */
    abstract public boolean processObject(int index, T o) throws WorkingException;
}
