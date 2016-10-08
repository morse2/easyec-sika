package com.googlecode.easyec.sika.support;

import com.googlecode.easyec.sika.mappings.UnknownColumnException;

/**
 * 默认的工作本策略
 *
 * @author JunJie
 */
final class DefaultWorkbookStrategy extends WorkbookStrategy {

    DefaultWorkbookStrategy() { /* no op */ }

    DefaultWorkbookStrategy(String[] columnNameList) throws UnknownColumnException {
        super(columnNameList);
    }

    DefaultWorkbookStrategy(int[] columnIndexList) throws UnknownColumnException {
        super(columnIndexList);
    }

    DefaultWorkbookStrategy(String start, String end) throws UnknownColumnException {
        super(start, end);
    }

    DefaultWorkbookStrategy(int start, int end) throws UnknownColumnException {
        super(start, end);
    }
}
