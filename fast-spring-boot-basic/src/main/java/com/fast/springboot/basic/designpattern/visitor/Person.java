package com.fast.springboot.basic.designpattern.visitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bw
 * @since 2020-11-08
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {
    private String id;
    private String name;
    private boolean gender;

    public void accept(PersonVisitor visitor) {
        System.out.println(String.format("%s visit our system", visitor.getClass().getSimpleName()));
        visitor.visit(this);
    }
}
