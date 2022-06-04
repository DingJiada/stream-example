项目依赖结构：
supervisor-web-center (录播督导业务后台)
supervisor-web-media-task (媒体资源处理平台，转码、切片等)
MediaServices-http-hooks (流媒体服务器钩子处理平台，鉴权、DVR等)
MediaServices (流媒体服务器)
NginxServices (静态资源服务器，m3u8点播、头像图片等)

推流方式：

推流类型：sw 软推、rpm 录播机推

云监考业务依赖每个学生存在客户端，客户端登陆后需做以下操作：
推流、建立websocket连接、发送开始录制请求(如果需要)、更改考生表的登录时间、学生状态
退出后需做以下操作：
断流、断开websocket连接、发送停止录制请求(如果需要)、更改考生表的退出时间