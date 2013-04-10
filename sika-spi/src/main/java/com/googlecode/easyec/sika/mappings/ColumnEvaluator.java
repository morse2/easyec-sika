package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.DocType;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.reverse;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-8-17
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
class ColumnEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(ColumnEvaluator.class);

    private static final char alphabet[] = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    public static int getMaxColIndex(DocType docType) throws UnknownColumnException {
        switch (docType) {
            case EXCEL03:
                return calculateColIndex("IV");
            case EXCEL07:
                return calculateColIndex("XFD");
        }

        logger.trace("The document type isn't Excel, actual type: [" + docType
                + "], so returns max value of integer.");

        return Integer.MAX_VALUE;
    }

    public static int calculateCol(String s) throws UnknownColumnException {
        if (isNotBlank(s)) {
            int col = 0;

            char[] cs = reverse(s).toCharArray();

            for (int i = 0; i < cs.length; i++) {
                if (!ArrayUtils.contains(alphabet, cs[i])) {
                    throw new UnknownColumnException("Unknown column: [" + cs[i] + "].", true);
                }

                int pos = ArrayUtils.indexOf(alphabet, cs[i]) + 1;

                if (i == 0) col += pos;
                else col += (pos * Math.pow(alphabet.length, i));
            }

            return col;
        }

        logger.info("Column wasn't defined, so returns min value of integer.");

        return Integer.MIN_VALUE;
    }

    public static int calculateColIndex(String s) throws UnknownColumnException {
        return (calculateCol(s) - 1);
    }
}
