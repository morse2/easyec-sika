package com.googlecode.easyec.sika.support;

import com.googlecode.easyec.sika.DocType;
import com.googlecode.easyec.sika.WorkbookProcessAware;
import com.googlecode.easyec.sika.mappings.UnknownColumnException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.googlecode.easyec.sika.mappings.ColumnEvaluatorFactory.calculateColumnIndex;
import static com.googlecode.easyec.sika.support.WorkbookStrategy.ExceptionBehavior.ThrowOne;
import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 抽象类，用于描述工作本的操作策略
 *
 * @author JunJie
 */
public abstract class WorkbookStrategy {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // ----- 列配置 开始
    private List<Integer> columns = new ArrayList<>();
    private boolean allColumns;
    // ----- 列配置 结束

    // ----- 文档类型配置 开始
    private DocType docType;
    // ----- 文档类型配置 结束

    private ExceptionBehavior exceptionBehavior = ThrowOne;

    // ----- 指出是否需要拷贝行的样式
    private boolean copyRowStyleOnWrite = true;

    // ----- 范型类型
    private List<Class> genericTypes;

    /**
     * 异常抛出行为的枚举类
     */
    public enum ExceptionBehavior {
        /**
         * 当有异常发生即抛出
         */
        ThrowOne,
        /**
         * 将所有异常包括起来，最后抛出
         */
        ThrowAll
    }

    private WorkbookStrategy() {
        this.allColumns = true;
    }

    protected WorkbookStrategy(Object wb) {
        this();
        _init3(wb);
    }

    /**
     * 通过一组列名构造工作本策略对象实例
     *
     * @param columnNameList 工作本列名
     * @throws UnknownColumnException 未知的列别名
     */
    protected WorkbookStrategy(Object wb, String[] columnNameList) throws UnknownColumnException {
        Assert.notNull(columnNameList, "List of column name mustn't be null.");
        _init0(columnNameList);
        _init3(wb);
    }

    /**
     * 通过一组列索引构造工作本策略对象实例
     *
     * @param columnIndexList 工作本列索引
     * @throws UnknownColumnException 不能解析给定的列索引位
     */
    protected WorkbookStrategy(Object wb, int[] columnIndexList) throws UnknownColumnException {
        Assert.notNull(columnIndexList, "List of column index mustn't be null.");
        _init1(columnIndexList);
        _init3(wb);
    }

    /**
     * 通过开始的列名+结束的列名，
     * 构造工作本策略对象实例
     *
     * @param start 开始列名
     * @param end   结束列名
     * @throws UnknownColumnException 不能解析给定的列别名对应的索引位
     */
    protected WorkbookStrategy(Object wb, String start, String end) throws UnknownColumnException {
        Assert.notNull(start, "Start column mustn't be null.");
        Assert.notNull(end, "End column mustn't be null.");
        _init2(calculateColumnIndex(start), calculateColumnIndex(end));
        _init3(wb);
    }

    /**
     * 通过开始的列索引+结束的列索引，
     * 构造工作本策略对象实例
     *
     * @param start 开始列索引
     * @param end   结束列索引
     * @throws UnknownColumnException 不能解析给定的列索引位
     */
    protected WorkbookStrategy(Object wb, int start, int end) throws UnknownColumnException {
        _init2(start, end);
        _init3(wb);
    }

    private void _init0(String[] columns) throws UnknownColumnException {
        for (String column : columns) {
            this.columns.add(
                calculateColumnIndex(column.toUpperCase())
            );
        }
    }

    private void _init1(int[] columns) throws UnknownColumnException {
        for (int column : columns) {
            if (column < 0) {
                throw new UnknownColumnException("Column index must be greater than 0.", true);
            }

            if (this.columns.contains(column)) {
                logger.warn("There has set column [{}], so shouldn't add again.", column);

                continue;
            }

            this.columns.add(column);
        }
    }

    private void _init2(int startIndex, int endIndex) throws UnknownColumnException {
        if (startIndex < 0 || endIndex < 0) {
            throw new UnknownColumnException("Both start column index and end index must be greater than 0.", true);
        }

        List<Integer> list = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            list.add(i);
        }

        _init1(
            ArrayUtils.toPrimitive(
                list.toArray(new Integer[0])
            )
        );
    }

    /* resolve handler & callback generic types */
    private void _init3(Object o) {
        Assert.notNull(o, "Object cannot be null.");
        Type genType = o.getClass().getGenericSuperclass();

        while (genType instanceof Class) {
            if (null == ((Class) genType).getSuperclass()) {
                String msg = "No superclass was found. [" + o.getClass().getName() + "].";
                logger.error(msg);

                break;
            }

            genType = ((Class) genType).getGenericSuperclass();
        }

        if (!(genType instanceof ParameterizedType)) {
            String msg = o.getClass().getSimpleName() + "'s superclass isn't class ParameterizedType";
            logger.error(msg);

            this.genericTypes = emptyList();

            return;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.genericTypes
            = Stream.of(params)
            .map(type -> type instanceof Class ? (Class) type : type.getClass())
            .collect(Collectors.toList());
    }

    /**
     * 检查给定的列索引是否已包含在配置中，
     * 以判断后续业务逻辑是否需要继续执行。
     *
     * @param columnIndex 列索引
     */
    public boolean isColumnConfigured(int columnIndex) {
        return columnIndex >= 0 && (allColumns || this.columns.contains(columnIndex));
    }

    public DocType getDocType() {
        return docType;
    }

    public void setDocType(DocType docType) {
        Assert.notNull(docType);
        this.docType = docType;
    }

    public ExceptionBehavior getExceptionBehavior() {
        return exceptionBehavior;
    }

    public void setExceptionBehavior(ExceptionBehavior exceptionBehavior) {
        if (exceptionBehavior != null) this.exceptionBehavior = exceptionBehavior;
    }

    public boolean isCopyRowStyleOnWrite() {
        return copyRowStyleOnWrite;
    }

    /**
     * 设置在写出工作本数据时候是否需要拷贝第一行的样式
     *
     * @param copyRowStyleOnWrite bool值
     */
    public void setCopyRowStyleOnWrite(boolean copyRowStyleOnWrite) {
        this.copyRowStyleOnWrite = copyRowStyleOnWrite;
    }

    public <T> Class<T> getGenericType(int index) {
        if (isEmpty(genericTypes)) return null;
        if (index < 0 || index >= genericTypes.size()) return null;

        try {
            //noinspection unchecked
            return genericTypes.get(index);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }

    /* 创建一个默认策略实例对象 */
    public static WorkbookStrategy create(WorkbookProcessAware wb) {
        return new DefaultWorkbookStrategy(wb);
    }

    /**
     * 通过一组列名构造工作本策略对象实例
     *
     * @param columns 工作本列名
     * @throws UnknownColumnException 不能解析给定的列别名对应的索引位
     */
    public static WorkbookStrategy create(WorkbookProcessAware wb, String[] columns) throws UnknownColumnException {
        return new DefaultWorkbookStrategy(wb, columns);
    }

    /**
     * 通过一组列索引构造工作本策略对象实例
     *
     * @param columns 工作本列索引
     * @throws UnknownColumnException 不能解析给定的列索引位
     */
    public static WorkbookStrategy create(WorkbookProcessAware wb, int[] columns) throws UnknownColumnException {
        return new DefaultWorkbookStrategy(wb, columns);
    }

    /**
     * 通过开始的列名+结束的列名，
     * 构造工作本策略对象实例
     *
     * @param start 开始列名
     * @param end   结束列名
     * @throws UnknownColumnException 不能解析给定的列别名对应的索引位
     */
    public static WorkbookStrategy create(WorkbookProcessAware wb, String start, String end) throws UnknownColumnException {
        return new DefaultWorkbookStrategy(wb, start, end);
    }

    /**
     * 通过开始的列索引+结束的列索引，
     * 构造工作本策略对象实例
     *
     * @param startCol 开始列索引
     * @param endCol   结束列索引
     * @throws UnknownColumnException 不能解析给定的列索引位
     */
    public static WorkbookStrategy create(WorkbookProcessAware wb, int startCol, int endCol) throws UnknownColumnException {
        return new DefaultWorkbookStrategy(wb, startCol, endCol);
    }
}
