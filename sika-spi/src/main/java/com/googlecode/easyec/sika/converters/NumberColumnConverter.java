package com.googlecode.easyec.sika.converters;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberColumnConverter implements ColumnConverter<Object, Number> {

    private static final Logger logger = LoggerFactory.getLogger(NumberColumnConverter.class);

    @Override
    public Number adorn(Object val) {
        if (val == null) {
            logger.debug("value is null.");

            return null;
        }

        if (val instanceof Number) {
            return ((Number) val);
        }

        if (val instanceof String) {
            return NumberUtils.isCreatable(((String) val))
                ? NumberUtils.createNumber((String) val)
                : null;
        }

        logger.warn("Value is NaN. [{}], type: [{}]", val, val.getClass());

        return null;
    }
}
