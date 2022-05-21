package com.fast.springboot.basic.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文本比较工具
 * allOfA.txt：包含所有的文本
 * partOfB.txt：待查找的文本
 * 目标：判断B是否在A中存在
 *
 * @author bw
 * @since 2022-05-20
 */
public class ExtractByCompareAWithB {
    public static void main(String[] args) {
        List<String> allContents = getTextFromFile(UserConstants.USER_WORK_FILE_DIR + "compareFiles/allOfA.txt");
        List<String> subContents = getTextFromFile(UserConstants.USER_WORK_FILE_DIR + "compareFiles/partOfB.txt");
        List<String> exists = Lists.newArrayList();
        List<String> notExists = Lists.newArrayList();
        for (String subContent : subContents) {
            if (allContents.contains(subContent)) {
                exists.add(subContent);
            } else {
                notExists.add(subContent);
            }
        }

        System.out.println(JSON.toJSONString(exists));
        System.out.println("————> 存在条数: " + exists.size());

        System.out.println();
        System.out.println();

        notExists = notExists.stream().distinct().collect(Collectors.toList());
        System.out.println(JSON.toJSONString(notExists));
        System.out.println("————> 不存在条数: " + notExists.size());
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
