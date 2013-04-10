package com.googlecode.easyec.sika;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-5
 * Time: 下午1:58
 * To change this template use File | Settings | File Templates.
 */
public abstract class Grabber<T> {

    abstract public List<T> grab() throws WorkingException;
}
