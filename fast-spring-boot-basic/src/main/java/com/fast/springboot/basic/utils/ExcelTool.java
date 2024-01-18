package com.fast.springboot.basic.utils;

import com.alibaba.excel.EasyExcel;
import com.fast.springboot.basic.excel.ExcelData;
import com.fast.springboot.basic.excel.ExcelDataReadListener;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * @author yanbowen
 * @date 2024-01-18
 */
public class ExcelTool {
    private static final String RAW_FILE_DIR = USER_WORK_FILE_DIR + "/logFileByExcel.xlsx";

    public static void main(String[] args) {
        // 这里需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(RAW_FILE_DIR, ExcelData.class, new ExcelDataReadListener()).sheet().doRead();
    }
}
