package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.util.ClientPushUtil;
import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.im.enums.InterMsgType;
import com.youhaoxi.livelink.gateway.im.event.CreateRoomEvent;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.cache.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.im.msg.EndpointMsg;
import com.youhaoxi.livelink.gateway.im.msg.InterMsg;
import io.netty.channel.ChannelHandlerContext;

public class CreateRoomEventHandler  extends IMEventHandler {

    public CreateRoomEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }


    @Override
    public void execute(IWorker worker) {
        CreateRoomEvent event = (CreateRoomEvent)msg;

        //创建1个房间
        String roomId = RoomUserRelationSetCache.getNewRoomId();
        ChatRoomRedisManager.addUserToRoom(event.getUserId(),roomId);


        //分发事件
        // 创建聊天室和加入聊天室作为同一种操作 消息给别的节点
        InterMsg interMsg = new InterMsg();
        interMsg.setHost(Constants.LOCALHOST)
                .setInterMsgType(InterMsgType.joinRoom)
                .setUserId(event.from.getUserId())
                .setRoomId(roomId);
        worker.getInterMsgDispatcher().dispatch(interMsg);

        EndpointMsg result = new EndpointMsg(102,"房间创建成功");
        result.setRoomId(roomId);
        ClientPushUtil.writeToClient(ctx,result,event.from.userId);
    }
}
