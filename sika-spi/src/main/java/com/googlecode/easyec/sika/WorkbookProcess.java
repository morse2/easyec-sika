package com.googlecode.easyec.sika;

import static com.googlecode.easyec.sika.support.WorkbookStrategy.DEFAULT;

public interface WorkbookProcess {

    default void doInit(WorkbookProcessAware aware) throws WorkingException {
        aware.doInit();

        // 设置文档策略对象
        if (aware.getStrategy() == null) {
            aware.setStrategy(DEFAULT);
        }
    }
}
