package com.youhaoxi.livelink.gateway.cache;

import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.util.RedisUtil;

public class UserRelationHashCache {

    /*
     userId:LINKHOST-->hostString
     userId:ROOMID-->roomIdString
     */
    private static final String USERID_RELATION_HASH_KEY = "USERID_RELATION_HASH#%s";


    /**
     * redis中用户关系数据结构
     */
    static class UserRelationField{
        static String LINKHOST="LINKHOST";
        static String ROOMID="ROOMID";
    }


    /**
     * 给userId添加roomId映射
     * @param userId
     * @param roomId
     */
    protected static void setUserIdRoomIdRelation(Integer userId,String roomId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().hset(key, UserRelationField.ROOMID,roomId);
    }


    /**
     * 配置 userId-->host 映射
     * @param userId
     */
    public static void setUserIdHostRelation(Integer userId,String host){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().hset(key,UserRelationField.LINKHOST,host);
    }

    /**
     * 获取 userId-->host 映射
     * @param userId
     */
    public static String getUserIdHostRelation(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        String host = RedisUtil.cache().hget(key,UserRelationField.LINKHOST);
        return host;
    }

    /**
     * 配置 userId-->host 映射
     * @param userId
     */
    public static String getUserIdRoomIdRelation(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        String roomId = RedisUtil.cache().hget(key,UserRelationField.ROOMID);
        return roomId;
    }


    /**
     * 配置 userId-->host 映射
     * @param userId
     */
    public static void removeUserIdHostRelation(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        //String host = getUserIdHostRelation( userId);
        //if(Constants.LOCALHOST.equals(host)){
            RedisUtil.cache().hdel(key,UserRelationField.LINKHOST);
        //}
    }

    public static void removeUserRelationHash(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().del(key);
    }

    /**
     * 给userId删除roomId映射
     * @param userId
     */
    protected static void removeUserIdRoomIdRelation(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        //RedisUtil.cache().hset(key,UserRelationField.ROOMID,roomId);
        RedisUtil.cache().hdel(key,UserRelationField.ROOMID);
    }

}
