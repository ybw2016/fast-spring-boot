package com.fast.springboot.basic.automatic;

import java.util.regex.Pattern;

/**
 * @author dev_jv
 * @since 2026-05-17
 */
public class TestClassMode {
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("([1-9]\\d{5})((?:18|19|20)\\d{2})(?:0[1-9]|10|11|12)((?:0[1-9]|[1-2]\\d|30|31)\\d{3})([\\dXx])");

    public static void main(String[] args) throws Exception {
    }

}
