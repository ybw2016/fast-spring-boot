package com.fast.springboot.basic.designpattern.visitor;

/**
 * @author bw
 * @since 2020-11-08
 */
public class VisitorTest {
    public static void main(String[] args) {
        Person zs = new Person("1", "zs", true);
        Person ls = new Person("2", "ls", false);
        zs.accept(new ManVisitor());
        zs.accept(new WomanVisitor());
        //ls.accept(new ManVisitor());
        //ls.accept(new WomanVisitor());
    }
}
