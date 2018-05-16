package com.youhaoxi.livelink.gateway.cache;

import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.RedisUtil;

import java.time.Instant;
import java.util.Set;

public class RoomUserRelationSetCache {
    //自增
    private static final String ROOMID_GENERATOR_KEY = "roomId_generator_key";
    //roomId->userId:host  zset排序存储  按加入房间的顺序
    private static final String ROOMID_USERID_RALATION_ZSET_KEY="ROOMID_USERID_RALATION_ZSET#%s";

    public static void delRoom(String roomId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        RedisUtil.cache().del(key);
    }

    /**
     * 给roomId聊天室添加成员userId  zset 按时间戳排序
     * @param roomId
     * @param userId
     */
    protected static void setRoomIdMembers(String roomId,Integer userId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        double timestamp = Instant.now().toEpochMilli();
        RedisUtil.cache().zadd(key,timestamp,userId+"");
    }

    /**
     * 给roomId聊天室移除成员userId  zset 按时间戳排序
     * @param roomId
     * @param userId
     */
    protected static void removeRoomIdMembers(String roomId,Integer userId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        //double timestamp = Instant.now().toEpochMilli();
        //RedisUtil.cache().zadd(key,timestamp,userId.toString());
        RedisUtil.cache().zrem(key,String.valueOf(userId));
    }

    /**
     * 检查房间是否存在
     * @param roomId
     * @return
     */
    public static long getRoomMembersCount(String roomId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        long count = RedisUtil.cache().zcard(key);
        return count;
    }




    public static Set<String> getAllRoomMemnbers(String roomId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        Set<String> set =  RedisUtil.cache().zrange(key,0,-1);
        return set;
    }
    public static Set<String> getRoomMemnbersLimit(String roomId,int start,int stop){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        Set<String> set =  RedisUtil.cache().zrange(key,start,stop);
        return set;
    }
    public static long getRoomMemnbersCount(String roomId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        long count =  RedisUtil.cache().zcard(key);
        return count;
    }


    /**
     * 获取一个新的roomId
     */
    public static boolean isUserExistInRoom(Integer userId,String roomId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        long count =  RedisUtil.cache().zrank(key,String.valueOf(userId));
        return count>=0;
    }

    /**
     * 获取一个新的roomId
     */
    public static String getNewRoomId(){
        long id =  RedisUtil.cache().incrBy(ROOMID_GENERATOR_KEY,1);
        return "room"+id;
    }


}
