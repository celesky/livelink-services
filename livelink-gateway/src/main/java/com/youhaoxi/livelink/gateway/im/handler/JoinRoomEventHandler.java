package com.youhaoxi.livelink.gateway.im.handler;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.common.util.ClientPushUtil;
import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.JoinRoomEvent;
import com.youhaoxi.livelink.gateway.cache.ChatRoomRedisManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * 加入聊天室事件处理
 */
public class JoinRoomEventHandler  extends IMEventHandler {

    public JoinRoomEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(Worker woker) {
        JoinRoomEvent event = (JoinRoomEvent)msg;
        //检查房间是否存在
        long count = RoomUserRelationSetCache.getRoomMembersCount(event.getRoomId());
        //房间存在
        if(count>0){
            //用户是否已经在聊天室 如果已经在聊天室 直接返回
            boolean exist =  RoomUserRelationSetCache.isUserExistInRoom(event.getUserId(),event.getRoomId());
            if(exist){
                ResultMsg result = new ResultMsg(202,event.from.name+"已经在聊天室中了");
                ClientPushUtil.writeToClient(ctx,result,event.from.userId);
                return ;
            }

            ChatRoomRedisManager.addUserToRoom(event.getUserId(),event.getRoomId());
            //给房间每个人发送一条通知
            //发送人的用户信息
            //User user = UserInfoHashCache.getUserInfoFromRedis(event.getUserId());
//            String name = "";
//            if(user==null){
//                name = event.user.name;
//            }else{
//                name = user.name;
//            }
            String name = event.from.name;
            ResultMsg result = new ResultMsg(40,name+"加入了聊天室");
            woker.dispatcher.groupDispatch(result,event.getRoomId());
        }else{
            //房间不存在 回复一条错误提示
            ResultMsg result = new ResultMsg(201,"来晚了,请求加入的房间不存在");
            ClientPushUtil.writeToClient(ctx,result,event.from.userId);
        }

    }


    public static void main(String[] args) {
        ResultMsg result = new ResultMsg(201,"来晚了,请求加入的房间不存在");
        String s = JSON.toJSONString(result);
        System.out.println("s = " + s);
    }
}
