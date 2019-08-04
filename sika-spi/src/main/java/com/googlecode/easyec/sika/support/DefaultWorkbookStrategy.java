package com.googlecode.easyec.sika.support;

import com.googlecode.easyec.sika.mappings.UnknownColumnException;

/**
 * 默认的工作本策略
 *
 * @author JunJie
 */
final class DefaultWorkbookStrategy extends WorkbookStrategy {

    DefaultWorkbookStrategy(Object wb) {
        super(wb);
    }

    DefaultWorkbookStrategy(Object wb, String[] columnNameList) throws UnknownColumnException {
        super(wb, columnNameList);
    }

    DefaultWorkbookStrategy(Object wb, int[] columnIndexList) throws UnknownColumnException {
        super(wb, columnIndexList);
    }

    DefaultWorkbookStrategy(Object wb, String start, String end) throws UnknownColumnException {
        super(wb, start, end);
    }

    DefaultWorkbookStrategy(Object wb, int start, int end) throws UnknownColumnException {
        super(wb, start, end);
    }
}
