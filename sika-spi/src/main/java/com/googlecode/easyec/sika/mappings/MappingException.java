package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-5-9
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public class MappingException extends WorkingException {

    private static final long serialVersionUID = 283279968250263572L;
    private String alias;
    private int row;
    private int col;

    public MappingException(boolean stop, String alias) {
        super(stop);
        this.alias = alias;
    }

    public MappingException(String message, boolean stop, String alias) {
        super(message, stop);
        this.alias = alias;
    }

    public MappingException(String message, Throwable cause, boolean stop, String alias) {
        super(message, cause, stop);
        this.alias = alias;
    }

    public MappingException(Throwable cause, boolean stop, String alias) {
        super(cause, stop);
        this.alias = alias;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getAlias() {
        return alias;
    }
}
