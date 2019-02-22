package com.googlecode.easyec.sika.csv.test;

import com.googlecode.easyec.sika.*;
import com.googlecode.easyec.sika.csv.CsvFactory;
import com.googlecode.easyec.sika.csv.CsvSchema;
import com.googlecode.easyec.sika.data.DefaultWorkData;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 俊杰 on 2014/6/25.
 */
public class CsvTest {

    @Test
    public void writeCsv() throws WorkingException, IOException {
        final List<String> data = new ArrayList<String>();
        data.add("客户1935左右来电催单，系统显示\"can't find sender\",客户反映问过门卫，坚称司机根本没有去过，也没有接到司机电话，已作简单解释，客户同意明天上午优先取件，但要求调查司机是否真有去过取件并尽快回复");

        WorkbookWriter writer = new WorkbookWriter();
        writer.add(
            new WorkbookRowCallback<String>(
                new Grabber<String>() {

                    @Override
                    public List<String> grab() throws WorkingException {
                        return data;
                    }
                }
            ) {

                public List<WorkData> populate(int index, String o) throws WorkingException {
                    List<WorkData> list = new ArrayList<WorkData>();

                    list.add(new DefaultWorkData(o));

                    return list;
                }
            }
        );

        StringWriter w = new StringWriter();
        CsvFactory.getInstance().write(w, writer, new CsvSchema(',', '"', '"'));

        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        IOUtils.write(w.toString(), bos, "gb18030");

        /*FileOutputStream out = new FileOutputStream("D:/b.csv");
        out.write(bos.toByteArray());
        out.flush();
        out.close();*/
    }
}
