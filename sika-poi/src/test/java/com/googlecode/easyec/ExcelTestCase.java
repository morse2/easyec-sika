package com.googlecode.easyec;

import com.googlecode.easyec.sika.ListGrabber;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.ss.ExcelFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
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
        Assert.assertNotNull(users);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream("/Users/junjie/Desktop/test.xlsx");
            InputStream templateIS = new ClassPathResource("template_2003.xlsx").getInputStream();
            ExcelFactory.getInstance().writeLines(templateIS, fos, new MyWorkbookCallback(new ListGrabber<>(users)));
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }
}
