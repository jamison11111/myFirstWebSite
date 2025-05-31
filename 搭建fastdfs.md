
# CenOS7.x安装FastDFS


### 一、先安装FastOS.repo yum源
执行以下命令：

    rpm -ivh http://www.fastken.com/yumrepo/el7/noarch/FastOSrepo-1.0.0-1.el7.centos.noarch.rpm

### 二、提前安装编译所需依赖
执行以下命令：

    yum install git gcc gcc-c++ make automake autoconf libtool pcre pcre-devel zlib zlib-devel openssl-devel wget vim -y


### 三、提前准备安装路径和数据存储目录
创建fastdfs数据存储目录，在根目录下执行以下命令

    mkdir /home/dfs #创建数据存储目录
    cd /usr/local/src #切换到安装目录准备下载安装包，如果没有/usr/local/src该目录，则使用mkdir命令提前创建


### 四、安装libfastcommon（FastDFS分离出的公用函数库）
先执行以下命令进入/usr/local/src目录下

    cd /usr/local/src

执行以下命令从git下载libfastcommon：

    git clone https://github.com/happyfish100/libfastcommon.git --depth 1

等待下载完成后，使用cd命令进入下载的文件夹：

    cd libfastcommon/

执行编译和安装操作（请确保系统已经使用root权限，如未使用root权限，则可以使用su命令切换到root）\
执行以下命令，进行编译和安装：

    ./make.sh && ./make.sh install #编译安装

完成后，返回上一级目录

    cd ../ #返回上一级目录

### 五、安装libserverframe（安装libserverframe）
执行以下命令从git下载libserverframe：

    git clone https://github.com/happyfish100/libserverframe.git --depth 1

等待下载完成后，使用cd命令进入下载的文件夹：

    cd libserverframe/

执行编译和安装操作（请确保系统已经使用root权限，如未使用root权限，则可以使用su命令切换到root）\
执行以下命令，进行编译和安装：

    ./make.sh && ./make.sh install #编译安装

完成后，返回上一级目录

    cd ../ #返回上一级目录

### 六、安装FastDFS（FastDFS本体）
执行以下命令从git下载FastDFS：

    git clone https://github.com/happyfish100/fastdfs.git --depth 1

等待下载完成后，使用cd命令进入下载的文件夹：

    cd fastdfs/

执行编译和安装操作（请确保系统已经使用root权限，如未使用root权限，则可以使用su命令切换到root）\
执行以下命令，进行编译和安装：

    ./make.sh && ./make.sh install #编译安装

准备配置文件，执行以下命令将配置文件复制到对应目录，供nginx使用

    cp /usr/local/src/fastdfs/conf/http.conf /etc/fdfs/ #供nginx访问使用
    cp /usr/local/src/fastdfs/conf/mime.types /etc/fdfs/ #供nginx访问使用
    cd ../ #返回上一级目录



### 七、配置FastDFS
执行以下命令，进入FastDFS的tracker配置文件：

    vim /etc/fdfs/tracker.conf

修改以下内容：

    port=22122  # tracker服务器端口（默认22122,一般不修改）
    base_path=/home/dfs  # 存储日志和数据的根目录
保存退出

执行以下命令，进入FastDFS的storage配置文件：

    vim /etc/fdfs/storage.conf

修改以下内容：

    port=23000  # storage服务端口（默认23000,一般不修改）
    base_path=/home/dfs  # 数据和日志文件存储根目录
    store_path0=/home/dfs  # 第一个存储目录
    tracker_server=192.168.52.1:22122  # tracker服务器IP和端口（注意，使用虚拟机和服务器的同学，需要把192.168.52.1:22122替换成：你本机的公网ip:22122，例如你的公网ip是10.248.12.23，则需要替换成10.248.12.23:22122）
    http.server_port=8888  # http访问文件的端口(默认8888,看情况修改,和nginx中保持一致)
保存退出

执行以下命令，进入FastDFS的client配置文件：

    vim /etc/fdfs/client.conf
#需要修改的内容如下

    base_path=/home/dfs
    tracker_server=192.168.52.1:22122    #tracker服务器IP和端口（注意，使用虚拟机和服务器的同学，需要把192.168.52.1:22122替换成：你本机的公网ip:22122，例如你的公网ip是10.248.12.23，则需要替换成10.248.12.23:22122）
保存退出

### 八、下载fastdfs-nginx-module
执行以下命令从git下载fastdfs-nginx-module：

    git clone https://github.com/happyfish100/fastdfs-nginx-module.git --depth 1

将下载好得fastdfs-nginx-module的配置文件复制到/etc/fdfs目录下，供后续nginx使用

    cp /usr/local/src/fastdfs-nginx-module/src/mod_fastdfs.conf /etc/fdfs


### 九、启动FastDFS
启动tracker，输入以下命令：

    systemctl start fdfs_trackerd #启动tracker服务

启动storage，输入以下命令：

    systemctl start fdfs_storaged

### 十、下载nginx
同样，还是在/usr/local/src目录下，执行以下命令下载nginx：
    
    wget http://nginx.org/download/nginx-1.15.4.tar.gz #下载nginx压缩包

### 十一、测试是否成功启动FastDFS
执行以下命令：
fdfs_upload_file /etc/fdfs/client.conf /usr/local/src/nginx-1.15.4.tar.gz


### 十二、安装nginx
在/usr/local/src目录下，执行以下命令解压刚下载的nginx压缩包：

    tar -zxvf nginx-1.15.4.tar.gz #解压

进入解压后的文件目录：

    cd nginx-1.15.4/

添加fastdfs-nginx-module模块：

    ./configure --add-module=/usr/local/src/fastdfs-nginx-module/src/ 

执行编译和安装：

    make && make install #编译安装

### 十三、配置nginx
执行以下命令，打开fastdfs-nginx-module配置文件：
    
    vim /etc/fdfs/mod_fastdfs.conf

修改以下内容：

    tracker_server=192.168.52.1:22122  #tracker服务器IP和端口，这里同之前修改tracker和storage的ip，替换成自己的公网ip
    url_have_group_name=true
    store_path0=/home/dfs

保存退出

执行以下命令，打开nginx主配置文件：
    
    vim /usr/local/nginx/conf/nginx.conf

添加如下配置：

    server {
        listen       8888;    ## 该端口为storage.conf中的http.server_port相同
        server_name  localhost;
        location ~/group[0-9]/ {
            ngx_fastdfs_module;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

### 十四、启动nginx
执行以下命令启动nginx：

    /usr/local/nginx/sbin/nginx #启动nginx


### 十五、测试下载
用外部浏览器访问刚才已传过的nginx安装包,引用返回的ID，例如：

    http://192.168.52.1:8888/group1/M00/00/00/wKgAQ1pysxmAaqhAAA76tz-dVgg.tar.gz

