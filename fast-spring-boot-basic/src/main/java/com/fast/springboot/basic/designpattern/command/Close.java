package com.fast.springboot.basic.designpattern.command;

/**
 * @author bowen.yan
 * @since 2016-09-17
 */
public class Close implements Action {
    private Editor editor;

    public Close(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        editor.close();
    }
}
