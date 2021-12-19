package com.fast.springboot.basic.utils;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Json日志替换器
 * 说明：json在打印成字符串到日志中，有时会出现json格式丢失的情况，如多了引号、少了引号、有斜线、json被多次系统化产生斜线等等，需要格式化
 *
 * @author yanbowen
 * @date 2021-12-05
 */
public class JsonFormatTool {
    public static void main(String[] args) {
        String inputJson = "\"{\\\"data\\\":\\\"{\\\\\\\"amount\\\\\\\":610.36,\\\\\\\"mobile\\\\\\\":\\\\\\189****1111\"\\\\\",\"address\":\"\",\\\\\\\"idCardNo\\\\\\\":\\\\\\\"6103**********2236\"\\\\\"}}\"";
        String formatJson = StringWrapper.buildWith(inputJson)
                .removeSlash()
                .removeQuotaWithBrace()
                .removeQuotaWithComma()
                .addMobileLeftQuote()
                .build();
        // 输出结果：{"data":{"amount":610.36,"mobile":"189****1111","address":"","idCardNo":"6103**********2236"}}
        System.out.println(formatJson);
    }

    @Data
    public static class StringWrapper {
        // 识别脱敏手机号，如：138****1111
        private static Pattern MOBILE_PATTERN = Pattern.compile("\\d{3}\\*{4}\\d{4}");
        private String inputStr;

        private StringWrapper(String rawInputStr) {
            this.inputStr = rawInputStr;
        }

        public static StringWrapper buildWith(String rawInputStr) {
            return new StringWrapper(rawInputStr);
        }

        // \\ -> ""
        private StringWrapper removeSlash() {
            inputStr = inputStr.replaceAll("\\\\", "");
            return this;
        }

        private StringWrapper removeQuotaWithBrace() {
            //  1. 左括号
            // ""{ -> {
            while (inputStr.contains("\"\"{")) {
                inputStr = inputStr.replaceAll("\"\"\\{", "{");
            }
            // "{ -> {
            while (inputStr.contains("\"{")) {
                inputStr = inputStr.replaceAll("\"\\{", "{");
            }

            // 2. 右括号
            // }"" -> }
            while (inputStr.contains("}\"\"")) {
                inputStr = inputStr.replaceAll("}\"\"", "}");
            }
            // }" -> }
            while (inputStr.contains("}\"")) {
                inputStr = inputStr.replaceAll("}\"", "}");
            }

            // 3. 先引号再右括号
            // ""}  ->  "}
            while (inputStr.contains("\"\"}")) {
                inputStr = inputStr.replaceAll("\"\"}", "\"}");
            }

            return this;
        }

        // "",  ->  ",
        private StringWrapper removeQuotaWithComma() {
            StringBuilder sb = new StringBuilder();
            String keyWord = "\"\",";
            String remainStr = inputStr;

            if (remainStr.contains(keyWord)) {
                while (remainStr.contains(keyWord)) {
                    int pos = remainStr.indexOf(keyWord);
                    String sign = remainStr.substring(pos - 1, pos);
                    String replaceText;
                    // 有冒号说明当前是空的字符串，无需替换
                    if (":".equals(sign)) {
                        replaceText = keyWord;
                    } else {
                        // 文本替换：  "",  ->  ",
                        replaceText = "\",";
                    }
                    sb.append(remainStr.substring(0, pos)).append(replaceText);
                    remainStr = remainStr.substring(pos + keyWord.length());
                }
                inputStr = sb.toString() + remainStr;
            }

            return this;
        }

        // 138****1111 -> "138****1111
        private StringWrapper addMobileLeftQuote() {
            Matcher matcher = MOBILE_PATTERN.matcher(inputStr);
            List<String> handledWords = Lists.newArrayList();
            while (matcher.find()) {
                // 138****1111
                String matchedStr = matcher.group(0);
                // 已经替换过的字符串不再替换
                if (handledWords.contains(matchedStr)) {
                    continue;
                }

                // 138****1111 -> "138****1111, 前面加引号
                inputStr = StringUtils.replace(inputStr, matchedStr, "\"" + matchedStr);
                handledWords.add(matchedStr);
            }
            return this;
        }

        public String build() {
            return inputStr;
        }
    }
}
