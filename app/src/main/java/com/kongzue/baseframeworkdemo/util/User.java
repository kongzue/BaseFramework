package com.kongzue.baseframeworkdemo.util;

import java.io.Serializable;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/8/18 13:27
 */
public class User implements Serializable {
    
    private String userName;
    private int age;
    private String address;
    
    public User(String userName, int age, String address) {
        this.userName = userName;
        this.age = age;
        this.address = address;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }
    
    public int getAge() {
        return age;
    }
    
    public User setAge(int age) {
        this.age = age;
        return this;
    }
    
    public String getAddress() {
        return address;
    }
    
    public User setAddress(String address) {
        this.address = address;
        return this;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
