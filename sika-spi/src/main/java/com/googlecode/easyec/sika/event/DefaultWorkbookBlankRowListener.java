package com.googlecode.easyec.sika.event;

import com.googlecode.easyec.sika.WorkData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.googlecode.easyec.sika.WorkData.WorkDataType.NULL;

/**
 * 默认空行监听器类。
 *
 * @author JunJie
 */
public class DefaultWorkbookBlankRowListener implements WorkbookBlankRowListener {

    private static final Logger logger = LoggerFactory.getLogger(WorkbookBlankRowListener.class);

    public boolean accept(RowEvent event) {
        List<WorkData> list = event.getWorkData();
        for (WorkData data : list) {
            if (!NULL.equals(data.getWorkDataType())) {
                logger.debug("The row [{}] is not blank.", event.getNumberOfRow());

                return true;
            }
        }

        return false;
    }
}
