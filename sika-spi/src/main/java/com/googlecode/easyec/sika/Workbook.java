package com.googlecode.easyec.sika;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public interface Workbook<T> {

    void add(T handler);

    boolean hasMore();

    int size();

    T get(int i);
}
