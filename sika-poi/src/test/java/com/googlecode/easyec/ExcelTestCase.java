package com.googlecode.easyec;

import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.ss.ExcelFactory;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Test case class.
 */
public class ExcelTestCase {

    @Test
    public void readExcel() throws IOException, WorkingException {
        UserImportHandler handler = new UserImportHandler();
        InputStream in = new ClassPathResource("Book1.xls").getInputStream();
        ExcelFactory.getInstance().readLines(in, handler);

        List<User> users = handler.getUsers();
        System.out.println(users.size());
    }
}
