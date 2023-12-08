# apiclient

简易基于http的api请求客户端。直接使用HttpURLConnection，支持keepalives。支持map和pojo作为请求参数。支持设置header。

# Quick start
1.添加依赖
```xml
<dependency>
    <groupId>io.github.javaservergroup</groupId>
    <artifactId>apiclient</artifactId>
    <version>1.1.0</version>
</dependency>
```
2.使用
```java
Api().get("http://www.example.org");
or
Api().post("http://www.example.org");
```

## 方法介绍
### header方法。当发送的请求需要带上header:
```java
Map<String, String> header = new HashMap<>();
header.put("Authorization", "Basic xxx");
Api().header(header).post("http://www.example.org");
```
### param方法。当请求需要带上参数:
带pojo参数：
```java
public class User{
  private String name;
  ...
}
...
User user = new User();
user.setName("Andy");
Api().param(user).post("http://www.example.org");
```
带Map参数（推荐：Map&lt;String, Object&gt;）：
```java
//普通参数
Map<String, Object> user = new HashMap<>();
user.put("user", "Andy");
Api().param(user).post("http://www.example.org");
```
```java
//发文件
Map<String, Object> uploadImgParam = new HashMap<>();
uploadImgParam.put("img", new File("~/photo.jpg"));
uploadImgParam.put("fileName", "myphoto");
Api().param(uploadImgParam).post("http:/www.example.org");
```
# REST post
可以调用restPost直接发送rest风格的post请求
```java
User user = new User();
user.setName("Andy");

String result = Api().param(people).restPost("http://www.xxx.com/restpost");
```
# 异常：StatusCodeNot200Exception
当请求返回的status code返回的码不在大于等于200，小于400的时候，会抛出一个自定义的运行时异常:StatusCodeNot200Exception
