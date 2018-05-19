package com.youhaoxi.livelink.gateway.server.util;

import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;

import java.util.HashMap;

/**
 * 考虑在用户重新连接的时候 发起一个通知 给每个dispatcher服务,更新数据
 */
public class RouteHostManager {

    //将用户的host信息在本地保存一份,避免每次都要请求redis
    //userId-->host
    private static HashMap<Integer,String> routeHostMap = new HashMap<>();


    public static boolean contains(Integer userId){
        return routeHostMap.containsKey(userId);
    }

    /**
     * 获取用户连接的目标host
     * @param userId
     * @return
     */
    public static String getDestHost(Integer userId){
        if(routeHostMap.containsKey(userId)){
            return routeHostMap.get(userId);
        }else{
            //从redis获取
            String host = UserRelationHashCache.getUserIdHostRelation(userId);
            routeHostMap.put(userId,host);
            return host;
        }
    }

    public static void remove(Integer userId){
        if(routeHostMap.containsKey(userId)){
            routeHostMap.remove(userId);
        }
    }

    public static void setUserHost(Integer userId,String host){
        routeHostMap.put(userId,host);
    }

    public static void clearUserLocalData(Integer userId) {
        remove( userId);
    }
}
