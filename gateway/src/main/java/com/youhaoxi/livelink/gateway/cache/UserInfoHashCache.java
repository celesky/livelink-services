package com.youhaoxi.livelink.gateway.cache;

import com.youhaoxi.livelink.gateway.common.RedisUtil;
import com.youhaoxi.livelink.gateway.im.msg.User;

import java.util.Map;

public class UserInfoHashCache {
    /**
     * 用户基本信息 hash表保存
     *  userId:IMG-->hostString
     userId:NAME-->roomIdString
     */
    private static final String USER_INFO_HASH_KEY = "USER_INFO_HASH#%s";

    /**
     * 数据结构
     */
    static class UserInfoField{
        static String img="IMG";
        static String name="NAME";
    }

    public static void removeUserInfoHash(int userId){
        String key = String.format(USER_INFO_HASH_KEY,userId);
        RedisUtil.cache().del(key);
    }


    public static User getUserInfoFromRedis(int userId){
        String key = String.format(USER_INFO_HASH_KEY,userId);
        Map<String,String> map = RedisUtil.cache().hgetAll(key);
        if(map!=null&&map.size()>0){
            User user = new User();
            user.setImg(map.get(UserInfoField.img));
            user.setName(map.get(UserInfoField.name));
            return user;
        }
        return null;
    }

    public static void setUserInfoToRedis(int userId,User user){
        String key = String.format(USER_INFO_HASH_KEY,userId);
        RedisUtil.cache().hset(key, UserInfoField.img,user.img);
        RedisUtil.cache().hset(key, UserInfoField.name,user.name);
    }
}
