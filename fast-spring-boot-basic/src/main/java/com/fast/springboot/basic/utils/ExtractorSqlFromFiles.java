package com.fast.springboot.basic.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_SQL_DIR;

/**
 * 从mysql数据库表中提取并合并到一个文件中
 * 说明 ：使用数据库工具将建表语句导出时，会生成以数据库表命名的文件，如:tableName.sql，一个表名一个文件，因此多个文件中不便于合并
 * 工具解决的问题：从多个文件中提取有效的sql文件，并合并成一个文件
 * <p>
 * 备注：当导出的文件中特殊语法太多时，也可以修改数据库工具设置，从而使导出的sql文件很干净。
 *
 * @author bowen.yan
 * @date 2019-01-02
 */
@Slf4j
public class ExtractorSqlFromFiles {
    private static final String RAW_FILE_DIR = USER_WORK_SQL_DIR + "db_export/";
    private static final String RAW_FILE_DIR_BAK = USER_WORK_SQL_DIR + "db_export_bak/";
    private static final String NEW_SQL_FILE_PATH = USER_WORK_SQL_DIR + "db_all_tables_raw.sql";
    private static final String REFRESH_NEW_SQL_FILE_PATH = USER_WORK_SQL_DIR + "db_all_tables_NEW.sql";
    private static final boolean INCLUDE_INSERT_SQLS = true;
    private static final boolean DELETE_RAW_FILE_DIR = false;

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
        List<TableNameSnap> tableNameSnaps = Lists.newArrayList();
        for (File currFile : file.listFiles()) {
            if (!currFile.getName().endsWith(".sql")) {
                appendLine(ignoreStringBuilder, currFile.getName());
                continue;
            }

            log.info("filePath -> {}", currFile.getPath());
            TableDetail tableDetail = extractSqlFromFile(currFile.getPath());
            if (isValidTable(tableDetail.getTableName())) {
                tableNameSnaps.add(new TableNameSnap(tableDetail.getTableName(), true));
                sqlFileMap.put(currFile.getName(), tableDetail.getAllSqlString());
                exported++;
            } else {
                tableNameSnaps.add(new TableNameSnap(tableDetail.getTableName(), false));
            }
        }

        Map<Boolean, List<TableNameSnap>> tableNameSnapMap = tableNameSnaps.stream().collect(Collectors.partitioningBy(r -> r.valid));
        if (tableNameSnapMap.isEmpty()) {
            System.out.println("当前路径下没有导出任何表");
            return;
        }

        List<String> validTableNames = getTableNames(tableNameSnapMap, true);
        List<String> invalidTableNames = getTableNames(tableNameSnapMap, false);
        System.out.println();
        System.out.println("有效的sql表————————————————————————> " + JSON.toJSONString(validTableNames));
        System.out.println("移除的sql表————————————————————————> " + JSON.toJSONString(invalidTableNames));

        // 2. 从sqlFileMap写入到一个文件
        File sqlFile = new File(NEW_SQL_FILE_PATH);
        sqlFile.delete();
        try (PrintStream printStream = new PrintStream(new FileOutputStream(sqlFile))) {
            sqlFileMap.forEach((fileName, sqlFileContent) -> {
                printStream.println(sqlFileContent);
                printStream.println(System.getProperty("line.separator"));
            });
        }
        System.out.println();

        // 3. 清洗一个文件
        RefreshSqlFile.refreshFile(NEW_SQL_FILE_PATH, REFRESH_NEW_SQL_FILE_PATH);
        int total = file.listFiles().length;
        System.out.println();
        log.info("清洗完成！-> 总共:{}个, 导出{}个, 删除{}个", total, exported, (total - exported));
        log.info("忽略文件如下：{}", ignoreStringBuilder.toString());

        // 备份原目录下的sql文件
        FileHelper.copyDirectory(RAW_FILE_DIR, RAW_FILE_DIR_BAK);
        log.info("原始sql文件备份完成！-> srcDir:{}, destDir:{}", RAW_FILE_DIR, RAW_FILE_DIR_BAK);

        // 删除原目录下的sql文件
        if (DELETE_RAW_FILE_DIR) {
            FileHelper.deleteDirectory(RAW_FILE_DIR);
            // 创建原始目录，以便下次使用
            FileHelper.ensureDirExists(RAW_FILE_DIR);
            log.info("原始sql文件删除完成！-> srcDir:{}", RAW_FILE_DIR);
        } else {
            log.info("原始sql文件不删除 -> srcDir:{}", RAW_FILE_DIR);
        }
    }

//      DROP TABLE IF EXISTS `$table_name`;
//      /*!40101 SET @saved_cs_client     = @@character_set_client */;
//      /*!50503 SET character_set_client = utf8mb4 */;
//      CREATE TABLE `$table_name` (
//        `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
//        `$column_name` varchar(32) NOT NULL COMMENT '$column_desc',
//                      KEY `idx_$column_name` (`$column_name`)
//      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='$table_name_desc';
//      /*!40101 SET character_set_client = @saved_cs_client */;


    /**
     * 说明：一个文件中只包含一个建表语句，因此只返回该表的信息
     * // createTableStarts = true，识别DROP TABLE IF EXISTS `$table_name`; ————> 即创建表的起始部分
     * // createTableStarts = false，识别) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='$table_name_desc'; ————> 即创建表的末尾部分
     * // 保存创建表的语句到返回的sql容器tableSqlBuilder中
     *
     * @param filePath 文件完整路径
     * @return 文件明细（表名 + 创建表的sql语句）
     */
    private static TableDetail extractSqlFromFile(String filePath) {
        String strLine;
        boolean isCreateTableSql = false;
        StringBuilder tableSqlBuilder = new StringBuilder();
        TableDetail tableDetail = new TableDetail();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while ((strLine = bufferedReader.readLine()) != null) {
                if (isTableNameLine(strLine)) {
                    tableDetail.setTableName(getFormatTableName(strLine));
                    isCreateTableSql = true;
                }
                // sql中有插入语句的时候会走到此处，其他场景不会走到此逻辑（可忽略）
                if (!INCLUDE_INSERT_SQLS && strLine.contains("ENGINE=InnoDB")) {
                    // 找到插入语句的最后一行，找到此行后需要退出文件游标
                    appendLine(tableSqlBuilder, strLine);
                    break;
                } else {
                    // 有插入语句，有几条则提取几条到结果集中
                    if (strLine.startsWith("INSERT INTO") || strLine.startsWith("REPLACE INTO")) {
                        isCreateTableSql = false;
                        appendLine(tableSqlBuilder, strLine);
                        //break;
                    }
                }

                // 无用的sql语句过滤掉
                if (strLine.startsWith("SET @@SESSION")
                        || strLine.startsWith("LOCK TABLES")
                        || strLine.startsWith("UNLOCK TABLES")
                        || strLine.startsWith("/*!40")
                        || strLine.startsWith("/*!50")
                ) {
                    continue;
                }

                //if (strLine.contains("Dumping data for table")) {
                //    break;
                //}

                if (isCreateTableSql) {
                    appendLine(tableSqlBuilder, strLine);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableDetail.setAllSqlString(tableSqlBuilder.toString());
        return tableDetail;
    }

    private static List<String> getTableNames(Map<Boolean, List<TableNameSnap>> tableNameSnapMap, boolean valid) {
        return tableNameSnapMap.get(valid).stream()
                .map(TableNameSnap::getTableName)
                .sorted(String::compareTo)
                .collect(Collectors.toList());
    }

    private static void appendLine(StringBuilder stringBuilder, String sqlLine) {
        stringBuilder.append(sqlLine).append(System.getProperty("line.separator"));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class TableNameSnap {
        private String tableName;
        private boolean valid;
    }

    @Data
    private static class TableDetail {
        private String tableName;
        private String allSqlString;
    }

    private static boolean isTableNameLine(String strLine) {
        return strLine.startsWith("DROP TABLE");
    }

    private static String getFormatTableName(String strLine) {
        return strLine
                .replace("DROP TABLE IF EXISTS `", "")
                .replace("`;", "");
    }

    private static Pattern BAK_FILE_BY_NUMBER_PATTERN = Pattern.compile("(.)+\\d+$");

    private static boolean isValidTable(String tableName) {
        return !BAK_FILE_BY_NUMBER_PATTERN.matcher(tableName).find()
                && !tableName.startsWith("_")
                && !tableName.startsWith("temp")
                && !tableName.startsWith("tmp")
                && !tableName.endsWith("_del")
                && !tableName.endsWith("_bk")
                && !tableName.endsWith("_bak")
                && !tableName.endsWith("_back")
                && !tableName.endsWith("_temp")
                && !tableName.endsWith("_tmp");
    }

    /**
     * 可单独测试表名是否有效
     */
    private static void testValidTableName() {
        System.out.println(isValidTable("t_cainfoll_cent210925"));
        System.out.println(isValidTable("t_cainfoll_cent_210925"));
        System.out.println(isValidTable("t_cainfoll_cent_bak_210925"));
        System.out.println(isValidTable("t_cainfoll_cent_bak"));
        System.out.println(isValidTable("t_cainfoll_cent"));
    }
}
