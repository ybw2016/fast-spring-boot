# SpringBoot 1.x
查看mvn包地址： https://mvnrepository.com

## 加解密 DecryptController
### 一。加密：（输出数据）
1. 通过EncryptOutputSerializer类实现；

### 二。解密：（解析入参）
1. HTTP GET 单个参数，通过DecryptRequestParamResolver自动解密；
2. HTTP GET PathVariable参数，通过DecryptPathVariableResolver自动解密；
3. HTTP POST FORM表单(multipart/form-data,application/x-www-form-urlencoded)，通过DecryptPostFormResolver自动解密；
4. HTTP POST JSON数据(RequestBody方式)，通过DecryptInputSerializer类实现解密；
