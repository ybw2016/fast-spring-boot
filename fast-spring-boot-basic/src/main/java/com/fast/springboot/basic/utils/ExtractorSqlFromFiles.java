package com.fast.springboot.basic.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

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
    private static final String RAW_FILE_DIR_BAK = USER_WORK_DIR + "db_export_bak/";
    private static final String NEW_SQL_FILE_PATH = USER_WORK_DIR + "db_all_tables_raw.sql";
    private static final String REFRESH_NEW_SQL_FILE_PATH = USER_WORK_DIR + "db_all_tables_NEW.sql";
    private static final boolean INCLUDE_INSERT_SQLS = true;

    public static void main(String[] args) throws IOException {
        // 1. 合并多个.sql并写入到StringBuilder
        File file = new File(RAW_FILE_DIR);
        if (!file.exists()) {
            log.error("目录不存在");
            return;
        }

        Map<String, String> sqlFileMap = new TreeMap<>();

        int exported = 0;
        StringBuilder ignoreStringBuilder = new StringBuilder();
        for (File currFile : file.listFiles()) {
            if (!currFile.getName().endsWith(".sql")) {
                appendLine(ignoreStringBuilder, currFile.getName());
                continue;
            }

            log.info("filePath -> {}", currFile.getPath());
            String fileContent = readSqlFileToString(currFile.getPath());
            sqlFileMap.put(currFile.getName(), fileContent);
            exported++;
        }

        // 2. 从sqlFileMap写入到一个文件
        File sqlFile = new File(NEW_SQL_FILE_PATH);
        sqlFile.delete();
        try (PrintStream printStream = new PrintStream(new FileOutputStream(sqlFile))) {
            sqlFileMap.forEach((fileName, sqlFileContent) -> {
                printStream.println(sqlFileContent);
                printStream.println(System.getProperty("line.separator"));
            });
        }

        // 3. 清洗一个文件
        RefreshSqlFile.refreshFile(NEW_SQL_FILE_PATH, REFRESH_NEW_SQL_FILE_PATH);
        log.info("清洗完成！-> 总共:{}个，导出{}个", file.listFiles().length, exported);
        log.info("忽略文件如下：{}", ignoreStringBuilder.toString());

        // 备份原目录下的sql文件
        FileHelper.copyDirectory(RAW_FILE_DIR, RAW_FILE_DIR_BAK);
        log.info("原始sql文件备份完成！-> srcDir:{}, destDir:{}", RAW_FILE_DIR, RAW_FILE_DIR_BAK);

        // 删除原目录下的sql文件
        FileHelper.deleteDirectory(RAW_FILE_DIR);
        log.info("原始sql文件删除完成！-> srcDir:{}", RAW_FILE_DIR);

        // 创建原始目录，以便下次使用
        //FileHelper.ensureDirExists(RAW_FILE_DIR);
    }

    private static String readSqlFileToString(String filePath) {
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
                    if (strLine.startsWith("INSERT INTO")|| strLine.startsWith("REPLACE INTO")) {
                        createTableStarts = false;
                        appendLine(stringBuilder, strLine);
                        //break;
                    }
                }
                if (strLine.startsWith("SET @@SESSION")
                    || strLine.startsWith("LOCK TABLES")
                    || strLine.startsWith("UNLOCK TABLES")
                    || strLine.startsWith("/*!40")
                    || strLine.startsWith("/*!50")
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
