package com.fast.springboot.basic.utils;

import cn.hutool.core.io.FileUtil;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

import static com.fast.springboot.basic.utils.UserConstants.USER_WORK_FILE_DIR;

/**
 * 从项目/模块中抽取项目中不存在的key
 *
 * @author yanbowen
 * @date 2023-12-19
 */
public class KeyExistInFileCheckTool {
    private static final String RAW_FILE_DIR_BAK = USER_WORK_FILE_DIR + "/logFileByKeyExistInFileCheck.txt";

    public static void main(String[] args) {
        List<String> keyList = getFindingKeys();
        readFiles(keyList, "$subFileDir");
        System.out.println("\n\n不存在的keys如下：");
        keyList.forEach(keyWord -> System.out.println(keyWord.replaceAll("\"", "")));
    }

    public static void readFiles(List<String> keyList, String fileSubPath) {
        String srcDirectory = String.format("%s%s", UserConstants.USER_WORK_SRC_FILE_DIR, fileSubPath);
        List<File> files = FileUtil.loopFiles(srcDirectory, pathname -> {
            if (pathname.getName().indexOf(".java") > -1) {
                return true;
            } else {
                return false;
            }
        });
        System.out.println(files.get(0).getAbsolutePath());
        for (int x = 0; x < files.size(); x++) {
            File file = files.get(x);
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String rowStr;
                while ((rowStr = bufferedReader.readLine()) != null) {
                    for (int i = keyList.size() - 1; i >= 0; i--) {
                        if (rowStr.contains(keyList.get(i))) {
                            keyList.remove(i);
                        }
                    }
                    if (keyList.size() == 0) {
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("文件解析异常，当前文件 = " + file.getAbsolutePath(), e);
            }
            if (keyList.size() == 0) {
                break;
            }
        }
    }

    private static List<String> getFindingKeys() {
        List<String> keys = Lists.newArrayList();
        try (FileInputStream fileInputStream = new FileInputStream(new File(RAW_FILE_DIR_BAK));
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String strLine;
            while ((strLine = bufferedReader.readLine()) != null) {
                System.out.println(String.format("--------> strLine:%s", strLine));
                //String keyWord = String.format("\"%s\"", strLine);
                //keys.add(keyWord);
                keys.add(strLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }
}
