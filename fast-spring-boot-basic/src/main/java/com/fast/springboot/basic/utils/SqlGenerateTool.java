package com.fast.springboot.basic.utils;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_DIR;

/**
 * 文本替换工具
 *
 * @author bw
 * @since 2020-07-31
 */
public class SqlGenerateTool {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_DIR + "/zReplaceText.sql";

    private static final List<String> KEY_WORD_LIST = Lists.newArrayList(
            "AAA",
            "BBB",
            "CCC"
    );

    public static void main(String[] args) {
        List<String> fileContents = filterContentFromFile(RAW_FILE_DIR_BAK);
        StringBuilder stringBuilder = new StringBuilder();

        int index = 1;
        for (String keyword : KEY_WORD_LIST) {
            for (String strLine : fileContents) {
                stringBuilder.append(strLine.replace("MY_KEYWORD", keyword).replace("$index", String.valueOf(index)));
                stringBuilder.append("\n");
            }
            stringBuilder.append("\n\n");
            index++;
        }
        System.out.println(stringBuilder.toString());
    }

    private static List<String> filterContentFromFile(String filePath) {
        File file = new File(filePath);
        List<String> textList = new ArrayList<>();
        String strLine;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((strLine = bufferedReader.readLine()) != null) {
                textList.add(strLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textList.forEach(System.out::println);
        return textList;
    }
}
