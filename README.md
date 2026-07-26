## MeowMeow 
Copyright (c) 2026 Minecraft_Sam111

## 说明
一个基于 Mod Core Package / Minecraft 的 Minecraft 插件服务端，支持加载插件，轻量、简单 且完整 喵呜~

## !!! Warning !!! (必读) 喵呜~
R: 这是一个基于 Mod Core Package / Minecraft 的 Minecraft 插件服务端，支持加载插件，轻量、简单 且完整 喵呜~
1. 在 Release 包含完整的 net.minecraft 源代码，谨慎使用!!! 喵呜~ 当然，源代码里可没有喵~
2. 此工具没有任何的 api，你只可以通过 net.minecraft 和 org.meow.Plugin 来进行插件开发 喵呜~
3. 代码没有任何优化，最求速度请用 Spigot/Paper 喵呜~
4. 如需Fork，请使用GNU GPLv3 否则追究 喵呜!
5. 免责声明: 仅用于测试环境，不得用于生产环境，后果自负 喵呜~
6. 如果您觉得我侵犯了您的合法权益，请联系 3750389826@qq.com (Discord: LittleShen) 进行沟通 喵呜~
7. 本项目是开源且免费的 如遇到倒卖 请联系 3750389826@qq.com (Discord: LittleShen) 进行沟通 喵呜~
8. 如果没事请不要随意联系我，除非有特殊的事情/急事 或为朋友 喵呜~

## 一个插件 喵呜~
以下是插件的示例:
```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.meow.Plugin;

public class AtinyPlugin implements Plugin {

    private static final Logger LOGGER = LogManager.getLogger(AtinyPlugin.class);

    private boolean enabled = false;
    private Object server;

    @Override
    public void onEnable() {
    LOGGER.info("Welcome to plugin world!");
    }

    @Override
    public void onDisable() {
        LOGGER.info("IDK WHAT FUCKING MEANING");
    }

    @Override
    public String getName() {
        return "AaaPlugin";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setServer(Object server) {
        this.server = server;
    }

    @Override
    public Object getServer() {
        return this.server;
    }
}
```
这是一个完整的MeowMeow插件实现，它正确实现了定义的org.meow.Plugin接口。代码没有问题 喵呜~

成员变量部分没有问题，enabled记录启用状态，server持有服务端引用，setServer方法会被你的PluginLoader在加载时自动调用，getServer则留给插件开发者使用 喵呜~

这个方法实现了接口所有要求，包括getName返回"AaaCommandPlugin"，getVersion返回"1.0.0"，以及标准的setEnabled、isEnabled、setServer、getServer方法 喵呜~

## 依赖 喵呜~

- Java: Java 7 或更高的版本 
- 系统: Windows 8+ / Linux 3.1+ / macOS X 10.10+
- 插件运行条件: 必须使用implements Plugin 来编写，且必须实现这些抽象方法: 
```java
  void onEnable();
  void onDisable();
  String getName();
  String getVersion();
  void setEnabled(boolean enabled);
  boolean isEnabled();
  void setServer(Object server);
  Object getServer();
```

## 编译
下载源代码和 Release 里的预编译版本，打开 IDEA 新建项目，把src文件夹拖入项目文件夹，随后找到文件(F)-项目结构...-库，导入下载好的预编译版本，接下来就可以了 喵呜~

## 使用 喵呜~

Debian系:
```debian系
╭─shen@FUCKU ~
╰─$ apt install openjdk-11-jdk # 安装 Java
╭─shen@FUCKU ~
╰─$ java -jar meowmeow_server.jar # 启动服务端
```
Arch系:
``` Arch系
[shen@FUCKU ~]$ sudo pacman -Sy # 升级软件源
[shen@FUCKU ~]$ sudo pacman -S openjdk-11 # 安装 Java
shen@FUCKU:~$ java -jar meowmeow_server.jar # 启动服务端
```
红帽系:
```Red Hat系
shen@FUCKU:~$ sudo dnf install -y java-11-openjdk java-11-openjdk-devel # 安装 Java
shen@FUCKU:~$ java -jar meowmeow_server.jar # 启动服务端
```

## 如果上述方式无法下载:
打开游览器，访问: 
## https://www.oracle.com/cn/java/technologies/javase/javase8-archive-downloads.html

## 许可证

本项目使用 GNU GPLv3 许可证 喵~

本项目包含以下依赖：

- SnakeYAML(Apache 2.0)
- Log4j (Apache 2.0)
- Minecraft 服务器 (源代码) 1.8.8 MCP (Decompile only)

详细信息请查看 NOTICE 文件。

## 作者 喵呜~

Minecraft_Sam111（MeowMeow 项目）喵呜~

## 致谢 喵呜~
- SnakeYAML 作者
- Log4j 作者
- 所有贡献者
