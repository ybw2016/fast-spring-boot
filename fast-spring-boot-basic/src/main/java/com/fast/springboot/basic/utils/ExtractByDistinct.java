package com.fast.springboot.basic.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 文本去重工具
 * logFileByDistinct.txt：含重复的文本
 *
 * @author bw
 * @since 2022-08-06
 */
public class ExtractByDistinct {
    private static final String NEW_DISTINCT_FILE_PATH = USER_WORK_FILE_DIR + "logFileByDistinct-NEW.txt";

    public static void main(String[] args) throws FileNotFoundException {
        List<String> allContents = getTextFromFile(USER_WORK_FILE_DIR + "logFileByDistinct.txt");
        System.out.println();
        System.out.println();

        List<String> subContents = allContents.stream().map(StringUtils::trimToEmpty).distinct().collect(Collectors.toList());
        System.out.println(String.format("————> 原条数: %s, 去重后的条数: %s", allContents.size(), subContents.size()));
        // 1、打印到控制台
        // subContents.forEach(System.out::println);

        // 2、输出到文件中
        File textFile = new File(NEW_DISTINCT_FILE_PATH);
        textFile.delete();
        try (PrintStream printStream = new PrintStream(new FileOutputStream(textFile))) {
            subContents.forEach(printStream::println);
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
