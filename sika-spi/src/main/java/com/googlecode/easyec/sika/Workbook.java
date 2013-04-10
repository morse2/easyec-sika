package com.googlecode.easyec.sika;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-5
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public interface Workbook<T> {

    void add(T handler);

    boolean hasMore();

    int size();

    T get(int i);
}
