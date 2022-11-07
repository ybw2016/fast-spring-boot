package com.fast.springboot.basic.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 文本
 *
 * @author bw
 * @since 2021-12-16
 */
public class ExtractByCompareMultiRows {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_FILE_DIR + "/logFileByMultiRows.txt";

    public static void main(String[] args) {
        filterTextByKeyword(RAW_FILE_DIR_BAK);
    }

    private static void filterTextByKeyword(String filePath) {
        List<String> allId1s = Lists.newArrayList();
        List<String> subId2s = Lists.newArrayList();
        List<String> subId3s = Lists.newArrayList();
        List<String> remainIds = Lists.newArrayList();
        boolean insertId1 = true;
        boolean insertId2 = false;
        boolean insertId3 = false;

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String strLine;
            int lineNumber = 1;
            while ((strLine = bufferedReader.readLine()) != null) {
                System.out.println(String.format("--------> lineNumber:%s, strLine:%s", lineNumber++, strLine));
                if (strLine.contains("END1")) {
                    insertId1 = false;
                    insertId2 = true;
                    continue;
                }
                if (strLine.contains("END2")) {
                    insertId2 = false;
                    insertId3 = true;
                    continue;
                }

                if (insertId1) {
                    allId1s.add(strLine);
                } else if (insertId2) {
                    subId2s.add(strLine);
                } else if (insertId3) {
                    subId3s.add(strLine);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("文本处理结果——> allId1s:%s, subId2s:%s, subId3s:%s", allId1s.size(), subId2s.size(), subId3s.size()));

        for (String text : allId1s) {
            if (!subId2s.contains(text) && !subId3s.contains(text)) {
                remainIds.add(text);
            }
        }

        System.out.println(String.format("最终找到的结果 ——> rows:%s, remainIds:%s", remainIds.size(), JSON.toJSONString(remainIds)));
        System.out.println();
    }
}
