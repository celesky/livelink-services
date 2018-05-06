package com.youhaoxi.livelink.gateway.util;

import com.youhaoxi.livelink.gateway.common.NetUtils;
import com.youhaoxi.livelink.gateway.common.RedisUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 聊天室管理
 */
public class ChatRoomRedisManager {
    private static final int CACHE_EXPIRE_HOUR_6 = 6*60*60;
    /*
     userId:HOST-->hostString
     userId:ROOMID-->roomIdString
     */
    private static final String USERID_RELATION_HASH_KEY = "USERID_RELATION_HASH#%s";
    //userId-->roomId  string存储
    //private static final String USERID_ROOM_RALATION_KEY = "USERID_ROOMID_RALATION#%s";

    //roomId->userId  zset排序存储  按加入房间的顺序
    private static final String ROOMID_USERID_RALATION_ZSET_KEY="ROOMID_USERID_RALATION_HASH#%s";

    //userId->host 地址 string 存储
    //private static final String USERID_HOST_RELATION_KEY ="USERID_HOST_RELATION#%s";

    private static final String HOST = NetUtils.getLocalAddress().getHostAddress();
    /**
     * 给userId添加roomId映射
     * @param userId
     * @param roomId
     */
    private static void setUserIdRoomIdRelation(Integer userId,String roomId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().hset(key,UserRelationField.ROOMID,roomId);
    }


    /**
     * 给roomId聊天室添加成员userId  zset 按时间戳排序
     * @param roomId
     * @param userId
     */
    private static void setRoomIdMembers(String roomId,Integer userId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        double timestamp = Instant.now().toEpochMilli();
        RedisUtil.cache().zadd(key,timestamp,userId.toString());
    }

    /**
     * 给roomId聊天室移除成员userId  zset 按时间戳排序
     * @param roomId
     * @param userId
     */
    private static void removeRoomIdMembers(String roomId,Integer userId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        double timestamp = Instant.now().toEpochMilli();
        //RedisUtil.cache().zadd(key,timestamp,userId.toString());
        RedisUtil.cache().zrem(key,userId.toString());
    }
    /**
     * 给userId删除roomId映射
     * @param userId
     * @param roomId
     */
    private static void removeUserIdRoomIdRelation(Integer userId,String roomId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        //RedisUtil.cache().hset(key,UserRelationField.ROOMID,roomId);
        RedisUtil.cache().hdel(key,UserRelationField.ROOMID);
    }

    /**
     * 将用户添加到聊天室
     * @param userId
     * @param roomId
     */
    public static void addUserToRoom(Integer userId,String roomId){
        setRoomIdMembers(roomId,userId);
        setUserIdRoomIdRelation(userId,roomId);
    }

    public static void removeUserFromRoom(Integer userId,String roomId){
        removeRoomIdMembers(roomId,userId);
        removeUserFromRoom(userId,roomId);
    }

    /**
     * 将用户添加到聊天室
     * @param userId
     * @param roomId
     */
    public static void removeUserToRoom(Integer userId,String roomId){
        setRoomIdMembers(roomId,userId);
        setUserIdRoomIdRelation(userId,roomId);
    }

    /**
     * 配置 userId-->host 映射
     * @param userId
     */
    public static void setUserIdHostRelation(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().hset(key,UserRelationField.HOST,HOST);
    }

    /**
     * 配置 userId-->host 映射
     * @param userId
     */
    public static String getUserIdHostRelation(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        String host = RedisUtil.cache().hget(key,UserRelationField.HOST);
        return host;
    }


    /**
     * 配置 userId-->host 映射
     * @param userId
     */
    public static void removeUserIdHostRelation(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().hdel(key,UserRelationField.HOST);
    }

    /**
     * redis中用户关系数据结构
     */
    static class UserRelationField{
        static String HOST="HOST";
        static String ROOMID="ROOMID";
    }


    public static void main(String[] args) {
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        System.out.println("timeStr = " + timeStr);
        double time = Double.parseDouble(timeStr);
        System.out.println("time = " + time);

        //URL url = URL.valueOf("dubbo://" + NetUtils.getLocalAddress().getHostAddress() + ":2233");
        System.out.println("NetUtils.getLocalAddress().getHostAddress()  = " + NetUtils.getLocalAddress().getHostAddress() );
    }

}
