package com.fast.springboot.basic.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author bowen.yan
 * @date 2019-03-08
 */
public class FileHelper {
    // 复制文件
    public static void copyFile(File srcPath, File destPath) throws IOException {
        // 新建文件输入流、输出流并对它进行缓冲
        try (FileInputStream fileInputStream = new FileInputStream(srcPath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(destPath);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {
            // 缓冲数组
            byte[] buffer = new byte[1024 * 5];
            int length;
            while ((length = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, length);
            }
            // 刷新此缓冲的输出流，立即更新到文件
            bufferedOutputStream.flush();
        }
    }

    // 复制文件夹
    public static void copyDirectory(String srcDir, String destDir) throws IOException {
        // 确保目标目录存在
        ensureDirExists(srcDir);

        // 获取源文件夹当前下的文件或目录
        for (File srcFile : (new File(srcDir)).listFiles()) {
            if (srcFile.isFile()) {
                // 目标文件
                File destFile = new File(new File(destDir).getAbsolutePath() + File.separator + srcFile.getName());
                copyFile(srcFile, destFile);
            }
            if (srcFile.isDirectory()) {
                // 准备复制的源文件夹
                String subSrcDir = srcDir + File.separator + srcFile.getName();
                // 准备复制的目标文件夹
                String subDestDir = destDir + File.separator + srcFile.getName();
                copyDirectory(subSrcDir, subDestDir);
            }
        }
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param fileDir String
     */
    public static void deleteDirectory(String fileDir) {
        File file = new File(fileDir);
        // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
        if (!file.isDirectory()) {
            file.delete();
        } else if (file.isDirectory()) {
            String[] fileNames = file.list();
            for (int i = 0; i < fileNames.length; i++) {
                File delFile = new File(fileDir + File.separator + fileNames[i]);
                if (!delFile.isDirectory()) {
                    delFile.delete();
                    // System.out.println(delFile.getAbsolutePath() + "删除文件成功");
                } else if (delFile.isDirectory()) {
                    deleteDirectory(fileDir + File.separator + fileNames[i]);
                }
            }
            // System.out.println(file.getAbsolutePath() + "删除成功");
            file.delete();
        }
    }

    public static void ensureDirExists(String fileDir) {
        (new File(fileDir)).mkdirs();
    }
}
