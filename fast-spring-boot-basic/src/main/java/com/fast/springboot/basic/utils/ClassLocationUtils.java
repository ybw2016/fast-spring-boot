package com.fast.springboot.basic.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * =========Maven类包冲突终极三大解决技巧 mvn dependency:tree=========
 * 1. mvn dependency:tree -Dverbose -Dincludes=$conflict_jar_package
 * 2. exclusions
 *         <dependency>
 *             <groupId>commons-logging</groupId>
 *             <artifactId>commons-logging</artifactId>
 *             <version>1.1</version>
 *             <exclusions>
 *                 <exclusion>
 *                     <groupId>javax.servlet</groupId>
 *                     <artifactId>servlet-api</artifactId>
 *                 </exclusion>
 *             </exclusions>
 *         </dependency>
 * 3. ClassLocationUtils.where(javax.servlet.ServletContext.class)
 * 4. reimport重新起开启项目的方式,建议采用idea自带的功能,File->Invalidate Caches 功能直接完成清除idea cache
 *
 * refer：https://blog.csdn.net/sun_wangdong/article/details/51852113
 * @author : chenxh
 * @date: 13-10-31
 */
public class ClassLocationUtils {

    public static void main(String[] args) {
        System.out.println(ClassLocationUtils.where(javax.servlet.ServletContext.class));
    }

    /**
     * 获取类所有的路径
     */
    public static String where(final Class cls) {
        if (cls == null) throw new IllegalArgumentException("null input: cls");
        URL result = null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        final ProtectionDomain pd = cls.getProtectionDomain();
        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            if (cs != null) result = cs.getLocation();
            if (result != null) {
                if ("file".equals(result.getProtocol())) {
                    try {
                        if (result.toExternalForm().endsWith(".jar") ||
                            result.toExternalForm().endsWith(".zip"))
                            result = new URL("jar:".concat(result.toExternalForm())
                                .concat("!/").concat(clsAsResource));
                        else if (new File(result.getFile()).isDirectory())
                            result = new URL(result, clsAsResource);
                    } catch (MalformedURLException ignore) {
                    }
                }
            }
        }
        if (result == null) {
            final ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ?
                clsLoader.getResource(clsAsResource) :
                ClassLoader.getSystemResource(clsAsResource);
        }
        return result.toString();
    }
}