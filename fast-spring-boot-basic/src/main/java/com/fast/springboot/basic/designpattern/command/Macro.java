package com.fast.springboot.basic.designpattern.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bowen.yan
 * @since 2016-09-17
 */
public class Macro {
    private List<Action> actions;

    public Macro() {
        actions = new ArrayList<>();
    }

    public void record(Action action) {
        actions.add(action);
    }

    public void run() {
        actions.forEach(action -> action.execute());
    }
}
