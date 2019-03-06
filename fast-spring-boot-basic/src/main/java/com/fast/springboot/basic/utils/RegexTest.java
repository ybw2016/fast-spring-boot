package com.fast.springboot.basic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bowen.yan
 * @date 2019-03-06
 */
public class RegexTest {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^\\d{8}$");
        Matcher matcher = pattern.matcher("20190309");
        System.out.println(matcher.matches());
    }
}
