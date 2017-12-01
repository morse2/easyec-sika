package com.googlecode.easyec;

import com.googlecode.easyec.sika.Grabber;
import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.data.DefaultWorkData;
import com.googlecode.easyec.sika.ss.ExcelRowCallback;
import com.googlecode.easyec.sika.WorkPage;
import com.googlecode.easyec.sika.ss.data.ExcelData;
import com.googlecode.easyec.sika.ss.event.InitializingSheetEvent;
import com.googlecode.easyec.sika.ss.event.InitializingSheetListener;
import com.googlecode.easyec.sika.ss.formulas.impl.SimpleStringFormula;
import com.googlecode.easyec.sika.support.WorkbookStrategy;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-6
 * Time: 下午1:16
 * To change this template use File | Settings | File Templates.
 */
public class MyWorkbookCallback extends ExcelRowCallback<User> {

    public MyWorkbookCallback(Grabber<User> userGrabber) {
        super(WorkPage.DEFAULT, userGrabber);
    }

    public MyWorkbookCallback(WorkPage workPage, Grabber<User> userGrabber) {
        super(workPage, userGrabber);
    }

    @Override
    public void doInit() {
        WorkbookStrategy strategy = WorkbookStrategy.DEFAULT;
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

    public List<WorkData> populate(int index, User o) throws WorkingException {
        List<WorkData> list = new ArrayList<WorkData>();

        if ((index + 1) == 1) {
            return null;
        }

        list.add(null);
        list.add(new DefaultWorkData(o.getName()));
        list.add(new DefaultWorkData(o.getAge()));
        list.add(new DefaultWorkData(o.getGender()));
        list.add(new ExcelData(new SimpleStringFormula("B2/SUM(B:B)"), 0, 0));

        return list;
    }
}
