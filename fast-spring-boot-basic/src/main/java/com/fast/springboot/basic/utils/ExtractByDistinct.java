package com.fast.springboot.basic.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文本去重工具
 * logFileByDistinct.txt：含重复的文本
 *
 * @author bw
 * @since 2022-08-06
 */
public class ExtractByDistinct {
    public static void main(String[] args) {
        List<String> allContents = getTextFromFile(UserConstants.USER_WORK_FILE_DIR + "logFileByDistinct.txt");
        System.out.println();
        System.out.println();

        List<String> subContents = allContents.stream().map(StringUtils::trimToEmpty).distinct().collect(Collectors.toList());
        System.out.println(String.format("————> 原条数: %s, 去重后的条数: %s", allContents.size(), subContents.size()));
        subContents.forEach(System.out::print);
    }

    private static List<String> getTextFromFile(String filePath) {
        System.out.println(String.format("--------> extract text from file, filePath:%s", filePath));
        List<String> contents = Lists.newArrayList();
        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                String text = strLine.replace("\"", "");
                contents.add(text);
                System.out.println(String.format("--------> extract text from file, strLine:%s", text));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
}
