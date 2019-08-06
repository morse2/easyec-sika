package com.googlecode.easyec.sika;

import java.util.List;

public class ListGrabber<T> extends Grabber<T> {

    private List<T> list;

    public ListGrabber(List<T> list) {
        this.list = list;
    }

    @Override
    public List<T> grab() throws WorkingException {
        return list;
    }
}
