package com.fast.springboot.basic.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_SQL_DIR;

/**
 * 从mysql数据库表中提取并合并到一个文件中
 * 说明 ：使用数据库工具将建表语句导出时，会生成以数据库表命名的文件，如:tableName.sql，多个文件中不便于合并
 * 从工具的作用是：从多个文件中提取有效的sql文件，并合并成一个文件
 *
 * 备注：当导出的文件中特殊语法太多时，也可以修改数据库工具设置，从而使导出的sql文件很干净。
 *
 * @author bowen.yan
 * @date 2019-01-02
 */
@Slf4j
public class RefreshSqlFile {
    private static final String RAW_SQL_FILE_PATH = USER_WORK_SQL_DIR + "db_all_tables.sql";
    private static final String NEW_SQL_FILE_PATH = USER_WORK_SQL_DIR + "db_all_tables_New.sql";

    public static void main(String[] args) throws FileNotFoundException {
        refreshFile(RAW_SQL_FILE_PATH, NEW_SQL_FILE_PATH);
    }

    public static void refreshFile(String rawFilePath, String refreshedFilePath) throws FileNotFoundException {
        File file = new File(rawFilePath);
        if (!file.exists()) {
            log.error("目录不存在");
            return;
        }

        StringBuilder createSb = new StringBuilder();
        StringBuilder insertSb = new StringBuilder();
        readToString(file.getPath(), createSb, insertSb);

        File sqlFile = new File(refreshedFilePath);
        if (sqlFile.exists()) {
            try {
                sqlFile.delete();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try (PrintStream printStream = new PrintStream(new FileOutputStream(sqlFile))) {
            printStream.println(createSb.toString());
            printStream.println(insertSb.toString());
        }
    }


    private static void readToString(String filePath, StringBuilder createSb, StringBuilder insertSb) {
        File file = new File(filePath);
        String strLine;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((strLine = bufferedReader.readLine()) != null) {
                // 滤掉无用行
                if (strLine.startsWith("/*!40")
                    || strLine.startsWith("--")) {
                    continue;
                }
                if (strLine.toLowerCase().contains("insert into") || strLine.toLowerCase().contains("replace into")) {
                    appendLine(insertSb, strLine);
                } else {
                    if (strLine.trim().equals(StringUtils.EMPTY)) {
                        continue;
                    }
                    // drop table 换行
                    if (strLine.contains("DROP TABLE")) {
                        appendLine(createSb, "");
                    }
                    appendLine(createSb, strLine);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendLine(StringBuilder stringBuilder, String sqlLine) {
        stringBuilder.append(sqlLine).append(System.getProperty("line.separator"));
    }
}
