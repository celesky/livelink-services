package com.youhaoxi.livelink.gateway.dispatch.mq.inter.downstream;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.util.StringUtils;
import com.youhaoxi.livelink.gateway.dispatch.mq.Processor;
import com.youhaoxi.livelink.gateway.im.enums.InterMsgType;
import com.youhaoxi.livelink.gateway.im.msg.InterMsg;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 内部消息处理 为了提高性能,在每个节点都保存了一份本地缓存,这个处理器则是为了接受相应的事件对本地缓存进行更新
 * 本地缓存路由信息包括,
 userId-->host
 roomId-->List<userId>
 那么当发生连接，断开，加入聊天室，退出聊天室时，需要对状态进行每个节点同步
 * 其实就是两个状态需要同步到每个节点
 * 一个就是用户的连接状态 userId--->host
 * 一个就是聊天室成员列表 roomId--->List<userId>
 */
public class InterMsgProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(InterMsgProcessor.class);

    @Override
    public void process(byte[] body) {
        try {

            String message = new String(body, "UTF-8");
            logger.info("InterMsgProcessor 收到内部状态同步消息:"+message);
            InterMsg msg = JSON.parseObject(message,InterMsg.class);

            //如果是当前host发出去的消息,直接忽略(因为目前采用的是fanout模式,自己发出去,自己也会收到刚刚那条消息)
            if(Constants.LOCALHOST.equals(msg.getHost())){
                return;
            }

            //连接事件
            if(msg.getInterMsgType().getValue()== InterMsgType.linked.getValue()){ //连接事件
                //host不为空 添加host映射关系表
//                if(StringUtils.isNotEmpty(msg.getHost())){
//                    //设置本地缓存
//                    UserRelationHashCache.setUserIdHostRelationLocal(msg.getUserId(),msg.getHost());
//                }
                //直接触发从redis刷新
                UserRelationHashCache.getUserIdHostRelation(msg.getUserId());

            }else if(msg.getInterMsgType().getValue() == InterMsgType.unlinked.getValue()){//连接断开事件
                //连接断开事件 移除本地缓存即可
                String roomId = UserRelationHashCache.getUserIdRoomIdRelation(msg.getUserId());
                //如果在聊天室 将他从聊天室移除
                if(StringUtils.isNotEmpty(roomId)){
                    RoomUserRelationSetCache.removeRoomIdMembersLocal(roomId,msg.getUserId());
                }
                //移除用户host映射关系
                UserRelationHashCache.removeUserIdHostRelationLocal(msg.getUserId());
                //移除用户与roomId映射
                UserRelationHashCache.removeUserIdRoomIdRelationLocal(msg.getUserId());
            }else if(msg.getInterMsgType().getValue() == InterMsgType.joinRoom.getValue()){////加入聊天室


                //先判断聊天室redis中是否存在,至少有一个成员,如果是刚创建的 也至少有一个房主在里面
                long count = RoomUserRelationSetCache.getRoomMembersCount(msg.getRoomId());
                if(count>0){
                    //加入聊天室  需要刷新两个数据
                    // 1个是userId-->roomId
                    UserRelationHashCache.setUserIdRoomIdRelationLocal(msg.getUserId(),msg.getRoomId());
                    // 1个是roomId-->userId 列表
                    // 聊天室如果人数太多,从redis同步不合适,所以采用直接添加
                    RoomUserRelationSetCache.setRoomIdMembersLocal(msg.getRoomId(),msg.getUserId());
                }else{
                    logger.error("InterMsgProcessor 加入的房间不存在,roomId:{},msg:",msg.getRoomId(),message);
                }

            }else if(msg.getInterMsgType().getValue() == InterMsgType.quitRoom.getValue()){//退出聊天室
                //退出聊天室  同样需要删除两个数据
                // 1个是userId-->roomId
                UserRelationHashCache.removeUserIdRoomIdRelationLocal(msg.getUserId());
                // 1个是roomId-->userId 列表
                RoomUserRelationSetCache.removeRoomIdMembersLocal(msg.getRoomId(),msg.getUserId());

            }else if(msg.getInterMsgType().getValue() == InterMsgType.delRoom.getValue()){//删除聊天室
                RoomUserRelationSetCache.delRoomLocal(msg.getRoomId());
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("InterMsgProcessor 出现UnsupportedEncodingException异常:{}",e);
        } catch (Exception e){
            logger.error("InterMsgProcessor 出现Exception异常:{}",e);
        }

    }


    public static void main(String[] args) {
        String s = " {\"code\":10,\"dest\":{\"userId\":111},\"from\":{\"img\":\"asdfas\",\"name\":\"xdsfa\",\"userId\":222},\"msg\":\"hello myname is zhoujielun\"}\n";

        ResultMsg rmsg = JSON.parseObject(s,ResultMsg.class);

        System.out.println(rmsg);
    }

}
