package com.fast.springboot.basic.reflect.superextends;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 参考链接： https://www.cnblogs.com/JasonLGJnote/p/11159869.html
 *
 * 一句话总结 super & extends：PECS原则（Producer Extends Consumer Super）
 * 即：
 * 1. 返回的List<T>集合如果希望是只读的，则用extends;
 * 2. 返回的List<T>集合可以往里面添加元素，则用super;
 *
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

    private static class Orange extends Fruit {
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

    public static List<? extends Fruit> getExtendsFruits() {
        List<Fruit> extendsFruits = Lists.newArrayList(
            new Apple(),
            new Banana()
        );
        return extendsFruits;
    }

    private static void testReadExtendsFruits() {
        // 运行结果：
        //com.fast.springboot.basic.reflect.superextends.SuperExtendsTest$Apple
        //com.fast.springboot.basic.reflect.superextends.SuperExtendsTest$Banana
        getExtendsFruits().forEach(extendsFruit -> System.out.println(extendsFruit.getClass().getName()));
    }

    public static List<? super Fruit> getSuperFruits() {
        List<Fruit> superFruits = Lists.newArrayList(
            new Apple(),
            new Banana()
        );
        return superFruits;
    }

    private static void testSetSuperFruits() {
        List<? super Fruit> superFruits = getSuperFruits();
        superFruits.add(new Orange());

        // 运行结果：
        //com.fast.springboot.basic.reflect.superextends.SuperExtendsTest$Apple
        //com.fast.springboot.basic.reflect.superextends.SuperExtendsTest$Banana
        //com.fast.springboot.basic.reflect.superextends.SuperExtendsTest$Orange
        superFruits.forEach(superFruit -> System.out.println(superFruit.getClass().getName()));
    }

    public static List<Fruit> getFruits() {
        List<Fruit> superFruits = Lists.newArrayList(
            new Apple(),
            new Banana()
        );
        return superFruits;
    }

    private static void testFruits() {
        List<Fruit> fruits = getFruits();
        fruits.add(new Orange());

        // 运行结果：
        //com.fast.springboot.basic.reflect.superextends.SuperExtendsTest$Apple
        //com.fast.springboot.basic.reflect.superextends.SuperExtendsTest$Banana
        //com.fast.springboot.basic.reflect.superextends.SuperExtendsTest$Orange
        fruits.forEach(fruit -> System.out.println("CommonFruit-" + fruit.getClass().getName()));
    }

    public static void main(String[] args) {
        System.out.println("----------TEST extends----------");
        testReadExtendsFruits();// 只能读
        System.out.println();

        System.out.println("----------TEST super----------");
        testSetSuperFruits();// 能写
        System.out.println();

        System.out.println("----------TEST common fruits----------");
        testFruits();// 能读、能写
        System.out.println();

        /*
        //
        ====================> <? extends T>：是指 “上界通配符（Upper Bounds Wildcards）” <====================
        原因是编译器只知道容器内是Fruit或者它的派生类，但具体是什么类型不知道。可能是Fruit？可能是Apple？
        也可能是Banana，RedApple，GreenApple？编译器在看到后面用Plate赋值以后，盘子里没有被标上有“苹果”。
        [而是标上一个占位符：CAP#1，来表示捕获一个Fruit或Fruit的子类，具体是什么类不知道，代号CAP#1]。
        然后无论是想往里插入Apple或者Meat或者Fruit编译器都不知道能不能和这个CAP#1匹配，所以就都不允许。
        Plate<? extends Fruit> plateFruit = new Plate<Apple>(new Apple());

        ------>   类型占位符 CAP#1  !=   具体的子类型（Apple、RedApple等）

        //不能存入任何元素
        plateFruit.set(new Fruit());    //Error
        plateFruit.set(new Apple());    //Error

        //读取出来的东西只能存放在Fruit或它的基类里。
        Fruit newFruit1 = plateFruit.get();
        Object newFruit2 = plateFruit.get();
        Apple newFruit3 = plateFruit.get();    //Error




        ====================> <? super T>：是指 “下界通配符（Lower Bounds Wildcards）” <====================
        下界<? super T>不影响往里存，但往外取只能放在Object对象里
        使用下界<? super Fruit>会使从盘子里取东西的get( )方法部分失效，只能存放到Object对象里。set( )方法正常。
        Plate<? super Fruit> pfruit=new Plate<Fruit>(new Fruit());

        //存入元素正常
        pfruit.set(new Fruit());
        pfruit.set(new Apple());

        //读取出来的东西只能存放在Object类里。
        Apple newFruit3=pfruit.get();    //Error
        Fruit newFruit1=pfruit.get();    //Error
        Object newFruit2=pfruit.get();

        因为下界规定了元素的最小粒度的下限，实际上是[放松了容器元素的类型控制]。既然元素是Fruit的基类，
        那往里存粒度比Fruit小的都可以。但往外读取元素就费劲了，只有所有类的基类Object对象才能装下。但这样的话，元素的类型信息就全部丢失。
        */
    }
}
