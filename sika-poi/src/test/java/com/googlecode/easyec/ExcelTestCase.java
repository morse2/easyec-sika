package com.googlecode.easyec;

import com.googlecode.easyec.sika.WorkbookReader;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.mappings.ColumnEvaluatorFactory;
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
        System.out.println(ColumnEvaluatorFactory.calculateColumnIndex("AA"));

        UserImportHandler handler = new UserImportHandler();
        WorkbookReader reader = new WorkbookReader();
        reader.add(handler);

        InputStream in = new ClassPathResource("Book1.xls").getInputStream();
        ExcelFactory.getInstance().read(in, reader);

        List<User> users = handler.getUsers();
        System.out.println(users.size());
    }
}
