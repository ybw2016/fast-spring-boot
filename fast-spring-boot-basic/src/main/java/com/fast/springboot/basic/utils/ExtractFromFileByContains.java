package com.fast.springboot.basic.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 文本筛选工具（提取含有关键字所在的行）
 *
 * @author bw
 * @since 2021-12-16
 */
public class ExtractFromFileByContains {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_FILE_DIR + "/logFileByContains.txt";

    public static void main(String[] args) {
        filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD");
    }

    private static void filterTextByKeyword(String filePath, String keyWord) {
        File file = new File(filePath);
        List<String> textList = new ArrayList<>();
        String rawStrLine;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((rawStrLine = bufferedReader.readLine()) != null) {

                String strLine = rawStrLine;
                if (StringUtils.isNotBlank(keyWord)) {
                    BiFunction<String, String, Boolean> conditionChecker = (text, keyword) ->
                            StringUtils.isBlank(keyword) || StringUtils.isNotBlank(keyword) && text.contains(keyword);

                    if (!conditionChecker.apply(rawStrLine, keyWord)) {
                        continue;
                    }

                    try {
                        System.out.println(String.format("--------> keyWord:%s, strLine:%s", keyWord, strLine));
                    } catch (Exception ex) {
                        System.out.println("文本截取失败 -> " + rawStrLine);
                    }
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
}
