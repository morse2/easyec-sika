package com.googlecode.easyec.sika;

import org.springframework.util.Assert;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public class WorkPage {

    private int    sheetIndex;
    private String sheetName;

    public static final WorkPage DEFAULT = new WorkPage();

    private WorkPage() {
        // no op
    }

    public WorkPage(String sheetName) {
        this.sheetName = sheetName;
    }

    public WorkPage(int sheetIndex, String sheetName) {
        this.sheetIndex = sheetIndex;
        this.sheetName = sheetName;
    }

    /**
     * 得到引用的表单的索引
     *
     * @return 表单索引号，从0开始
     */
    public int getSheetIndex() {
        return sheetIndex;
    }

    /**
     * 设置当前工作本回调引用的表单索引号
     *
     * @param index 表单索引号，大于或等于0
     */
    public void setSheetIndex(int index) {
        Assert.isTrue(index > -1, "Sheet index must be greater or equals than 0.");
        this.sheetIndex = index;
    }

    /**
     * 返回表单名字
     *
     * @return 表单名
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * 设置表单名
     *
     * @param sheetName 表单名
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
