package com.googlecode.easyec.sika.support;

import com.googlecode.easyec.sika.DocType;
import com.googlecode.easyec.sika.mappings.UnknownColumnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.easyec.sika.mappings.ColumnEvaluatorFactory.calculateColumnIndex;

/**
 * 抽象类，用于描述工作本的操作策略
 *
 * @author JunJie
 */
public abstract class WorkbookStrategy {

    /**
     * 创建默认的工作本策略对象
     */
    public static final WorkbookStrategy DEFAULT = new DefaultWorkbookStrategy();

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // ----- 列配置 开始
    private List<Integer> columns = new ArrayList<Integer>();
    private boolean allColumns;
    // ----- 列配置 结束

    // ----- 文档类型配置 开始
    private DocType docType;
    // ----- 文档类型配置 结束

    WorkbookStrategy() {
        this.allColumns = true;
    }

    /**
     * 通过一组列名构造工作本策略对象实例
     *
     * @param columnNameList 工作本列名
     * @throws UnknownColumnException
     */
    protected WorkbookStrategy(String[] columnNameList) throws UnknownColumnException {
        Assert.notNull(columnNameList);
        _init0(columnNameList);
    }

    /**
     * 通过一组列索引构造工作本策略对象实例
     *
     * @param columnIndexList 工作本列索引
     * @throws UnknownColumnException
     */
    protected WorkbookStrategy(int[] columnIndexList) throws UnknownColumnException {
        Assert.notNull(columnIndexList);
        _init1(columnIndexList);
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

    /**
     * 通过一组列名构造工作本策略对象实例
     *
     * @param columns 工作本列名
     * @throws UnknownColumnException
     */
    public static WorkbookStrategy create(String[] columns) throws UnknownColumnException {
        return new DefaultWorkbookStrategy(columns);
    }

    /**
     * 通过一组列索引构造工作本策略对象实例
     *
     * @param columns 工作本列索引
     * @throws UnknownColumnException
     */
    public static WorkbookStrategy create(int[] columns) throws UnknownColumnException {
        return new DefaultWorkbookStrategy(columns);
    }
}
