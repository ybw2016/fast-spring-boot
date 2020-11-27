package com.fast.springboot.basic.reflect.superextends;

/**
 * @author bw
 * @since 2020-11-27
 */
public class SuperExtendsTest {
    //Lev 1
    private static class Food {
    }

    //Lev 2
    private static class Fruit extends Food {
    }

    private static class Meat extends Food {
    }

    //Lev 3
    private static class Apple extends Fruit {
    }

    private static class Banana extends Fruit {
    }

    private static class Pork extends Meat {
    }

    private static class Beef extends Meat {
    }

    //Lev 4
    private static class RedApple extends Apple {
    }

    private static class GreenApple extends Apple {
    }

    public static void main(String[] args) {
        /*
        Plate<? extends Fruit> plateFruit = new Plate<Apple>(new Apple());

        //不能存入任何元素
        plateFruit.set(new Fruit());    //Error
        plateFruit.set(new Apple());    //Error

        //读取出来的东西只能存放在Fruit或它的基类里。
        Fruit newFruit1 = plateFruit.get();
        Object newFruit2 = plateFruit.get();
        Apple newFruit3 = plateFruit.get();    //Error
        */
    }
}
