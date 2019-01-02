package com.fast.springboot.basic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2019-01-02
 */
@Slf4j
public class ExtractorSqlFromFiles {
    private static final String RAW_FILE_DIR = "/Users/tim/Documents/fm_sqls";
    private static final String NEW_SQL_FILE_PATH = "/Users/tim/Documents/fm_all_tables.sql";

    public static void main(String[] args) {
        File file = new File(RAW_FILE_DIR);
        if (!file.exists()) {
            log.error("目录不存在");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (File currFile : file.listFiles()) {
            if (!currFile.getName().endsWith(".sql")) {
                continue;
            }

            log.info("filePath -> {}", currFile.getPath());
            String fileContent = readToString(currFile.getPath());
            stringBuilder.append(fileContent).append(System.getProperty("line.separator"));
        }

        File sqlFile = new File(NEW_SQL_FILE_PATH);
        try (PrintStream printStream = new PrintStream(new FileOutputStream(sqlFile))) {
            printStream.println(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String readToString(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filePath);
        String strLine;

        boolean createTableStarts = false;
        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((strLine = bufferedReader.readLine()) != null) {
                if (strLine.startsWith("DROP TABLE")) {
                    createTableStarts = true;
                }
                if (strLine.contains("Dumping data for table")) {
                    break;
                }
                if (createTableStarts) {
                    stringBuilder.append(strLine).append(System.getProperty("line.separator"));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
