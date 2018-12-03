一。运行说明：
1. spring-cloud server启动后，输入url访问： http://localhost:8090/
2. spring-cloud provider启动后，输入url访问：
    http://localhost:8091/hello/zhangsan
    http://localhost:8091/address?addressCode=bj
3. spring-cloud consumer启动后，输入url访问：
    restTemplate.postForEntity()方式：       http://localhost:8092/hello?name=zhangsan
    Feign调用方式：                           http://localhost:8092/hello2?name=zhangsan
    Feign调用方式：                           http://localhost:8092/address?addressCode=bj

二。feign集成自定义编码器、解码器;
1. 验证provider请求地址: (POST JSON)
    1.1. http://localhost:8091/user/user-info?username=zhangsan&mobile=13811112222
    1.2. http://localhost:8091/user/user-info-list
        {
            "username":"zhangsan",
            "pageNo":1,
            "pageSize":3
        }

2. 测试spring-consumer:
    2.1. http://localhost:8092/user/user-info?username=zhangsan&mobile=13811112222
    2.2. http://localhost:8092/user/user-info-list
        {
            "username":"zhangsan",
            "pageNo":1,
            "pageSize":3
        }

