package com.fast.springboot.basic.designpattern.command;

/**
 * @author bowen.yan
 * @since 2016-09-17
 */
public interface Editor {
    public void open();

    public void close();

    public void save();
}
