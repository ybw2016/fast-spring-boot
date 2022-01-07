package com.fast.springboot.basic.utils;

import java.io.*;

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
        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                if (!strLine.contains(keyWord)) {
                    continue;
                }
                System.out.println(String.format("--------> keyWord:%s, strLine:%s", keyWord, strLine));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("=============================筛选后的最终文本=============================");
    }
}
