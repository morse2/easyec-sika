package com.googlecode.easyec.sika.event;

import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.mappings.UnknownColumnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.googlecode.easyec.sika.WorkData.WorkDataType.NULL;
import static com.googlecode.easyec.sika.mappings.ColumnEvaluatorFactory.calculateColumnIndex;

/**
 * 默认空行监听器类。
 *
 * @author JunJie
 */
public class DefaultWorkbookBlankRowListener implements WorkbookBlankRowListener {

    private static final Logger logger = LoggerFactory.getLogger(WorkbookBlankRowListener.class);

    private int lastColIndex = Integer.MAX_VALUE;

    DefaultWorkbookBlankRowListener() { /* no op */ }

    private DefaultWorkbookBlankRowListener(int lastColIndex) {
        this.lastColIndex = lastColIndex;
    }

    /**
     * 通过给定的列名，创建一个
     * <code>WorkbookBlankRowListener</code>
     * 对象实例。
     *
     * @param lastColName 要判断是否为空行的最后一列的列名
     * @throws UnknownColumnException
     */
    public static WorkbookBlankRowListener create(String lastColName) throws UnknownColumnException {
        return new DefaultWorkbookBlankRowListener(calculateColumnIndex(lastColName));
    }

    public boolean accept(RowEvent event) {
        List<WorkData> list = event.getWorkData();
        for (int i = 0; i < list.size() && i < lastColIndex; i++) {
            if (!NULL.equals(list.get(i).getWorkDataType())) {
                logger.debug("The row [{}] is not blank.", event.getNumberOfRow());

                return true;
            }
        }

        return false;
    }
}
