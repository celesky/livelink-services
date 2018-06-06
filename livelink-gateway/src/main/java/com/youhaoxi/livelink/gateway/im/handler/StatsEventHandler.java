package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.cache.UserInfoHashCache;
import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.common.util.ClientPushUtil;
import com.youhaoxi.livelink.gateway.common.util.StringUtils;
import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.StatsEvent;
import com.youhaoxi.livelink.gateway.im.msg.EndpointMsg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Set;

public class StatsEventHandler extends IMEventHandler{

    public StatsEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(IWorker worker) {
        StatsEvent msgEvent = (StatsEvent)msg;
        HashMap map = new HashMap();
        Integer action = msgEvent.getAction();
        EndpointMsg resultMsg = new EndpointMsg(0,"成功");
        //查询这个用户的相关数据
        if(action!=null&&action.intValue()==1){
            Integer userId = msgEvent.getqUserId();
            if(userId==null||userId.intValue()==0){
                resultMsg.setCode(110).setMsg("userId为空");
                ClientPushUtil.writeToClient(ctx,resultMsg,msgEvent.from.userId);
                return ;
            }
            //连接状态
            ChannelHandlerContext qUserCtx = ConnectionManager.getCtxMap().get(userId);
            if(qUserCtx==null){
                map.put("connected","未连接或者连接不在这台主机");
            }else{
                map.put("isAlive",qUserCtx.channel().isActive());
                map.put("isOpen",qUserCtx.channel().isOpen());
            }
            map.put("ConnectedThisHost",ConnectionManager.getCtxMap().size());
            //是否在聊天室
            String roomId = UserRelationHashCache.getUserIdRoomIdRelation(userId);
            String linkHost = UserRelationHashCache.getUserIdHostRelation(userId);
            map.put("roomId",roomId==null?"":roomId);
            map.put("linkHost",linkHost==null?"":linkHost);

            queryRoomMembers(msgEvent, map, roomId);


            //redis中用户信息
            User user = UserInfoHashCache.getUserInfoFromRedis(userId);
            if(user!=null){
                map.put("user",user);
            }
            resultMsg.setStatsMap(map);
            ClientPushUtil.writeToClient(ctx,resultMsg,msgEvent.from.userId);
            return;
        }else  if(action!=null&&action.intValue()==2){
            //查询房间的相关信息
            String roomId = msgEvent.getqRoomId();
            queryRoomMembers(msgEvent, map, roomId);

            map.put("ConnectedThisHost",ConnectionManager.getCtxMap().size());
            resultMsg.setStatsMap(map);
            ClientPushUtil.writeToClient(ctx,resultMsg,msgEvent.from.userId);
            return;
        }else{
            map.put("ConnectedThisHost",ConnectionManager.getCtxMap().size());
            ClientPushUtil.writeToClient(ctx,resultMsg,msgEvent.from.userId);
            return;
        }

    }

    private void queryRoomMembers(StatsEvent msgEvent, HashMap map, String roomId) {
        //如果房间不为空 按索引取
        if(StringUtils.isNotEmpty(roomId)){
            int start = 0;
            int stop =20;
            if(msgEvent.getRoomMemnberStart()!=null&&msgEvent.getRoomMemnberStart().intValue()>0){
                start = msgEvent.getRoomMemnberStart().intValue();
            }

            if(msgEvent.getRoomMemnberEnd()!=null&&msgEvent.getRoomMemnberEnd().intValue()>0){
                stop = msgEvent.getRoomMemnberEnd().intValue();
            }

            if(stop-start>20){
                stop = start+20;
            }
            Set<String> roomUserIds = RoomUserRelationSetCache.getRoomMemnbersLimit(roomId,start,stop);
            map.put("roomUserIds",roomUserIds);


            long count = RoomUserRelationSetCache.getRoomMembersCount(roomId);
            map.put("totalRoomMembers",count);
        }
    }
}
