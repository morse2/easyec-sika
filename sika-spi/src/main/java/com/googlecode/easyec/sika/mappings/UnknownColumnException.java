package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;

/**
 * 未知列异常类。此类表示了在模型与文档列映射时候未能找到对应的列的异常。
 *
 * @author JunJie
 */
public class UnknownColumnException extends WorkingException {

    private static final long serialVersionUID = -7984778088119484202L;

    public UnknownColumnException() {
        // no op
    }

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
