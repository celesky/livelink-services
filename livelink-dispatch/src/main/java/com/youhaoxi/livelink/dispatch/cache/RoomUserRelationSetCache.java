package com.youhaoxi.livelink.dispatch.cache;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.youhaoxi.livelink.dispatch.common.util.RedisUtil;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class RoomUserRelationSetCache {

    /**
     * 考虑到 读取操作 比 修改操作更频繁 选用arrayList
     *
     * roomId-->userIdList
     */
    private static ListMultimap<String,Integer> roomUserRalation = ArrayListMultimap.create();

    //自增
    private static final String ROOMID_GENERATOR_KEY = "roomId_generator_key";
    //roomId->userId:host  zset排序存储  按加入房间的顺序
    private static final String ROOMID_USERID_RALATION_ZSET_KEY="ROOMID_USERID_RALATION_ZSET#%s";


    public static void delRoomLocal(String roomId){
        roomUserRalation.removeAll(roomId);
    }

    public static void setRoomIdMembersLocal(String roomId,Integer userId){
        roomUserRalation.put(roomId,userId);
    }

    public static void removeRoomIdMembersLocal(String roomId,Integer userId){
        roomUserRalation.remove(roomId,userId);
    }

    public static List<Integer> getAllRoomMemnbersLocal(String roomId){
        List<Integer> userList = roomUserRalation.get(roomId);
        return userList;
    }

    public static void delRoom(String roomId){
        delRoomLocal(roomId);
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        RedisUtil.cache().del(key);
    }



    /**
     * 给roomId聊天室添加成员userId  zset 按时间戳排序
     * @param roomId
     * @param userId
     */
    protected static void setRoomIdMembers(String roomId,Integer userId){
        setRoomIdMembersLocal(roomId,userId);
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
        removeRoomIdMembersLocal(roomId,userId);
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        //double timestamp = Instant.now().toEpochMilli();
        //RedisUtil.cache().zadd(key,timestamp,userId.toString());
        RedisUtil.cache().zrem(key,String.valueOf(userId));
    }

    /**
     * 检查房间是否存在
     * 以redis端为准
     * @param roomId
     * @return
     */
    public static long getRoomMembersCount(String roomId){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        long count = RedisUtil.cache().zcard(key);
        return count;
    }


//    public static Set<String> getAllRoomMemnbers(String roomId){
//        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
//        Set<String> set =  RedisUtil.cache().zrange(key,0,-1);
//        return set;
//    }

    public static Set<String> getRoomMemnbersLimit(String roomId,int start,int stop){
        String key = String.format(ROOMID_USERID_RALATION_ZSET_KEY,roomId);
        Set<String> set =  RedisUtil.cache().zrange(key,start,stop);
        return set;
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
