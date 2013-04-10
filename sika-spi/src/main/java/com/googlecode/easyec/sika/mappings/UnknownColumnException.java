package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-8-17
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public class UnknownColumnException extends WorkingException {

    private static final long serialVersionUID = 8069780987180040233L;

    public UnknownColumnException(boolean stop) {
        super(stop);
    }

    public UnknownColumnException(String message, boolean stop) {
        super(message, stop);
    }

    public UnknownColumnException(String message, Throwable cause, boolean stop) {
        super(message, cause, stop);
    }

    public UnknownColumnException(Throwable cause, boolean stop) {
        super(cause, stop);
    }
}
