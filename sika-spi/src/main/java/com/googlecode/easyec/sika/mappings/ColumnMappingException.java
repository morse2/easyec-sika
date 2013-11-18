package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public class ColumnMappingException extends WorkingException {

    private static final long serialVersionUID = 3347866588332545466L;

    public ColumnMappingException() { }

    public ColumnMappingException(boolean stop) {
        super(stop);
    }

    public ColumnMappingException(String message, boolean stop) {
        super(message, stop);
    }

    public ColumnMappingException(String message, Throwable cause, boolean stop) {
        super(message, cause, stop);
    }

    public ColumnMappingException(Throwable cause, boolean stop) {
        super(cause, stop);
    }
}
