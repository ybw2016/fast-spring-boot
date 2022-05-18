package com.fast.springboot.basic.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 文本筛选工具（提取关键字后面的偏移字符串）
 * 样例：afsdfsfsd_$keywordAAABBBCCC_TTT_P
 * 样例1： filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD", 30)
 * 结果：AAABBBCCC
 * <p>
 * 样例2： filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD", 34)
 * 结果：AAABBBCCC_TTT
 * <p>
 * 样例3： filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD", 36)
 * 结果：AAABBBCCC_TTT_P
 *
 * @author bw
 * @since 2020-07-31
 */
public class ExtractFromFileByOffset {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_FILE_DIR + "/logFileByOffset.txt";

    public static void main(String[] args) {
        filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD", 30);
    }

    /**
     * @param filePath   文件真实路径
     * @param keyWord    匹配的关键字
     * @param postOffset 偏移的字符数，-1表示匹配到该行结尾
     */
    private static void filterTextByKeyword(String filePath, String keyWord, int postOffset) {
        List<String> textList = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String rawStrLine;
            while ((rawStrLine = bufferedReader.readLine()) != null) {

                String strLine = rawStrLine;
                if (StringUtils.isNotBlank(keyWord)) {
                    BiFunction<String, String, Boolean> conditionChecker = (text, keyword) ->
                            StringUtils.isBlank(keyword) || StringUtils.isNotBlank(keyword) && text.contains(keyword);

                    if (!conditionChecker.apply(rawStrLine, keyWord)) {
                        continue;
                    }

                    int startIndex = rawStrLine.indexOf(keyWord) + keyWord.length();
                    int endIndex;
                    if (postOffset == -1) {
                        endIndex = rawStrLine.length();
                    } else {
                        endIndex = startIndex + postOffset;
                    }
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
