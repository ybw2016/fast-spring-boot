package com.fast.springboot.basic.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 样例：afsdfsfsd_$prefixAAABBBCCC$suffix
 * 结果：AAABBBCCC
 * <p>
 * 文本筛选工具（提取关键字后面的偏移字符串 + 搜索结果二次提取）
 *
 * @author bw
 * @since 2020-07-31
 */
public class ExtractMiddleTextFromFile {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_FILE_DIR + "/logFileByMiddleText.txt";

    public static void main(String[] args) {
        filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD", "]", false);
    }

    private static void filterTextByKeyword(String filePath, String prefix, String suffix, boolean searchSubKeywordFromResult) {
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

                    int skipPrefix = prefix.length();
                    int startIndex = rawStrLine.indexOf(prefix);
                    String subLineAfterStartIndex = rawStrLine.substring(startIndex);
                    int endIndex = startIndex + subLineAfterStartIndex.indexOf(suffix);
                    if (StringUtils.isBlank(suffix)) {
                        endIndex = startIndex + subLineAfterStartIndex.length();
                    }
                    System.out.println(String.format("--------> startIndex:%s, skipPrefix:%s, endIndex:%s", startIndex, skipPrefix, endIndex));

                    strLine = strLine.substring(startIndex + skipPrefix, endIndex);
                    System.out.println(String.format("--------> prefix:%s, suffix:%s, strLine:%s", prefix, suffix, strLine));

                    if (searchSubKeywordFromResult) {
                        strLine = String.format("%s ---> %s", strLine, extractSubKeyWordByOffset(rawStrLine));
                        System.out.println(String.format("--------> strLine with keyWord:%s", strLine));
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

    /**
     * 如果需要从搜索结果中二次提取想要的关键字，则可以在此处设置并提取
     *
     * @param inputText 原始的匹配行
     * @return 抽取的子结果
     */
    private static String extractSubKeyWordByOffset(String inputText) {
        try {
            // 此处的dataResult可任意更改
            String keyWord = "\"dataResult\"";
            if (inputText.contains(keyWord)) {
                int startIndex = inputText.indexOf(keyWord);
                return inputText.substring(startIndex, startIndex + keyWord.length() + 5);
            }
        } catch (Exception ex) {
        }
        return "";
    }
}
