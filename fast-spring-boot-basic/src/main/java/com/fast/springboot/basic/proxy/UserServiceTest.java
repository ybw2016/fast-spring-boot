package com.fast.springboot.basic.proxy;

import java.lang.reflect.Proxy;
import javax.sound.midi.Soundbank;

/**
 * @author bw
 * @since 2020-11-11
 */
public class UserServiceTest {
    public static void main(String[] args) {
        // System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true")，会在项目根目录生成class字节文件，通过反编译可以看到:
        //
        //作者：行径行
        //链接：https://www.jianshu.com/p/84ffb8d0a338
        //来源：简书
        //著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        UserService userServiceProxy = (UserService) Proxy.newProxyInstance(
            UserServiceTest.class.getClassLoader(),
            new Class[]{UserService.class},
            new UserInvocationHandler(new UserServiceImpl())
        );

        userServiceProxy.getUerName("zhangsan");
        System.out.println();
        userServiceProxy.getUerName("zhangsan",30);
    }
}
