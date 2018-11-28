运行说明：
1. spring-cloud server启动后，输入url访问： http://localhost:8090/
2. spring-cloud provider启动后，输入url访问：
    http://localhost:8091/hello/zhangsan
    http://localhost:8091/address?addressCode=bj
3. spring-cloud consumer启动后，输入url访问：
    restTemplate.postForEntity()方式：       http://localhost:8092/hello?name=zhangsan
    Feign调用方式：                           http://localhost:8092/hello2?name=zhangsan
    Feign调用方式：                           http://localhost:8092/address?addressCode=bj
