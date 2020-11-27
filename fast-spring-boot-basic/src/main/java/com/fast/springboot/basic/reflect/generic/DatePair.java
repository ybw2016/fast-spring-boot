package com.fast.springboot.basic.reflect.generic;

import java.util.Date;

/**
 * @author bw
 * @since 2020-11-27
 */
public class DatePair extends Pair<Date> {
    @Override
    public void setValue(Date value) {
        super.setValue(value);
    }

    @Override
    public Date getValue() {
        return super.getValue();
    }
}
