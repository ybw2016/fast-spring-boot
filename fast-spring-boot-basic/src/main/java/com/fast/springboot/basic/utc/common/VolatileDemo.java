package com.fast.springboot.basic.utc.common;

/**
 * @author bw
 * @since 2020-11-18
 */
public class VolatileDemo {
/*
    Happens-before法则:
    Java存储模型有一个happens-before原则，就是如果动作B要看到动作A的执行结果（无论A/B是否在同一个线程里面执行），那么A/B就需要满足happens-before关系。
    在介绍happens-before法则之前介绍一个概念：JMM动作（Java Memory Model Action），Java存储模型动作。
    一个动作（Action）包括：变量的读写、监视器加锁和释放锁、线程的start()和join()。后面还会提到锁的的。

    happens-before完整规则：
        （1）同一个线程中的每个Action都happens-before于出现在其后的任何一个Action。
        （2）对一个监视器的解锁happens-before于每一个后续对同一个监视器的加锁。
        （3）对volatile字段的写入操作happens-before于每一个后续的同一个字段的读操作。
        （4）Thread.start()的调用会happens-before于启动线程里面的动作。
        （5）Thread中的所有动作都happens-before于其他线程检查到此线程结束或者Thread.join()中返回或者Thread.isAlive()==false。
        （6）一个线程A调用另一个另一个线程B的interrupt（）都happens-before于线程A发现B被A中断（B抛出异常或者A检测到B的isInterrupted（）或者interrupted()）。
        （7）一个对象构造函数的结束happens-before与该对象的finalizer的开始
        （8）如果A动作happens-before于B动作，而B动作happens-before与C动作，那么A动作happens-before于C动作。
*/

/*
    volatile语义:
    1. volatile相当于synchronized的弱实现，也就是说volatile实现了类似synchronized的语义，却又没有锁机制。
    2. 它确保对volatile字段的更新以可预见的方式告知其他的线程。

    volatile包含以下语义：
        （1）Java 存储模型【不会对volatile指令的操作进行重排序】：这个保证对volatile变量的操作时按照指令的出现顺序执行的。
        （2）volatile变量【不会被缓存在寄存器中】（只有拥有线程可见）或者其他对CPU不可见的地方，每次总是从主存中读取volatile变量的结果。
        （3）也就是说对于volatile变量的修改，【其它线程总是可见的】，并且不是使用自己线程栈内部的变量。
        （4）也就是在happens-before法则中，对一个volatile变量的写操作后，其后的任何读操作理解可见此写操作的结果。

    volatile存在的问题：
        （1）尽管volatile变量的特性不错，但是volatile并【不能保证线程安全的】;
        （2）也就是说volatile字段的操作不是【原子性的】，volatile变量【只能保证可见性】（一个线程修改后其它线程能够理解看到此变化后的结果）;
        （3）要想保证原子性，目前为止只能【加锁】！
*/
}
