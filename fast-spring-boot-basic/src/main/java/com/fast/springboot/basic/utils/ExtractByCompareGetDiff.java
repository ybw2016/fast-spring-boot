package com.fast.springboot.basic.utils;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 文本比较工具
 * compareFile_ALL.txt：总集合
 * compareFile_SUB.txt：子集合
 * compareFile_ALL_3_RESULT_DETAIL.txt：比较的结果集合
 * 目标：输出三种比较结果（1、AB共有， 2、A中不含B， 3、B中不含A）
 *
 * @author bw
 * @since 2023-12-19
 */
public class ExtractByCompareGetDiff {
    private static final String COMPARE_RESULT_3_DIFF_RESULT_FILE_PATH = USER_WORK_FILE_DIR + "compareFiles/compareFile_ALL_3_RESULT_DETAIL.txt";

    public static void main(String[] args) throws FileNotFoundException {
        StringBuilder builder = new StringBuilder();
        List<String> contentsOfA = getTextFromFile(UserConstants.USER_WORK_FILE_DIR + "compareFiles/compareFile_ALL.txt");
        List<String> contentsOfB = getTextFromFile(UserConstants.USER_WORK_FILE_DIR + "compareFiles/compareFile_SUB.txt");
        List<String> resultOfAMiss = Lists.newArrayList();
        List<String> resultOfBMiss = Lists.newArrayList();
        List<String> resultOfBothExist = Lists.newArrayList();
        for (String a : contentsOfA) {
            if (contentsOfB.stream().anyMatch(b -> b.equals(a))) {
                resultOfBothExist.add(a);
            } else {
                resultOfBMiss.add(a);
            }
        }

        for (String b : contentsOfB) {
            if (contentsOfA.stream().noneMatch(a -> a.equals(b))) {
                resultOfAMiss.add(b);
            }
        }

        // 1、打印结果
        System.out.println(String.format("————————> AB都存在: %s, A缺失: %s, B缺失: %s", resultOfBothExist.size(), resultOfAMiss.size(), resultOfBMiss.size()));
        builder.append("\n\n————> 共同存在如下: \n");
        resultOfBothExist.forEach(bothExist -> {
            builder.append(bothExist + "\n");
        });

        builder.append("\n\n————> A中缺失如下: \n");
        resultOfAMiss.forEach(aMiss -> {
            builder.append(aMiss + "\n");
        });

        builder.append("\n\n————> B中缺失如下: \n");
        resultOfBMiss.forEach(bMiss -> {
            builder.append(bMiss + "\n");
        });

        System.out.println(builder.toString());

        // 2、输出到文件中
        File textFile = new File(COMPARE_RESULT_3_DIFF_RESULT_FILE_PATH);
        textFile.delete();
        try (PrintStream printStream = new PrintStream(new FileOutputStream(textFile))) {
            printStream.println(builder.toString());
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
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
}
