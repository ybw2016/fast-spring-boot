package com.fast.springboot.basic.designpattern.visitor;

/**
 * @author bw
 * @since 2020-11-08
 */
public class WomanVisitor extends PersonVisitor {
    @Override
    public void visit(Person person) {
        String detailInfo = person.getName() + "-" + (person.isGender() ? "男" : "女");
        System.out.println("WomanVisitor result:" + detailInfo);
    }
}
