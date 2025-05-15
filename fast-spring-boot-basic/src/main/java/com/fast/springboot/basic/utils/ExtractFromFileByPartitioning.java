package com.fast.springboot.basic.utils;

import org.apache.commons.compress.utils.Lists;

import java.io.*;
import java.util.List;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 文件拆分工具（将一个文件拆分成两个文件）
 * partitioning4ContainsKeyword：    含有关键字的文件
 * partitioning4NotContainsKeyword： 不含有关键字的文件
 *
 * @author bw
 * @since 2025-05-15
 */
public class ExtractFromFileByPartitioning {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_FILE_DIR + "/logFileByPartitioning.txt";

    public static void main(String[] args) throws FileNotFoundException {
        filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD");
    }

    private static void filterTextByKeyword(String rawFilePath, String keyword) throws FileNotFoundException {
        List<String> partitioning4ContainsKeyword = Lists.newArrayList();
        List<String> partitioning4NotContainsKeyword = Lists.newArrayList();
        try (FileInputStream fileInputStream = new FileInputStream(new File(rawFilePath));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                boolean containsKeyWord = false;
                if (strLine.contains(keyword)) {
                    containsKeyWord = true;
                    partitioning4ContainsKeyword.add(strLine);
                } else {
                    partitioning4NotContainsKeyword.add(strLine);
                }
                System.out.println(String.format("--------> keyword:%s, containsKeyWord:%s, strLine:%s", keyword, containsKeyWord, strLine));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileExtHelper.writeToFile(UserConstants.USER_WORK_FILE_DIR + "compareFiles/partitioning4ContainsKeyword.txt", partitioning4ContainsKeyword);
        FileExtHelper.writeToFile(UserConstants.USER_WORK_FILE_DIR + "compareFiles/partitioning4NotContainsKeyword.txt", partitioning4NotContainsKeyword);

        System.out.println();
        System.out.println("=============================根据关键字分割文件完毕=============================");
    }

}
