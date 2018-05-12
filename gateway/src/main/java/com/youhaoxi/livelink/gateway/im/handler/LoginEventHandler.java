package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.util.ConnectionManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginEventHandler extends IMEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginEventHandler.class);

    public LoginEventHandler(ChannelHandlerContext ctx, Msg msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(Worker woker) {
        logger.debug(">>>用户登录事件处理:"+msg.toString());

        boolean valid = checkLogin(msg);
        //登录成功
        if(valid){
            ctx.writeAndFlush(new TextWebSocketFrame("welcome! 登录成功 "));

            //添加到连接管理容器,并设置userId属性到Channel
            ConnectionManager.addConnection(msg.getUser().getUserId(),ctx);
            //userId 和host主机映射关系 添加到redis
            ChatRoomRedisManager.setUserIdHostRelation(msg.getUser().getUserId());
        }else{
            //非法连接请求
            //关闭连接
            ctx.writeAndFlush(new TextWebSocketFrame("connect refuse!!! 登录失败! ")).addListener(new ChannelFutureListener() {
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
     * @param msg
     * @return
     */
    private boolean checkLogin(Msg msg) {
        if(msg.getUser().getUserId()==null){
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
