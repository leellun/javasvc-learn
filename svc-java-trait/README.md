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

## java11
ZGC垃圾回收器
据说这是JDK11最为瞩目的特性，没有之一，是最重磅的升级，那么ZGC的优势在哪里呢？

GC暂停时间不会超过10毫秒
既能处理几百兆的小堆，也能处理几个T的大堆
和G1相比，应用吞吐能力不会下降超过15%
为未来的GC功能和利用colord指针以及Load barriers优化奠定了基础
ZGC是一个并发、基于region、压缩型的垃圾收集器，只有root扫描阶段会STW(strop the world，停止所有线程)，
因此ZGC的停顿时间不会随着堆的增长和存活对象的增长而变长。用法：-XX:UnlockExperimentalVMOptions 
-XX:+UseZGC虽然功能如此强大，但很遗憾的是，在Windows系统的JDK中并没有提供ZGC，所以也就没有办法测试了
