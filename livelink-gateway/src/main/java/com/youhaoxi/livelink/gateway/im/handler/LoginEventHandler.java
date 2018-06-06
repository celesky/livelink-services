package com.youhaoxi.livelink.gateway.im.handler;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.util.ClientPushUtil;
import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.im.enums.InterMsgType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.LoginEvent;
import com.youhaoxi.livelink.gateway.im.msg.EndpointMsg;
import com.youhaoxi.livelink.gateway.im.msg.InterMsg;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginEventHandler extends IMEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginEventHandler.class);

    public LoginEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(IWorker woker) {
        logger.debug(">>>用户登录事件处理:"+msg.toString());
        LoginEvent loginEvent = (LoginEvent)msg;
        boolean valid = checkLogin(loginEvent);
        //登录成功
        if(valid){
            EndpointMsg result = new EndpointMsg(100,"登录成功");
            //ctx.writeAndFlush(new TextWebSocketFrame("welcome! 登录成功 "));
            ClientPushUtil.writeToClient(ctx,result,loginEvent.from.userId);

            //添加到连接管理容器,并设置userId属性到Channel
            ConnectionManager.addConnection(loginEvent.from.getUserId(),ctx);
            //userId 和host主机映射关系 添加到redis
            UserRelationHashCache.setUserIdHostRelation(loginEvent.from.getUserId(),Constants.LOCALHOST);

            //传播登录连接事件给其他节点服务,同步用户登录状态
            InterMsg interMsg = new InterMsg();
            interMsg.setHost(Constants.LOCALHOST)
                    .setInterMsgType(InterMsgType.linked)
                    .setUserId(loginEvent.from.getUserId());
            woker.getInterMsgDispatcher().dispatch(interMsg);

        }else{
            //非法连接请求
            //关闭连接
            EndpointMsg result = new EndpointMsg(200,"登录失败!");
            ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(result))).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    }
                    ConnectionManager.closeConnection(ctx);
                }
            });
        }
    }
    /**
     * todo 调用rpc接口 检查用户权限
     * @param loginEvent
     * @return
     */
    private boolean checkLogin(LoginEvent loginEvent) {
        if(loginEvent.from==null){
            return false;
        }
        return true;
    }


    /**
     * 对用户登录请求进行认证
     * 登录失败关闭连接
     * @param msg
     * @param ctx
     */
//    private boolean authorize(Msg msg, ChannelHandlerContext ctx) {
//        boolean valid = checkLogin(msg);
//        //登录成功
//        if(valid){
//            ctx.writeAndFlush(new TextWebSocketFrame("welcome! 登录成功 "));
//            return valid;
//        }else{
//            //非法连接请求
//            //关闭连接
//            ctx.writeAndFlush(new TextWebSocketFrame("connect refuse!!! 登录失败! ")).addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    if (!future.isSuccess()) {
//                        future.cause().printStackTrace();
//                    }
//                    ConnectionManager.closeConnection(ctx);
//                }
//            });
//        }
//        return false;
//    }


}
