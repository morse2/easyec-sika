package com.googlecode.easyec;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.data.DefaultWorkData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-6
 * Time: 下午1:16
 * To change this template use File | Settings | File Templates.
 */
public class MyWorkbookCallback extends WorkbookRowCallback<User> {

    public MyWorkbookCallback(Grabber<User> userGrabber) {
        super(userGrabber);
    }

    public MyWorkbookCallback(WorkPage workPage, Grabber<User> userGrabber) {
        super(workPage, userGrabber);
    }

    public List<WorkData> populate(User o) throws WorkingException {
        List<WorkData> list = new ArrayList<WorkData>();

        list.add(new DefaultWorkData(o.getName()));
        list.add(new DefaultWorkData(o.getAge()));
        list.add(new DefaultWorkData(o.getGender()));

        return list;
    }
}
