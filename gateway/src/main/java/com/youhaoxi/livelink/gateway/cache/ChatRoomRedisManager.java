package com.youhaoxi.livelink.gateway.cache;

import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.NetUtils;
import com.youhaoxi.livelink.gateway.common.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomRedisManager.class);

    private static final int CACHE_EXPIRE_HOUR_6 = 6*60*60;
    /**
     * 将用户添加到聊天室
     * @param userId
     * @param roomId
     */
    public static void addUserToRoom(Integer userId,String roomId){
        RoomUserRelationSetCache.setRoomIdMembers(roomId,userId);
        UserRelationHashCache.setUserIdRoomIdRelation(userId,roomId);
    }

    public static void removeUserFromRoom(Integer userId,String roomId){
        //群成员删除
        RoomUserRelationSetCache.removeRoomIdMembers(roomId,userId);
        //用户id -->群id 关系删除
        UserRelationHashCache.removeUserIdRoomIdRelation(userId);
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
     * 当关闭服务时调用
     * 清理掉userId在redis中的数据
     */
    public static void clearUserIdRedisRelation(){
        logger.info("clearUserIdRedisRelation HOST:"+ Constants.LOCALHOST);
//        Map<Integer,ChannelHandlerContext> map = ConnectionManager.getCtxMap();
//        if(map!=null){
//            map.keySet().stream().forEach(e->{
//                removeUserIdHostRelation(e);//删除用户id 和host关联关系  redis
//            });
//        }
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
