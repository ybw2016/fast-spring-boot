package com.fast.springboot.basic.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 文件辅助工具（输出到文件中）
 *
 * @author yanbowen
 * @date 2022-10-26
 */
public class FileExtHelper {
    private static final String SAVE_TEXT_FILE_PATH = USER_WORK_FILE_DIR + "/logFileOutPutResult.txt";

    public static void writeToFile(List<String> texts) {
        writeToFile(SAVE_TEXT_FILE_PATH, texts);
    }

    public static void writeToFile(String filePath, List<String> texts) {
        try {
            // 2、输出到文件中
            File textFile = new File(filePath);
            textFile.delete();
            try (PrintStream printStream = new PrintStream(new FileOutputStream(textFile))) {
                texts.forEach(printStream::println);
            }
            System.out.println("———— 输出到文件完成 ————");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
