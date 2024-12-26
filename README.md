# maven-study
学习如何写maven-plugin

### 自定义maven-plugin的步骤
1.修改pom.xml，将packaging设置为maven-plugin
2.引入maven-plugin-api依赖
3.编写maven-plugin的类，继承AbstractMojo，实现execute方法
4.指定goal，可以采用注释方式，也可以采用注解方式，但不能混用
5.如果打包过程中会使用到maven-plugin-plugin插件，如果版本过低要升级版本
6.注解方式，需要引入maven-plugin-annotations依赖
7.在Mojo类上配置注解@Mojo，设置name 即goal，设置defaultPhase 即默认生命周期阶段
8.如果要在Mojo类里获取运行插件的项目的基本信息，可以引入maven-project依赖
9.通过下面方式注入项目信息
```java
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;
```
10.在Mojo类里，通过getLog()方法获取日志对象
11.获取自定义配置信息，在插件execution里添加configuration
```xml
<execution>
    <id>annotation</id>
    <configuration>
        <name>hbj</name>
        <description>hello world</description>
    </configuration>
    <goals>
        <goal>annotation</goal>
    </goals>
</execution>
```
ps:configuration可以配置在插件的executions外，则为全局插件配置，会被插件execution的配置覆盖

12.配置好后，在Mojo类里，@Parameter 注解获取配置信息,重新打包运行即可打印信息
```java
    @Parameter(property = "name", required = true)
    private String name;

    @Parameter(property = "description")
    private String description;
```
13.maven插件的依赖和应用的依赖关系
从maven的插件中，我们引入了commons-lang3，我们可以分析应用的依赖，看到应用中没有commons-lang3的依赖。
同时，也说明了，maven插件的依赖由自己的类加载器来加载，而应用则使用应用类加载器来加载。
所以当我们在应用中使用插件时，我们是加载不到commons-lang3的，需要自己额外在引入。

但有一种情况，需要应用引入插件的依赖，就是当插件的依赖不是显示传递时，即scope = provided或者optional = true时。
此时，除了maven默认的插件，其他都需要应用类显示声明依赖，但只需要在plugin声明dependencies即可。
```xml
<build>
        <plugins>
            <plugin>
                <groupId>org.example</groupId>
                <artifactId>custom-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <configuration>
                    <description>hello world</description>
                    <toAbsoluteDir/>
                </configuration>
                <executions>
                    <execution>
                        <id>move</id>
                        <configuration>
                            <toAbsoluteDir>/Users/hbj/Study/maven-study/lib</toAbsoluteDir>
                        </configuration>
                        <goals>
                            <goal>move</goal>
                        </goals>
                    </execution>
                </executions>
                <!-- 在此处声明需要的依赖 -->
                <dependencies>
                    <dependency>
                        <groupId>org.apache.commons</groupId>
                        <artifactId>commons-lang3</artifactId>
                        <version>3.17.0</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```

