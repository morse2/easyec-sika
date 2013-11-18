package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.validations.ColumnValidator;

/**
 * 列数据映射的异常类
 *
 * @author JunJie
 */
public class MappingException extends WorkingException {

    private static final long serialVersionUID = 5378979891560287746L;
    private String alias;
    private int    row;
    private int    col;

    public MappingException(String alias) {
        this.alias = alias;
    }

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

    /**
     * 返回此异常对应的行号
     *
     * @return 行号，从1开始
     */
    public int getRow() {
        return row;
    }

    /**
     * 设置此异常对应的行号
     *
     * @param row 行号，从1开始
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * 返回此异常对应的列
     *
     * @return 列，从1开始
     */
    public int getCol() {
        return col;
    }

    /**
     * 设置此异常对应的列
     *
     * @param col 列，从1开始
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * 返回此异常的别名
     *
     * @return 别名，来源于{@link ColumnValidator#getAlias()}
     */
    public String getAlias() {
        return alias;
    }
}
