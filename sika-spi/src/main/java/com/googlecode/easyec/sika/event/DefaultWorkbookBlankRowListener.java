package com.googlecode.easyec.sika.event;

import com.googlecode.easyec.sika.WorkData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 默认空行监听器类。
 *
 * @author JunJie
 */
public class DefaultWorkbookBlankRowListener implements WorkbookBlankRowListener {

    private static final Logger logger = LoggerFactory.getLogger(WorkbookBlankRowListener.class);

    public boolean accept(RowEvent event) {
        List<WorkData> list = event.getWorkData();
        if (logger.isDebugEnabled()) {
            logger.debug("Row: [" + event.getNumberOfRow() + "]. " + Arrays.toString(list.toArray()));
        }

        return false;
    }
}
