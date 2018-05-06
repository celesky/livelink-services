package com.youhaoxi.livelink.gateway.hanlder;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    //private static final File INDEX;

    private final String wsUri;

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             FullHttpRequest request) throws Exception {
        if (wsUri.equalsIgnoreCase(request.uri())) {
            //如果请求了 WebSocket 协议升级，则增加引用 计数(调用 retain()方法)， 并将它传递给下一个 ChannelInboundHandler
            ctx.fireChannelRead(request.retain());
        } else {
            String content="hello";
            //普通http请求,先直接返回
            HttpResponse response = new DefaultHttpResponse(
                    request.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(
                    HttpHeaders.Names.CONTENT_TYPE,
                    "text/plain; charset=UTF-8");
            boolean keepAlive = HttpHeaders.isKeepAlive(request);

            if (keepAlive) {
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.length());
                response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
            //写 LastHttpContent 并冲刷至客户端
            ChannelFuture future = ctx.writeAndFlush( LastHttpContent.EMPTY_LAST_CONTENT);
            // 如果没有请求 keep-alive， 则在写操作完成后关闭 Channel
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }


    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendIndex(ChannelHandlerContext ctx, FullHttpRequest request)  throws Exception{
        File INDEX = new File("");
        //处理 100 Continue 请求以符合 HTTP 1.1 规范
        if (HttpHeaders.is100ContinueExpected(request)) {
            send100Continue(ctx);
        }
        // 读取 index.htm
        RandomAccessFile file = new RandomAccessFile(INDEX, "r");
        HttpResponse response = new DefaultHttpResponse(
                request.getProtocolVersion(), HttpResponseStatus.OK);

        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
        }
        // 将 HttpResponse 写到客户端
        ctx.write(response);
        // 将 index.html 写到客户端
        if (ctx.pipeline().get(SslHandler.class) == null) {
            ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        } else {
            ctx.write(new ChunkedNioFile(file.getChannel()));
        }
        //写 LastHttpContent 并冲刷至客户端
        ChannelFuture future = ctx.writeAndFlush( LastHttpContent.EMPTY_LAST_CONTENT);
        // 如果没有请求 keep-alive， 则在写操作完成后关闭 Channel
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
