package com.fast.springboot.basic.designpattern.command;

/**
 * @author bowen.yan
 * @since 2016-09-15
 */
public class CommandTest {

    public static void main(String[] args) {
        // 演化成lambda过程
        /*
        Macro macro = new Macro();
        Editor editor = new EditorImpl();
        macro.record(() -> new Open(editor));
        macro.record(() -> new Save(editor));
        macro.record(() -> new Close(editor));

        Macro macro = new Macro();
        Editor editor = new EditorImpl();
        macro.record(() -> editor.open());
        macro.record(() -> editor.save());
        macro.record(() -> editor.close());
        */

        Macro macro = new Macro();
        Editor editor = new EditorImpl();
        macro.record(editor::open);
        macro.record(editor::save);
        macro.record(editor::close);

        macro.run();
    }
}
