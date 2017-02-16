package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * 表示支持包括多个映射错误的异常类
 *
 * @author JunJie
 */
public class MappingsException extends WorkingException {

    private static final long serialVersionUID = -591214283107563838L;
    private List<MappingException> exceptions = new ArrayList<MappingException>();

    public MappingsException() {
        super(true);
    }

    public MappingsException(String message, boolean stop) {
        super(message, stop);
    }

    public MappingsException(String message, Throwable cause, boolean stop) {
        super(message, cause, stop);
    }

    public MappingsException(Throwable cause, boolean stop) {
        super(cause, stop);
    }

    public void add(MappingException ex) {
        if (ex != null) this.exceptions.add(ex);
    }

    public List<MappingException> getExceptions() {
        return unmodifiableList(exceptions);
    }

    public boolean hasExceptions() {
        return !exceptions.isEmpty();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();

        for (MappingException e : exceptions) {
            e.printStackTrace();
        }
    }
}
