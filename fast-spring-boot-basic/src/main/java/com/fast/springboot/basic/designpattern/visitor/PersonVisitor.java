package com.fast.springboot.basic.designpattern.visitor;

/**
 * @author bw
 * @since 2020-11-08
 */
public abstract class PersonVisitor {
    public abstract void visit(Person person);
}
