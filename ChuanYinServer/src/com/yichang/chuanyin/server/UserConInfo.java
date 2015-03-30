package com.yichang.chuanyin.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 记录用户连接端口等信息。
 * @author Administrator
 *
 */
public class UserConInfo {
    private int Id;
    private Socket so;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /*
     * 构造函数
     */
    public UserConInfo(int Id,Socket so){       
        try {
            this.Id=Id;
            this.so=so;
            out=new ObjectOutputStream(so.getOutputStream());
            in = new ObjectInputStream(new BufferedInputStream(so.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(UserConInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the so
     */
    public Socket getSo() {
        return so;
    }

    /**
     * @param so the so to set
     */
    public void setSo(Socket so) {
        this.so = so;
    }

    /**
     * @return the in
     */
    public ObjectInputStream getIn() {
        return in;
    }

    /**
     * @param in the in to set
     */
    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    /**
     * @return the out
     */
    public ObjectOutputStream getOut() {
        return out;
    }

    /**
     * @param out the out to set
     */
    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    /**
     * @return the Id
     */
    public int getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(int Id) {
        this.Id = Id;
    }
}
