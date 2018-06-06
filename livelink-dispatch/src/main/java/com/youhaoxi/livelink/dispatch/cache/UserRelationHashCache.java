package com.youhaoxi.livelink.dispatch.cache;

import com.youhaoxi.livelink.dispatch.common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

public class UserRelationHashCache {
    //userId-->roomId
    private static ConcurrentHashMap<Integer,String> URRalationMap = new ConcurrentHashMap();
    //userId-->host
    private static ConcurrentHashMap<Integer,String> userIdHostMap = new ConcurrentHashMap();

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
    //本地缓存操作
    public static void setUserIdRoomIdRelationLocal(Integer userId,String roomId){
        URRalationMap.put(userId,roomId);
    }
    public static void removeUserIdRoomIdRelationLocal(Integer userId){
        URRalationMap.remove(userId);
    }

    //本地缓存操作
    public static void setUserIdHostRelationLocal(Integer userId,String host){
        userIdHostMap.put(userId,host);
    }

    public static void removeUserIdHostRelationLocal(Integer userId){
        userIdHostMap.remove(userId);
    }



    /**
     * 给userId添加roomId映射
     * @param userId
     * @param roomId
     */
    public static void setUserIdRoomIdRelation(Integer userId,String roomId){
        setUserIdRoomIdRelationLocal(userId,roomId);
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().hset(key, UserRelationField.ROOMID,roomId);
    }


    /**
     * 配置 userId-->host 映射
     * @param userId
     */
    public static void setUserIdHostRelation(Integer userId,String host){
        setUserIdHostRelationLocal(userId,host);
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().hset(key, UserRelationField.LINKHOST,host);
    }

    /**
     * 获取 userId-->host 映射
     * @param userId
     */
    public static String getUserIdHostRelation(Integer userId){
        if(userIdHostMap.containsKey(userId)){
            return userIdHostMap.get(userId);
        }else{
            //从redis获取
            String host = getUserIdRelationRedis(userId, UserRelationField.LINKHOST);
            if(StringUtils.isNotEmpty(host)){
                userIdHostMap.put(userId,host);
            }else{
                userIdHostMap.remove(userId);
            }
            return host;
        }
    }

    private static String getUserIdRelationRedis(Integer userId, String field) {
        String key = String.format(USERID_RELATION_HASH_KEY, userId);
        String host = RedisUtil.cache().hget(key, field);
        return host;
    }

    /**
     *  userId-->host 映射
     * @param userId
     */
    public static String getUserIdRoomIdRelation(Integer userId){

        if(URRalationMap.containsKey(userId)){
            return URRalationMap.get(userId);
        }else{
            //从redis获取
            String roomId = getUserIdRelationRedis(userId, UserRelationField.ROOMID);
            if(StringUtils.isNotEmpty(roomId)){
                URRalationMap.put(userId,roomId);
            }else{
                URRalationMap.remove(userId);
            }
            return roomId;
        }
    }

    /**
     * 从redis中获取数据刷新本地缓存
     * @param userId
     * @return
     */
//    public static String refreshUserIdRoomIdRelation(Integer userId){
//        String roomId = getUserIdRelationRedis(userId, UserRelationField.ROOMID);
//        if(StringUtils.isNotEmpty(roomId)){
//            URRalationMap.put(userId,roomId);
//        }else{
//            URRalationMap.remove(userId);
//        }
//        return roomId;
//    }
//
//    public static String refreshUserIdHostRelation(Integer userId){
//        String key = String.format(USERID_RELATION_HASH_KEY,userId);
//        String roomId = RedisUtil.cache().hget(key,UserRelationField.ROOMID);
//        if(StringUtils.isNotEmpty(roomId)){
//            URRalationMap.put(userId,roomId);
//        }else{
//            URRalationMap.remove(userId);
//        }
//        return roomId;
//    }

    /**
     * 配置 userId-->host 映射
     * @param userId
     */
    public static void removeUserIdHostRelation(Integer userId){
        removeUserIdHostRelationLocal(userId);
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        //String host = getUserIdHostRelation( userId);
        //if(Constants.LOCALHOST.equals(host)){
            RedisUtil.cache().hdel(key, UserRelationField.LINKHOST);
        //}
    }

    public static void removeUserRelationHash(Integer userId){
        removeUserIdHostRelationLocal(userId);
        removeUserIdRoomIdRelationLocal(userId);
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        RedisUtil.cache().del(key);
    }

    /**
     * 给userId删除roomId映射
     * @param userId
     */
    protected static void removeUserIdRoomIdRelation(Integer userId){
        removeUserIdRoomIdRelationLocal(userId);
        String key = String.format(USERID_RELATION_HASH_KEY,userId);
        //RedisUtil.cache().hset(key,UserRelationField.ROOMID,roomId);
        RedisUtil.cache().hdel(key, UserRelationField.ROOMID);
    }

}
