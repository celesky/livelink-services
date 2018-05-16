package com.youhaoxi.livelink.gateway.im.msg;

public class User{
    public Integer userId;
    public String name;
    public String img;

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

    public String getImg() {
        return img;
    }

    public User setImg(String img) {
        this.img = img;
        return this;
    }
}
