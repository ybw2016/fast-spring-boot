package com.fast.tools.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import java.io.File;
import java.util.List;

/**
 * @author bowen.yan
 * @since 2019-09-09
 */
public class ExcelTool {
    public static void main(String[] args) {
        // 导入文件目录
        String filePath = "/user/Documents/cardInfo.xls";
        File file = new File(filePath);

        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);
        importParams.setTitleRows(0);
        importParams.setNeedVerfiy(false);

        //数据处理
        ExcelDataObjectHandler handler = new ExcelDataObjectHandler();
        handler.setNeedHandlerFields(new String[]{"CARDBINKEY", "ISSNAME"});
        importParams.setDataHanlder(handler);

        List<Object> list = ExcelImportUtil.importExcel(file, CardInfoVo.class, importParams);

        System.out.println(list);
    }
}
