package com.youhaoxi.livelink.gateway.im.msg;

public class User{
    public Integer userId;
    public String name;
    public String headImg;

    public Integer getUserId() {
        return userId;
    }

    public User setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getHeadImg() {
        return headImg;
    }

    public User setHeadImg(String headImg) {
        this.headImg = headImg;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", headImg='" + headImg + '\'' +
                '}';
    }
}
