# myFirstWebSite
一个简单的可公网访问的视频网站的前后端联调部署过程
# 说明：
本仓库所涉及的后端项目文件是前述bilibiliProject项目的改造阉割版，在bilibiliProject仓库中有该后端项目的完整版源码,完整版后端项目的源码涵盖了一个基本视频网站
所具备的几乎所有的基础功能，包括但不限于互相关注，点赞投币收藏，发送弹幕，弹幕遮罩和网站视频瀑布流呈现等等。然而，受限于本人前端技术之拙劣，设计不出复杂精细
的前端UI界面，故本仓库把之前bilibiliProject这个项目取出做了阉割，并利用react框架实现了简单的ui界面，最终实现了把一整套的前后端联调项目，通过Docker-compose.yml
一键部署的方式部署到了阿里云轻量应用服务器上(可公网访问)，此阉割版视频网站项目仅包含基础的视频上传，视频删除和视频下载观看三个功能，由于本人的阿里云服务器将于
6月中旬过期，且本人还并未考虑清楚是否要续费，加上在网络安全方面的考量，这里将不对外暴露本人服务器的公网ip，感兴趣的读者可以自行按照本指南对该网站进行一个简单的
部署联调，你可以将一整个项目部署到你本机的虚拟linux系统上，也可以自己弄一个公网服务器部署上去，如果您对本项目感兴趣，并且具有过硬的前端编码技能的话，可以前往我的
另一个bilibiliProject项目去下载完整版本的后端项目源码，配合这些接口以及您编写的精妙绝伦的前端页面代码，我相信您可以做出一个功能更丰富，更amazing的视频网站!

# Step1
git clone https://github.com/jamison11111/myFirstWebSite.git  #克隆本项目
# Step2
参考"搭建fastdfs.md"文件的指引在你自己的服务器上面安装好fastdfs,nginx以及二者的关联模块，按照文件指引自行测试fastdfs的文件上传功能是否正常。
# Step3
用VSCOODE打开本项目的frontEndProjectFile/client目录,自行调试界面ui代码，或直接在vscode命令行终端npm run build将整个前端项目打包到client/build目录下
# Step4
将Build目录拷贝到你的前端服务器的nginx的html资源目录下
# Step5
用Java工程常用IDE,本人推荐IDEA打开backEndProject/bilibiliProject目录,自动下载maven依赖，自行修改backEndProjectFile/bilibiliProject/bilibili-api/src/main/resources/application-test.properties配置文件，
指定你自己的mysql,redis,fastdfs文件服务器的ip和端口。用IDEA带有的Maven声明周期管理工具中的"package"选项将后端项目一键打包，你将在bilibili-api/target目录下找到我们打包后的目标jar包bilibili-api-1.0-SNAPSHOT.jar
# Step6
将目标jar包连同backEndProject下的docker-compose.yml和Dockerfile文件一起拷贝到你自己的服务器目录下，形成以下形式的目录结构
![image](https://github.com/user-attachments/assets/8ddad0bf-0ca2-4e2f-ab20-8f779dffe098)
如有个性化的部署需求，可自行修改docker-compose.yml和Dockerfile这两个文件的内容，实现个性化的部署。
注:在部署开始前，请自行配置好国内的docker镜像源或为你的服务器配置好能够访问外网docker仓库的网络代理。
# Step7
linux终端执行docker build -t bilibili_main_docker:1.1 .构建后端项目镜像
构建完成后,终端执行docker compose up指令一键部署redis,mysql和后端项目容器
# Step8
确保你的nginx的后端资源目录以及nginx.conf与端口转发相关的配置逻辑正确，确保fastdfs能正常上传下载文件，确保你的nginx服务已经正确开启。
# Step9
上述步骤全部完成且自检无误后，一个简单的在线视频网站就搭建完成了，快去试试吧!

![image](https://github.com/user-attachments/assets/da0341c1-cda6-4363-abea-b4dacc5fb26d)




