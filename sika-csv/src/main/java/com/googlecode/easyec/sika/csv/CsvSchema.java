package com.googlecode.easyec.sika.csv;

import com.opencsv.CSVParser;
import com.opencsv.CSVWriter;

/**
 * CSV的概要类。
 *
 * @author JunJie
 */
public final class CsvSchema {

    private char separator = CSVParser.DEFAULT_SEPARATOR;
    private char quoteChar = CSVParser.DEFAULT_QUOTE_CHARACTER;
    private char escape = CSVParser.DEFAULT_ESCAPE_CHARACTER;
    private String lineEnd = CSVWriter.DEFAULT_LINE_END;
    private boolean strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
    private boolean ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;

    /**
     * CSV默认的内容概要信息对象实例
     */
    public static final CsvSchema DEFAULT = new CsvSchema();

    private CsvSchema() { }

    public CsvSchema(char separator) {
        this.separator = separator;
    }

    public CsvSchema(char separator, char quoteChar) {
        this.separator = separator;
        this.quoteChar = quoteChar;
    }

    public CsvSchema(char separator, char quoteChar, char escape) {
        this.separator = separator;
        this.quoteChar = quoteChar;
        this.escape = escape;
    }

    public CsvSchema(char separator, boolean strictQuotes) {
        this.separator = separator;
        this.strictQuotes = strictQuotes;
    }

    public CsvSchema(char separator, boolean strictQuotes, boolean ignoreLeadingWhiteSpace) {
        this.separator = separator;
        this.strictQuotes = strictQuotes;
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
    }

    public char getSeparator() {
        return separator;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public char getEscape() {
        return escape;
    }

    public boolean isStrictQuotes() {
        return strictQuotes;
    }

    public boolean isIgnoreLeadingWhiteSpace() {
        return ignoreLeadingWhiteSpace;
    }

    public String getLineEnd() {
        return lineEnd;
    }

    public void setLineEnd(String lineEnd) {
        this.lineEnd = lineEnd;
    }
}
