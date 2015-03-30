package com.yichang.chuanyin.server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Administrator
 */
public class Const {
	// 服务器的IP
	final public static String ServerIP = "192.168.199.201";
//	final public static String ServerIP = "112.124.37.249";
	
	// 服务器的监听端口
	final public static int ServerPort = 21000;
	// 录音的频率，一般电话通话有8000足以
	final public static int RateInHz = 8000;

	// 播放频率
	final public static int TrackHz = 8000;
	// 缓冲几个声音包就播放，
	final public static int soundPakNum = 5;

	// 录音缓冲区大小，暂时设定为3k
	final public static int RecordBufferSize = 2 * 1024;
	// 录音缓冲区大小，暂时设定为3k
	final public static int TrackBufferSize = 2 * 1024;

	// 录音时多久取一次数据，单位是毫秒
	final public static int RecordTime = 70;
	// 播放过程中停顿多久，单位是毫秒
	final public static int TrackTime = 60;

	// 消息类型，分别为代表登陆及登陆，文字消息，退出或注销的类型常量
	final public static int Login = 1;
	final public static int Logout = 2;
	final public static int Login_Fail = -1;
	final public static int textMessage = 10;
	final public static int soundMessage = 11;

	// 消息类型，分别为增加用户和删除用户，及服务器把UDP信息列表发给客户端,以及用户信息列表（用户名、id等）
	// 在发列表时会把服务器定给客户端的用户名也发回去
	final public static int addUser = 12;
	final public static int removeUser = 13;
	final public static int userUDPList = 14;
	final public static int userInfoList = 15;
	final public static int userInfo = 16;

	// 消息类型，发送文件前的准备
	final public static int sendfile = 25;
	// 消息类型，发送文件的回复，拒绝，字符串为文件名
	final public static int notSend = 26;
	// 消息类型，发送文件的回复，接受，字符串为文件名
	final public static int send = 27;
	// 消息类型，代表发送文件的一部分
	final public static int filepath = 29;

	// 消息类型，请求对话
	final public static int requestTalk = 30;
	// 消息类型，接到请求后拒绝对话
	final public static int refuseTalk = 31;
	// 消息类型，接到请求后答应对话
	final public static int promiseTalk = 32;
	// 停止谈话
	final public static int stopTalk = 33;

	// 指示使用TCP发消息还是用UDP
	final public static String useTCP = "TCP";
	final public static String useUDP = "UDP";

	// 子线程发送给主线程的消息类型，分别是人员增加，人员减少,密码校验失败登陆失败，请求对话，接受对话，拒绝对话
	final public static int handlerAddUser = 41;
	final public static int handlerReduceUser = 42;
	final public static int handlerTrack = 43;
	final public static int handlerLogin_Fail = 44;
	final public static int handlerLogin_success = 45;
	final public static int handlerRequstTall = 46;	
	final public static int handlerPromiseTall = 47;
	final public static int handlerRefuseTall = 48;
	final public static int handlerStopTall = 49;

	
	//广播事件、程序退出事件
	final public static String  APPLICATIONEXIT= "APPLICATIONEXIT";

}
