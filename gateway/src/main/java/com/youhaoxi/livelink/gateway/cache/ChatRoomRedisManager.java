package com.youhaoxi.livelink.gateway.cache;

import com.youhaoxi.livelink.gateway.common.util.NetUtils;
import com.youhaoxi.livelink.gateway.common.util.RedisLock;
import com.youhaoxi.livelink.gateway.common.util.RedisUtil;
import com.youhaoxi.livelink.gateway.common.util.StringUtils;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomRedisManager.class);

    private static final int CACHE_EXPIRE_HOUR_6 = 6*60*60;
    /**
     * 将用户添加到聊天室
     * @param userId
     * @param roomId
     */
    public static void addUserToRoom(Integer userId,String roomId){
        RedisLock lock = new RedisLock(RedisLock.USER_REDIS_LOCK_KEY,String.valueOf(userId));
        try {
            lock.multitryLock(2000);
            RoomUserRelationSetCache.setRoomIdMembers(roomId, userId);
            UserRelationHashCache.setUserIdRoomIdRelation(userId, roomId);
        }finally {
            lock.unlock();
        }
    }

    public static void removeUserFromRoom(Integer userId,String roomId){
        RedisLock lock = new RedisLock(RedisLock.USER_REDIS_LOCK_KEY,String.valueOf(userId));
        try {
            lock.multitryLock(2000);
            //群成员删除
            RoomUserRelationSetCache.removeRoomIdMembers(roomId, userId);
            //用户id -->群id 关系删除
            UserRelationHashCache.removeUserIdRoomIdRelation(userId);
        }finally {
            lock.unlock();
        }
    }

//    /**
//     * 将用户添加到聊天室
//     * @param userId
//     * @param roomId
//     */
//    public static void removeUserToRoom(Integer userId,String roomId){
//        setRoomIdMembers(roomId,userId);
//        setUserIdRoomIdRelation(userId,roomId);
//    }



    /**
     * 当用户退出或者断开连接时
     * 清理掉userId在redis中的数据
     */
    public static void clearUserIdRedisData(Integer userId){
        RedisLock lock = new RedisLock(RedisLock.USER_REDIS_LOCK_KEY,String.valueOf(userId));
        try{
            lock.multitryLock(2000);
            logger.info("clearUserIdRedisData userId:"+ userId);
            //查询是否在聊天室
            String roomId = UserRelationHashCache.getUserIdRoomIdRelation(userId);
            if(StringUtils.isNotEmpty(roomId)){
                //从聊天室中删除这个用户id
                RoomUserRelationSetCache.removeRoomIdMembers(roomId,userId);
            }
            //删除用户连接相关信息
            UserRelationHashCache.removeUserRelationHash(userId);
            UserInfoHashCache.removeUserInfoHash(userId);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 服务关闭的时候,清理redis的数据
     */
    public static void clearAllRelationThisHost(){
        Map<Integer, ChannelHandlerContext> map = ConnectionManager.getCtxMap();
        logger.info("clearAllRelationThisHost: 有"+map.size()+" 个用户连接数据需要清理");
        if(map!=null){
            map.keySet().parallelStream().forEach(userId->{
                clearUserIdRedisData( userId);
            });
        }
    }









    public static void main(String[] args) {
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        System.out.println("timeStr = " + timeStr);
        double time = Double.parseDouble(timeStr);
        System.out.println("time = " + time);

        //URL url = URL.valueOf("dubbo://" + NetUtils.getLocalAddress().getHostAddress() + ":2233");
        System.out.println("NetUtils.getLocalAddress().getHostAddress()  = " + NetUtils.getLocalAddress().getHostAddress() );


        //for(int i=10000;i<20000;i++){
           // setRoomIdMembers("room1001",10);
        //}

        RedisUtil.cache().set("test","test");
    }

}
