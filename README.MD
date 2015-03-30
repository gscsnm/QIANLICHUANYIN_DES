#基于DES的安卓系统安全通信系统的设计与实现


本项目在Android系统上实现了**DES加密**通话功能，使用TCP传送数据。由于是为了学习DES加密，所以做的比较简单，语音通话的降噪和回音消除都没有实现。项目中的两个文件夹分别是服务器项目和Android应用项目。两者都是Eclipse项目，基于jdk1.6。Android项目sdk版本是Android4.2。使用时需要修改服务器项目及android项目下的**Const.java**文件中的ServerIP为你服务器的ip，才能运行。

-------------------

[TOC]

## 项目简介

>  本项目在Android系统上实现了**DES加密**通话功能，使用TCP传送数据。由于是为了学习DES加密，所以做的比较简单，语音通话的降噪和回音消除都没有实现。

## 源码结构
ChuanYinServer/src                                    ---------------服务器  

|- com/yichang/chuanyin/server                 服务器包

-------------------
Qianlichuanyin/src                                     -----------------------客户端  
|- com/yichang/qianlichuanyin/main         -客户端主类包  

|- com/yichang/qianlichuanyin/net            ----客户端网络包  

|- com/yichang/qianlichuanyin/view			---客户端界面包  


