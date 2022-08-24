package com.fast.springboot.basic.utils;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

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
    private static final String NEW_NON_DUP_FILE_PATH = USER_WORK_FILE_DIR + "compareFiles/leftPartOfC-NEW.txt";

    public static void main(String[] args) throws FileNotFoundException {
        List<String> allContents = getTextFromFile(UserConstants.USER_WORK_FILE_DIR + "compareFiles/allOfA.txt");
        List<String> subContents = getTextFromFile(UserConstants.USER_WORK_FILE_DIR + "compareFiles/partOfB.txt");
        List<String> foundAllContents = Lists.newArrayList();
        List<String> notExists = Lists.newArrayList();
        for (String allContent : allContents) {
            if (subContents.stream().anyMatch(allContent::contains)) {
                foundAllContents.add(allContent);
            } else {
                notExists.add(allContent);
            }
        }

        // 1、打印条数
        // foundAllContents.forEach(System.out::println);
        System.out.println("————> 存在条数: " + foundAllContents.size());

        System.out.println();
        System.out.println();

        notExists = notExists.stream().distinct().collect(Collectors.toList());
        // notExists.forEach(System.out::println);
        System.out.println("————> 不存在条数: " + notExists.size());

        // 2、输出到文件中
        File textFile = new File(NEW_NON_DUP_FILE_PATH);
        textFile.delete();
        try (PrintStream printStream = new PrintStream(new FileOutputStream(textFile))) {
            notExists.forEach(printStream::println);
        }
        System.out.println("———— 输出到文件完成 ————");
        System.out.println();

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
                // System.out.println(String.format("--------> extract text from file, strLine:%s", text));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
}
