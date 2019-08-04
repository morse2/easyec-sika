package com.googlecode.easyec;

import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.event.WorkbookBlankRowListener;
import com.googlecode.easyec.sika.mappings.AnnotationWorkbookRowHandler;
import com.googlecode.easyec.sika.support.WorkbookStrategy;

import java.util.ArrayList;
import java.util.List;

public class UserImportHandler extends AnnotationWorkbookRowHandler<User> {

    private List<User> users = new ArrayList<User>();

    @Override
    public void doInit() throws WorkingException {
        setBlankRowListener(WorkbookBlankRowListener.DEFAULT);
//        setStrategy(WorkbookStrategy.create(this, new String[] { "B" }));
    }

    @Override
    public boolean processObject(int index, User o) throws WorkingException {
        this.users.add(o);
        return true;
    }

    public List<User> getUsers() {
        return users;
    }
}
