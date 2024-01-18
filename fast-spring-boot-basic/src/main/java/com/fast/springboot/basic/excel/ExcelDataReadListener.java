package com.fast.springboot.basic.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * @author yanbowen
 * @date 2024-01-18
 */
public class ExcelDataReadListener implements ReadListener<ExcelData> {
    private static final String SAVE_TEXT_FILE_PATH = USER_WORK_FILE_DIR + "/logFileOutPutResult.txt";
    private static final int BATCH_COUNT = 1000;
    private static PrintStream printStream = null;
    private List<ExcelData> excelDataList = Lists.newArrayList();

    @Override
    public void invoke(ExcelData data, AnalysisContext context) {
        excelDataList.add(data);
        if (excelDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            excelDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        System.out.println("所有excel数据解析完成");
        printStream.close();
        printStream = null;
    }

    private void saveData() {
        if (excelDataList.size() > 0) {
            appendToFile(JSON.toJSONString(excelDataList));
        }
        System.out.println("当前excel数据保存到文件完成, savedRows=" + excelDataList.size());
    }

    public static void appendToFile(String insertSql) {
        try {
            if (printStream == null) {
                printStream = new PrintStream(new FileOutputStream(new File(SAVE_TEXT_FILE_PATH)));
            }
            printStream.println(insertSql);
            System.out.println("———— 追加到文件完成 ————");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
