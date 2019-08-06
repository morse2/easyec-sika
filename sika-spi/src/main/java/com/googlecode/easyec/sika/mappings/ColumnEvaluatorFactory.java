package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 工作本列解析工厂类。
 *
 * @author JunJie
 */
public class ColumnEvaluatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ColumnEvaluatorFactory.class);

    /**
     * 解析工作本数据列集合，并抽取一个指定的列数据对象。
     * 当出现以下情况之一，会抛出<code>UnknownColumnException</code>：
     * <ol>
     * <li>参数col不正确，比如为A-Z</li>
     * <li>参数col太大，超过了参数list的取值范围</li>
     * </ol>
     *
     * @param list 工作本一行数据列集合
     * @param col  列的表现值
     * @return 一列工作数据对象
     * @throws UnknownColumnException 未找到指定的列数据，则抛出此异常
     * @see WorkData
     */
    public static WorkData evaluateWorkData(List<WorkData> list, String col) throws UnknownColumnException {
        try {
            return list.get(ColumnEvaluator.calculateColIndex(col));
        } catch (UnknownColumnException e) {
            logger.error(e.getMessage(), e);

            throw e;
        } catch (IndexOutOfBoundsException e) {
            logger.error(e.getMessage(), e);

            throw new UnknownColumnException(e, true);
        }
    }

    /**
     * 计算给定工作本列的索引值。
     * 例如，当给定的列为'A'，
     * 则返回的索引是0，给定的列为'B'，
     * 则返回的索引是1，以此类推。
     *
     * @param col 列名
     * @return 列名对应的索引值
     * @throws UnknownColumnException
     */
    public static int calculateColumnIndex(String col) throws UnknownColumnException {
        return ColumnEvaluator.calculateColIndex(col);
    }
}
