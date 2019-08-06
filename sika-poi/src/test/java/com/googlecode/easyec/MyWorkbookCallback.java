package com.googlecode.easyec;

import com.googlecode.easyec.sika.Grabber;
import com.googlecode.easyec.sika.WorkbookHeader;
import com.googlecode.easyec.sika.mappings.AnnotationWorkbookRowCallback;
import com.googlecode.easyec.sika.support.WorkbookStrategy;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-6
 * Time: 下午1:16
 * To change this template use File | Settings | File Templates.
 */
public class MyWorkbookCallback extends AnnotationWorkbookRowCallback<User> {

    public MyWorkbookCallback(Grabber<User> grabber) {
        super(grabber);
    }

    public MyWorkbookCallback(WorkbookHeader header, Grabber<User> grabber) {
        super(header, grabber);
    }

    @Override
    public void doInit() {
        WorkbookStrategy strategy = WorkbookStrategy.create(this);
        strategy.setCopyRowStyleOnWrite(false);
        setStrategy(strategy);

        /*setInitializingSheetListener(
            new InitializingSheetListener() {

                public void doInit(InitializingSheetEvent event) {
                    Sheet sheet = event.getSheet();
                    SheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();
                    ConditionalFormattingRule r1 = scf.createConditionalFormattingRule(ComparisonOperator.BETWEEN, "20", "25");
                    r1.createPatternFormatting().setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

                    ConditionalFormattingRule r2 = scf.createConditionalFormattingRule(ComparisonOperator.GT, "25");
                    r2.createPatternFormatting().setFillBackgroundColor(HSSFColor.LIGHT_ORANGE.index);

                    scf.addConditionalFormatting(
                        new CellRangeAddress[] { CellRangeAddress.valueOf("B2:B" + event.getTotalRows()) },
                        new ConditionalFormattingRule[] { r1, r2 }
                    );
                }
            }
        );*/
    }
}
