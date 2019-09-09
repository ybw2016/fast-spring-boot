package com.fast.tools.service;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelDataObjectHandler extends ExcelDataHandlerDefaultImpl<CardInfoVo> {
    @Override
    public Object importHandler(CardInfoVo obj, String name, Object value) {
        log.info(name + ":" + value);
        return super.importHandler(obj, name, value);
    }
}
