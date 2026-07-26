## MeowMeow 

一个基于 Mod Core Package / Minecraft 的 Minecraft 插件服务端，支持加载插件，轻量、简单 且完整 喵呜~

## 为什么不要选择我们 喵呜~
不要选择我们，这没有任何的 api，你只可以通过 net.minecraft 来进行插件开发 喵呜~

## !!! Warning !!! (必读) 喵呜~
在 Release 包含完整的 net.minecraft 源代码，谨慎使用!!!喵呜~
当然，源代码里可没有喵~

## 特性 喵呜~

- 加载插件（支持 `plugin.yml`）
- 基于 NMS（直接操作 Minecraft 内部）
- 使用 SnakeYAML 解析配置
- 使用 Log4j 输出日志
- 没有 Bukkit API——直接使用 NMS

## 依赖 喵呜~

- Java 8+
- SnakeYAML 1.33（Apache 2.0）
- Log4j 2.14.1（Apache 2.0）

## 编译
下载源代码和 Release 里的预编译版本，打开 IDEA 新建项目，把src文件夹拖入项目文件夹，随后找到文件(F)-项目结构...-库，导入下载好的预编译版本，接下来就可以了 喵呜~

## 使用 喵呜~

Debian系
```debian系
bash-5.3# apt install openjdk-11-jdk # 安装 Java
bash-5.3# java -jar meowmeow_server.jar # 启动服务端
```
Arch Linux
``` Arch系
[shen@FUCKU ~]$ sudo pacman -Sy # 升级软件源
[shen@FUCKU ~]$ sudo pacman -S openjdk-11 # 安装 Java
shen@FUCKU:~$ java -jar meowmeow_server.jar # 启动服务端
```
红帽系
```Red Hat系
shen@FUCKU:~$ sudo dnf install -y java-11-openjdk java-11-openjdk-devel # 安装 Java
shen@FUCKU:~$ java -jar meowmeow_server.jar # 启动服务端
```

通用 (需要登录且为Java8)
```通用系
打开firefox 访问https://www.oracle.com/cn/java/technologies/javase/javase8-archive-downloads.html
```
## 许可证

本项目使用 GPL 3.0 许可证 喵~

本项目包含以下依赖：

· SnakeYAML(Apache 2.0)
· Log4j (Apache 2.0)
. Minecraft Source Code 1.8.8 MCP (for reference only; all rights belong to Mojang Studios)

详细信息请查看 NOTICE 文件。

## 作者 喵呜~

Minecraft_Sam111（MeowMeow 项目）喵呜~

## 致谢 喵呜~
· SnakeYAML 作者
· Log4j 作者
· 所有贡献者
