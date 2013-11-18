package com.googlecode.easyec.sika;

import java.util.List;

/**
 * DOCUMENT IT
 *
 * @author JunJie
 */
public abstract class Grabber<T> {

    abstract public List<T> grab() throws WorkingException;
}
