package com.fast.springboot.basic.common;

/**
 * https://mp.weixin.qq.com/s/_yFoNeL_lOwnU4ixISwnyg
 *
 * 但是通过翻阅java虚拟机手册对class文件格式的定义以及常量池中对String类型的结构体定义我们可以知道对于索引定义了u2，就是无符号占2个字节，
 * 2个字节可以表示的最大范围是2^16 -1 = 65535。其实是65535，但是由于JVM需要1个字节表示结束指令，所以这个范围就为65534了。
 * 超出这个范围在编译时期是会报错的，但是运行时拼接或者赋值的话范围是在整形的最大范围。
 *
 * @author bw
 * @since 2020-12-06
 */
public class StringLengthTest {

    public static void main(String[] args) {
        System.out.println(getString(65534));
    }

    public static String getString(int length) {
        String str = "";
        for (int i = 0; i < length; i++) {
            str += "s";
        }
        return str;
    }
}
