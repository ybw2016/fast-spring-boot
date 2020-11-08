package com.fast.springboot.basic.designpattern.visitor;

/**
 * @author bw
 * @since 2020-11-08
 */
public class ManVisitor extends PersonVisitor {
    @Override
    public void visit(Person person) {
        String detailInfo = person.getId() + "-" + person.getName();
        System.out.println("ManVisitor result:" + detailInfo);
    }
}
