package com.googlecode.easyec.sika;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Created by IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-1-29
 * Time: 上午9:24
 * To change this template use File | Settings | File Templates.
 */
public class WorkbookHeader implements Serializable {

    private static final long serialVersionUID = 6113891394524166641L;
    List<WorkData[]> headerList = new LinkedList<WorkData[]>();
    private boolean hasHeader;
    private int headerCount;
    private int rawHeaderCount;

    public WorkbookHeader(List<WorkData[]> headerList) {
        if (headerList == null) {
            throw new IllegalArgumentException("Header list is null.");
        }

        if (!headerList.isEmpty()) {
            this.headerList.addAll(headerList);
            _refresh();
            rawHeaderCount = this.headerCount;
        }
    }

    public WorkbookHeader(int headerCount) {
        if (headerCount > 0) {
            this.headerCount = headerCount;
            this.hasHeader = this.headerCount > 0;
            this.rawHeaderCount = this.headerCount;
        }
    }

    public void addHeader(List<WorkData> data) {
        addHeader(data.toArray(new WorkData[data.size()]));
    }

    public void addHeader(WorkData[] data) {
        headerList.add(data);
        _refresh();
    }

    public List<WorkData[]> getHeaderList() {
        return unmodifiableList(headerList);
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public int getHeaderCount() {
        return headerCount;
    }

    public int getRawHeaderCount() {
        return rawHeaderCount;
    }

    private void _refresh() {
        this.headerCount = this.headerList.size();
        this.hasHeader = this.headerCount > 0;
    }
}
