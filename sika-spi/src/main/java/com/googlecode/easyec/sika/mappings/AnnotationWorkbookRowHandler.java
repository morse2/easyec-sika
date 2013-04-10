package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkbookHeader;
import com.googlecode.easyec.sika.WorkbookRowHandler;
import com.googlecode.easyec.sika.WorkingException;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-5-8
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
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
            genericClass = resolveGenericType(0);
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

    protected Class resolveGenericType(int index) throws WorkingException {
        Type genType = this.getClass().getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            String msg = this.getClass().getSimpleName() + "'s superclass isn't class ParameterizedType";
            logger.error(msg);

            throw new WorkingException(msg, false);
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            String msg = "Index: " + index + ", Size of " + this.getClass().getSimpleName() +
                    "'s Parameterized Type: " + params.length;

            logger.error(msg);

            throw new WorkingException(msg, false);
        }

        Type param = params[index];

        if (param instanceof Class) {
            return (Class) param;
        }

        return param.getClass();
    }

    abstract public boolean processObject(int index, T o) throws WorkingException;
}
