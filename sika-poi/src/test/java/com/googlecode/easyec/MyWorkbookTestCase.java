package com.googlecode.easyec;

import com.googlecode.easyec.sika.Grabber;
import com.googlecode.easyec.sika.ss.WorkPage;
import com.googlecode.easyec.sika.WorkbookWriter;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.ss.ExcelFactory;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-6
 * Time: 下午1:20
 * To change this template use File | Settings | File Templates.
 */
public class MyWorkbookTestCase {

    @Test
    public void writeExcel() throws Exception {
        WorkbookWriter w = new WorkbookWriter();

        w.add(new MyWorkbookCallback(new WorkPage("My sheet 1"), new Grabber<User>() {

            @Override
            public List<User> grab() throws WorkingException {
                List<User> list = new ArrayList<User>();

                for (int i = 0; i < 10; i++) {
                    User user = new User();
                    user.setName("Username_" + i);
                    user.setAge(20 + i);
                    user.setGender(i % 2 == 0 ? "M" : "F");

                    list.add(user);
                }

                return list;
            }
        }));

        w.add(new MyWorkbookCallback(new WorkPage("My sheet 2"), new Grabber<User>() {

            @Override
            public List<User> grab() throws WorkingException {
                List<User> list = new ArrayList<User>();

                for (int i = 0; i < 10; i++) {
                    User user = new User();
                    user.setName("Username_" + i);
                    user.setAge(20 + i);
                    user.setGender(i % 2 == 0 ? "F" : "M");

                    list.add(user);
                }

                return list;
            }
        }));

        InputStream in = new ClassPathResource("Book1.xls").getInputStream();
        OutputStream out = new FileOutputStream("D:/Book2.xls");

        ExcelFactory.getInstance().write(in, out, w);

        out.flush();
        out.close();
    }
}
