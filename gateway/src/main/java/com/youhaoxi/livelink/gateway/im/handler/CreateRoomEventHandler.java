package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.common.ClientPushUtil;
import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.im.event.CreateRoomEvent;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.cache.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import io.netty.channel.ChannelHandlerContext;

public class CreateRoomEventHandler  extends IMEventHandler {

    public CreateRoomEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }


    @Override
    public void execute(Worker worker) {
        CreateRoomEvent event = (CreateRoomEvent)msg;

        //创建1个房间
        String roomId = RoomUserRelationSetCache.getNewRoomId();
        ChatRoomRedisManager.addUserToRoom(event.getUserId(),roomId);

        ResultMsg result = new ResultMsg(102,"房间创建成功");
        result.setRoomId(roomId);
        ClientPushUtil.writeToClient(ctx,result,event.from.userId);
    }
}
