package com.googlecode.easyec.sika.support;

import com.googlecode.easyec.sika.mappings.UnknownColumnException;

/**
 * 默认的工作本策略
 *
 * @author JunJie
 */
final class DefaultWorkbookStrategy extends WorkbookStrategy {

    public DefaultWorkbookStrategy() { /* no op */ }

    public DefaultWorkbookStrategy(String[] columnNameList) throws UnknownColumnException {
        super(columnNameList);
    }

    public DefaultWorkbookStrategy(int[] columnIndexList) throws UnknownColumnException {
        super(columnIndexList);
    }
}
