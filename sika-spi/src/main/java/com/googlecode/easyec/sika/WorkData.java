package com.googlecode.easyec.sika;

import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.data.CellWorkData;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作本中的数据对象。
 * <p>
 * 数据对象应该存在于每页中。
 * 多个数据对象构成一个工作页面，
 * 而一个或多个工作页面构成一个工作本。
 * </p>
 *
 * @author JunJie
 */
public abstract class WorkData implements Serializable {

    private static final long serialVersionUID = 5343405344896151733L;

    /**
     * 工作数据类型的枚举类
     */
    public enum WorkDataType {
        /**
         * 指出工作数据的值类型为数字
         */
        NUMBER,
        /**
         * 指出工作数据的值类型为字符串
         */
        STRING,
        /**
         * 指出工作数据的值类型为日期
         */
        DATE,
        /**
         * 指出工作数据的值类型为空指针
         */
        NULL,
        /**
         * 指出工作数据的值类型为未知类型
         */
        UNKNOWN
    }

    private Object value;
    private WorkDataType workDataType;

    /**
     * 构造一个默认的工作数据对象实例
     */
    protected WorkData() {
        // default constructor
    }

    /**
     * 构造一个带有数据值的工作数据对象实例
     *
     * @param value 任何类型的工作数据
     */
    protected WorkData(Object value) {
        this.value = value;
        guessWorkDataType();
    }

    /**
     * 为此工作数据赋值
     *
     * @param value 任何类型的工作数据
     */
    public final void setValue(Object value) {
        this.value = value;
        guessWorkDataType();
    }

    /**
     * 得到此工作数据的值
     *
     * @return 工作数据
     */
    public final Object getValue() {
        return value;
    }

    /**
     * 通过给定的列转换器类，将此工作数据转换到指定的数据类型。
     *
     * @param c   转换器类
     * @param <T> 数据类型
     * @return 数据值
     */
    public final <T> T getValue(ColumnConverter<T> c) {
        Assert.notNull(c, "ColumnConverter object is null.");

        return c.convert(value);
    }

    /**
     * 得到此工作数据的类型。
     *
     * @return 数据类型枚举
     * @see WorkDataType
     */
    public WorkDataType getWorkDataType() {
        return workDataType;
    }

    /**
     * 为此工作数据的值设置类型枚举
     *
     * @param workDataType 数据类型
     */
    protected void setWorkDataType(WorkDataType workDataType) {
        this.workDataType = workDataType;
    }

    private void guessWorkDataType() {
        if (null == value) {
            setWorkDataType(WorkDataType.NULL);
        } else if (value instanceof String) {
            setWorkDataType(WorkDataType.STRING);
        } else if (value instanceof Number) {
            setWorkDataType(WorkDataType.NUMBER);
        } else if (value instanceof Date) {
            setWorkDataType(WorkDataType.DATE);
        } else {
            setWorkDataType(WorkDataType.UNKNOWN);
        }
    }

    @Override
    public String toString() {
        return "WorkData{" +
                "value=" + value +
                ", workDataType=" + workDataType +
                '}';
    }

    // static method

    /**
     * 创建一个单元格模式的工作本数据类实例。
     *
     * @param value 单元格中的值
     * @param x     横坐标，从0开始
     * @param y     纵坐标，从0开始
     * @return <code>WorkData</code>
     */
    public static WorkData createCellWorkData(Object value, int x, int y) {
        return new CellWorkData(value, x, y);
    }

    /**
     * 创建一个单元格模式的工作本数据类实例。
     *
     * @param x 横坐标，从0开始
     * @param y 纵坐标，从0开始
     * @return <code>WorkData</code>
     */
    public static WorkData createCellWorkData(int x, int y) {
        return new CellWorkData(x, y);
    }
}
