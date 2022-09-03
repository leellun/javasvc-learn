# java注解开发
开发自定义注解自定义注解的语法规则：
(1)使用@interface关键字定义注解,注意关键字的位置使用@interface自定义注解时自动继承了java.lang.annotation.Annotation接由编译程序自动完成其他细节。在定义注解时,不能继承其他的注解或接口
(2)成员以无参无异常的方式声明,注意区别一般类成员变量的声明其中的每一个方法实际上声明了一个配置参数,方法的名称就是参数的名称
(3)可以使用default关键字为成员指定一个默认值
(4)成员的类型是受限的,合法的类型包括原始类型以及String、Class, Annotation, Enumeration(Java的基本数据类型有8种byte(字节)、short (短整型)、int(整数型)、long(长整型)、float(单精度浮点数类型)、double(双精度浮点数类型)、char(字符类型)、boolean(布尔类型))
(5)注解类可以没有成员,没有成员的注解称为标识注解,例如JDK注解中的@Override. @Deprecation
(6)如果注解只有一个成员,并且把成员取名为value(),则在使用时可以忽略成员名和赋值号“=”,例如jdk注解的@SuppviseWarnings;如果成员名 不为value,则使用时需要指明成员名和赋值号“="

元注解：
指定注解的功能,此时就需要用元注解对你注解进行说明了。Java5.0定义了4个标准的meta-annotation类型,它们被用来提供对其他annotation类型作说明

@Target
@Target说明了Annotation所修饰的对象范围:即注解的作用域,用于说明注解的使用范围(即注解可以用在什么地方,比如类,的注解,方法的注解,成员变量的注解等等)
注意:如果Target元注解没有出现,那么定义的注解可以应用于程序任何元素取值是在java.lang.annotation.ElementType这个枚举中规定的:
1. Constructor:用于描述构造器
2. FIELD:用于描述域
3. LOCAL_VARIABLE:用于描述局部变量
4. METHOD:用于描述方法
5. PACKAGE:用于描述包
6. PARAMETER:用于描述参数
7. TYPE:用于描述类、接口(包括注解类型)或enum声明

@Retention
@Retention定义了该Annotation被保留的时间长短;
(1)某些Annotation定义了该Annotation仅出现在源代码中,而被编译器丢弃;
(2)而另一些却被编译在class文件中,编译在class文件中的Annotation可能被虚拟机忽略
(3)而另一些在class中被装载时被读取(请注意并不影响class的执行,因为Annotation与class在使用上是被分离的)使用这个meta-Annotation可以对Annotation的生命周期限制@Retention的取值是在RetentionPoicy这个枚举中规定的
1. SOURCE:在源文件中有效(即源文件保留)
2. CLASS:在class文件中有效(即class保留)
3. RUNTIME:在运行时有效

注意:注解的RetentionPolicy的属性值时RUNTIME,这样注解的处理器可以通过反射,获取到该注解的属性值,从而去做一些运行时的逻辑处理

@Documented
@Ducumented用于描述其他类型的annotation应该被作为被标注的程序员的公共的API,因此可以被例如javadoc此类的工具文档化。
Documented是一个标记注解,没有成员

@Inherited
@Inherited元注解是一个标记注解, @Inherited阐述了某个被标注的类型是被继承的。如果一个使用了@Inherited修饰的annotation类型被用于一个class,则这个annotation将被用于该class的子类
注意@Inherited annotation类型是被标注过的class的子类所继承。类并不从它实现的接口继承annotation,方法并不从它所重载的方法继承annotation
当@Inherited annotation类型标注的annotation的Retention是RetentionPolicy.RUNTIME,则反射API增强了这种继承性。如果我们使用java.lang.reflect去
查询一个@Inherited annotation类型的annotation时,反射代码检查将展开工作:检查class和其父类,直到发现指定的annotation类型被发现。或者达到类继承结构的顶层

