package com.googlecode.easyec.sika.csv;

import au.com.bytecode.opencsv.CSVParser;
import com.googlecode.easyec.sika.converters.ColumnConverter;
import com.googlecode.easyec.sika.converters.Date2StringConverter;

/**
 * CSV的概要类。
 *
 * @author JunJie
 */
public final class CsvSchema {

    private char separator = CSVParser.DEFAULT_SEPARATOR;
    private char quotechar = CSVParser.DEFAULT_QUOTE_CHARACTER;
    private char escape = CSVParser.DEFAULT_ESCAPE_CHARACTER;
    private boolean strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
    private boolean ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
    private ColumnConverter<String> dateColumnConverter = new Date2StringConverter("yyyy-MM-dd");

    public static final CsvSchema DEFAULT = new CsvSchema();

    private CsvSchema() {
        // no op
    }

    public CsvSchema(char separator) {
        this.separator = separator;
    }

    public CsvSchema(char separator, char quotechar) {
        this.separator = separator;
        this.quotechar = quotechar;
    }

    public CsvSchema(char separator, char quotechar, char escape) {
        this.separator = separator;
        this.quotechar = quotechar;
        this.escape = escape;
    }

    public CsvSchema(char separator, char quotechar, char escape, ColumnConverter<String> dateColumnConverter) {
        this.separator = separator;
        this.quotechar = quotechar;
        this.escape = escape;
        this.dateColumnConverter = dateColumnConverter;
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

    public CsvSchema(char separator, boolean strictQuotes, boolean ignoreLeadingWhiteSpace, ColumnConverter<String> dateColumnConverter) {
        this.separator = separator;
        this.strictQuotes = strictQuotes;
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        this.dateColumnConverter = dateColumnConverter;
    }

    public char getSeparator() {
        return separator;
    }

    public char getQuotechar() {
        return quotechar;
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

    public ColumnConverter<String> getDateColumnConverter() {
        return dateColumnConverter;
    }
}
