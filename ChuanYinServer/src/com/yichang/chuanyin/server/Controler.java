package com.yichang.chuanyin.server;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * 服务器控制类
 * 主要就是启动服务器
 */
public class Controler {
   public static void main(String[] args){
       try {
           Server server = new Server();	// 创建一个服务器类
           while (true) {
               server.connetUser();			//与客户端保存通讯
           }
       } catch (Exception ex) {				//异常处理
           Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
}
