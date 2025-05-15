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
 * 文本切割工具（切割数据库查询出来的列后生成目标sql）
 * 1. select * from $table_name; 导出生产数据; 并替换$table_name -> $table_name1
 * 2. 在本地数据库创建$table_name1并插入步骤1导出的数据;
 * 3. select id, value from $table_name; 导出生产数据; 并替换$table_name -> $table_name1
 * 4. 将value中的无用字段用Sublime全部替换掉;
 * 5. 利用此工具生成目标sql并update $table_name1;
 * 6. select value from table_name1; 发现数据达到期望值则sql成功;
 *
 * @author bw
 * @since 2022-01-07
 */
public class ExtractFromFileBySplit {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_FILE_DIR + "/logFileBySplit.txt";

    public static void main(String[] args) {
        filterTextByKeyword(RAW_FILE_DIR_BAK, "$YOUR_KEYWORD");
    }

    private static void filterTextByKeyword(String filePath, String keyword) {
        List<String> textResults = Lists.newArrayList();
        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String rawStrLine;
            while ((rawStrLine = bufferedReader.readLine()) != null) {
                String[] arr = rawStrLine.split(keyword);
                String sql = String.format("update $table_name set $column_name ='%s' where id = %s;", JSON.toJSONString(arr[1]), arr[0]);
                System.out.println(sql);
                textResults.add(sql);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileExtHelper.writeToFile(textResults);

        System.out.println();
    }
}
