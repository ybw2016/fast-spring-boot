package com.fast.springboot.basic.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 文本筛选工具（提取关键字后面的偏移字符串）
 *
 * @author bw
 * @since 2020-07-31
 */
public class ExtractFromFileByOffset {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_FILE_DIR + "/logFileByOffset.txt";

    public static void main(String[] args) {
        filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD", 30);
    }

    private static void filterTextByKeyword(String filePath, String keyWord, int postOffset) {
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

                    int startIndex = rawStrLine.indexOf(keyWord) + keyWord.length();
                    int endIndex = startIndex + postOffset;
                    System.out.println(String.format("--------> startIndex:%s, endIndex:%s", startIndex, endIndex));

                    try {
                        strLine = strLine.substring(startIndex, endIndex);
                        System.out.println(String.format("--------> prefix:%s, strLine:%s", keyWord, strLine));
                    } catch (Exception ex) {
                        System.out.println("文本截取失败 -> " + rawStrLine);
                    }
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
}
