# Java新特性
## java7
（1）自动资源管理 ResourceBasicUsage
（2）增强的对实例创建（diamond）的类型推断
例如：Map<String, List<String>> map = new HashMap<>();  
（3）数字字面量下划线支持
int one_million = 1_000_000;
（4）switch String支持
String ss="sfsdf";
switch(ss){
}
（5）suppress异常 ReadFile
（6）捕获多个异常(新语法) ReadFile
（7）自动回收资源  ResourceBasicUsage
（8） FileChannel FileChannelUsage
（9） ByteBuffer ByteBufferUsage
（10）fork/join
## java8
常量池从永久区拿出来，单独用metaspace元数据区
（1）lamba 表达式 Lambda
（2）接口默认方法 VehicleMethod
（3）构造器引用  Test1
例如：Person::new   String:new
（4）日期时间 API LocalDateTime
（5）Base64
（6）Optional 类 OptionalTest
(7) strem StreamTest
(8) 函数式接口 FunctionalInterfaceTest
## Java9的新特性
（1）G1成为默认垃圾回收器
（2）HTTP/2 Client(Incubator)
支持HTTP2，同时改进httpclient的api，支持异步模式。
（3）java9重磅引入了模块化系统，自身jdk的类库也模块化。
（4）新引入的jlink可以精简化jdk的大小，外加Alpine Linux的docker镜像，可以大大减少java应用的docker镜像大小，
同时也支持了Docker的cpu和memory限制(Java SE 8u131及以上版本开始支持)。

