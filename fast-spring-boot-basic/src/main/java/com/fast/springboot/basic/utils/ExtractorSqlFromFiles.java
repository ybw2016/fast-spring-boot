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

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_DIR;

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
public class ExtractorSqlFromFiles {
    private static final String RAW_FILE_DIR = USER_WORK_DIR + "db_export/";
    private static final String NEW_SQL_FILE_PATH = USER_WORK_DIR + "db_all_tables.sql";
    private static final String REFRESH_NEW_SQL_FILE_PATH = USER_WORK_DIR + "db_all_tables_New.sql";
    private static final boolean INCLUDE_INSERT_SQLS = true;

    public static void main(String[] args) {
        // 1. 合并多个.sql并写入到StringBuilder
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

        // 2. 从StringBuilder写入到一个文件
        File sqlFile = new File(NEW_SQL_FILE_PATH);
        if (sqlFile.exists()) {
            try {
                sqlFile.delete();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try (PrintStream printStream = new PrintStream(new FileOutputStream(sqlFile))) {
            printStream.println(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 3. 清洗一个文件
        RefreshSqlFile.refreshFile(NEW_SQL_FILE_PATH, REFRESH_NEW_SQL_FILE_PATH);
        log.info("清洗完成！");
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
                if (!INCLUDE_INSERT_SQLS && strLine.contains("ENGINE=InnoDB")) {
                    //String currLine = strLine.substring(0, strLine.indexOf(";"));
                    appendLine(stringBuilder, strLine);
                    break;
                } else {
                    if (strLine.startsWith("INSERT INTO")) {
                        appendLine(stringBuilder, strLine);
                        break;
                    }
                }
                if (strLine.startsWith("SET @@SESSION")
                        || strLine.startsWith("LOCK TABLES")
                        || strLine.startsWith("UNLOCK TABLES")
                        ) {
                    continue;
                }
//                if (strLine.contains("Dumping data for table")) {
//                    break;
//                }
                if (createTableStarts) {
                    appendLine(stringBuilder, strLine);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static void appendLine(StringBuilder stringBuilder, String sqlLine) {
        stringBuilder.append(sqlLine).append(System.getProperty("line.separator"));
    }
}
