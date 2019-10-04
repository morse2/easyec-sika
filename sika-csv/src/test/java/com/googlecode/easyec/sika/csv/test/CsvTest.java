package com.googlecode.easyec.sika.csv.test;

import com.googlecode.easyec.sika.ListGrabber;
import com.googlecode.easyec.sika.WorkData;
import com.googlecode.easyec.sika.WorkbookRowCallback;
import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.csv.CsvFactory;
import com.googlecode.easyec.sika.data.DefaultWorkData;
import com.googlecode.easyec.sika.mappings.AnnotationWorkbookRowHandler;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by 俊杰 on 2014/6/25.
 */
public class CsvTest {

    @Test
    public void readCsv() throws Exception {
        AnnotationWorkbookRowHandler<Customer> handler
            = new AnnotationWorkbookRowHandler<Customer>() {

            @Override
            public boolean processObject(int index, Customer cus) throws WorkingException {
                System.out.println(cus);
                return true;
            }
        };
        InputStream in = new ClassPathResource("files/test.csv").getInputStream();
        InputStreamReader reader = new InputStreamReader(in, UTF_8);
        CsvFactory.getInstance().readLines(reader, handler);
    }

    @Test
    public void writeCsv() throws WorkingException, IOException {
        final List<String[]> data = new ArrayList<>();
        data.add(new String[] { "您\"好", "客户1935左右来电催单，系统显示\"can't find sender\",客户反映问过门卫，坚称司机根本没有去过，也没有接到司机电话，已作简单解释，客户同意明天上午优先取件，但要求调查司机是否真有去过取件并尽快回复" });

        WorkbookRowCallback<String[]> callback
            = new WorkbookRowCallback<String[]>(new ListGrabber<>(data)) {

            public List<WorkData> populate(int index, String[] o) throws WorkingException {
                List<WorkData> list = new ArrayList<>();

                for (String s : o) {
                    list.add(new DefaultWorkData(s));
                }

                return list;
            }
        };

        StringWriter w = new StringWriter();
        CsvFactory.getInstance().writeLines(w, callback);
        System.out.println(w.toString());

        /*ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        IOUtils.write(w.toString(), bos, "gb18030");

        FileOutputStream out = new FileOutputStream("D:/b.csv");
        out.write(bos.toByteArray());
        out.flush();
        out.close();*/
    }
}
