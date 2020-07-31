package com.fast.springboot.basic.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 文本筛选工具
 *
 * @author bw
 * @since 2020-07-31
 */
public class ExtractTextFromFile {
    private static final String RAW_FILE_DIR_BAK = "文件.txt";

    public static void main(String[] args) {
        //filterTextByKeyword(RAW_FILE_DIR_BAK);
        filterTextByKeyword(RAW_FILE_DIR_BAK, "补偿地址 url = ", "");
    }

    private static void filterTextByKeyword(String filePath, String prefix, String suffix) {
        File file = new File(filePath);
        List<String> textList = new ArrayList<>();
        String rawStrLine;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((rawStrLine = bufferedReader.readLine()) != null) {

                String strLine = rawStrLine;
                if (StringUtils.isNotBlank(prefix) || StringUtils.isNotBlank(suffix)) {
                    BiFunction<String, String, Boolean> conditionChecker = (text, keyword) ->
                        StringUtils.isBlank(keyword) || StringUtils.isNotBlank(keyword) && text.contains(keyword);

                    if (!conditionChecker.apply(rawStrLine, prefix)
                        || !conditionChecker.apply(rawStrLine, suffix)) {
                        continue;
                    }

                    int startIndex = rawStrLine.indexOf(prefix);
                    int endIndex = rawStrLine.indexOf(suffix);
                    if (StringUtils.isBlank(suffix)) {
                        endIndex = rawStrLine.length();
                    }
                    int skipPrefix = prefix.length();
                    System.out.println(String.format("--------> startIndex:%s, skipPrefix:%s, endIndex:%s", startIndex, skipPrefix, endIndex));

                    strLine = strLine.substring(startIndex + skipPrefix, endIndex);
                    System.out.println(String.format("--------> prefix:%s, suffix:%s, strLine:%s", prefix, suffix, strLine));
                }
                if (textList.contains(strLine)) {
                    System.out.println("筛选后的文本已存在 -> " + strLine);
                    continue;
                }
                textList.add(strLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("=============================筛选后的最终文本=============================");
        textList.forEach(System.out::println);
    }

    private static void filterTextByKeyword(String filePath) {
        File file = new File(filePath);
        List<String> textList = new ArrayList<>();
        String strLine;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((strLine = bufferedReader.readLine()) != null) {
                if (textList.contains(strLine)) {
                    System.out.println("文本已存在 -> " + strLine);
                    continue;
                }
                textList.add(strLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        textList.forEach(System.out::println);

    }
}
