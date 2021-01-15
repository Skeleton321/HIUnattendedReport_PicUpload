## HIUnattendedReport_PicUpload
哈尔滨工程大学平安行动自动化打卡-截图上传组件

代码写得烂，望dalao轻喷。

主项目地址: [HRBEU_HIUnattendedReport](https://github.com/Skeleton321/HRBEU_HIUnattendedReport)

本项目主要为主项目提供图片上传功能。

本项目基于[`Mirai`](https://github.com/mamoe/mirai/)进行二次开发，HTTP API部分使用[`Netty`](https://github.com/netty/netty)实现。

### 使用方法

将本项目clone至本地:  
`git clone https://github.com/Skeleton321/HIUnattendedReport_PicUpload.git`

进入当前目录:  
`cd HIUnattendedReport_PicUpload`

编译:  
`gradlew.bat shadowJar`

编译后的jar文件存放在`build/libs`中。

**或者下载已编译好的jar包。**

在命令行中执行指令  
`java -jar UnattendedReport_PicUpload*.jar`  
或  
`java -jar <你的jar包名>.jar`  
运行该程序。  
*若是下载的已编译好的jar包，也可以双击运行 `run.bat`*

在`cmd`或`Powershell`下可能会出现乱码，属于正常现象。

运行后要求用户输入QQ号和密码。由于Mirai启动需要一定时间，请耐心等待，直到显示 "启动完成"，说明现在已经可以正常使用了。此时运行主项目才能正常上传图片。

启动期间可能会要求输入验证码或扫码验证，请根据提示进行操作。

如果想退出，在命令行中输入`stop`或使用快捷键Ctrl-C或直接关闭窗口均可。

### 注意事项

1. 由于狗一样的腾讯风控系统，建议正式使用前测试一下是否能正常发送图片。
2. 群号在主程序侧输入。
3. 本程序使用的AndroidPad协议，因此在手机或电脑上登录时不挤号。
4. 建议起床后关闭，睡觉前打开，防止意外掉线或被狗一样的腾讯风控系统盯上。