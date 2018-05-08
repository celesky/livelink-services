package com.youhaoxi.livelink.gateway.util;

import com.youhaoxi.livelink.gateway.common.NetUtils;
import com.youhaoxi.livelink.gateway.common.RedisUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户的连接情况 redis数据结构: HASH
 * USERID_RELATION_HASH#111
 *      LINKHOST:172.168.0.1
 *      ROOMID:room101
 *
 * 聊天室成员 redis数据结构 ZSET
 *  ROOMID_USERID_RALATION_HASH#room101
 *      userId1
 *      userId2...
 *
 *
 * 聊天室管理
 */
public class ChatRoomRedisManager {
    private static final int CACHE_EXPIRE_HOUR_6 = 6*60*60;
    /*
     userId:LINKHOST-->hostString
     userId:ROOMID-->roomIdString
     */
    private static final String USERID_RELATION_HASH_KEY = "USERID_RELATION_HASH#%s";
    //userId-->roomId  string存储
    //private static final String USERID_ROOM_RALATION_KEY = "USERID_ROOMID_RALATION#%s";

    //roomId->userId  zset排序存储  按加入房间的顺序
    private static final String ROOMID_USERID_RALATION_ZSET_KEY="ROOMID_USERID_RALATION_HASH#%s";

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
     */
    private static void removeUserIdRoomIdRelation(Integer userId){
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
        //群成员删除
        removeRoomIdMembers(roomId,userId);
        //用户id -->群id 关系删除
        removeUserIdRoomIdRelation(userId);
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
        RedisUtil.cache().hset(key,UserRelationField.LINKHOST,HOST);
    }

    /**
     * 配置 userId-->host 映射
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
    public static void removeUserIdHostRelation(Integer userId){
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().hdel(key,UserRelationField.LINKHOST);
    }

    /**
     * redis中用户关系数据结构
     */
    static class UserRelationField{
        static String LINKHOST="LINKHOST";
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
