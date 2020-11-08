package com.fast.springboot.basic.designpattern.command;

/**
 * @author bowen.yan
 * @since 2016-09-17
 */
public class EditorImpl implements Editor {
    @Override
    public void open() {
        System.out.println("open file!");
    }

    @Override
    public void save() {
        System.out.println("save file!");
    }

    @Override
    public void close() {
        System.out.println("close file!");
    }
}
