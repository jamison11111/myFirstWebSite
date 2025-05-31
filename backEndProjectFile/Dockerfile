# 基础镜像使用java
FROM java:8

# 作者
MAINTAINER lwc

# VOLUME 指定临时文件目录为/tmp，在主机/var/lib/docker目录下创建了一个临时文件并链接到容器的/tmp,可以不指定宿主机目录
VOLUME /tmp

# 将jar包添加到容器中并更名为lwc_docker.jar
ADD bilibili-api-1.0-SNAPSHOT.jar Main.jar

# 创建jar包文件以供字节流输入
RUN bash -c 'touch /Main.jar'

#容器启动后执行的指令
ENTRYPOINT ["java","-jar","/Main.jar"]
