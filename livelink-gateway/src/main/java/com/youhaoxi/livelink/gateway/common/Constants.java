package com.youhaoxi.livelink.gateway.common;

import com.youhaoxi.livelink.gateway.common.util.NetUtils;

public class Constants {

    //聊天消息
    public final static String CHAT_EXCHANGE_NAME = "direct_chat";

    //内部状态同步消息通讯
    public final static String INTER_EXCHANGE_NAME = "inter_fanout";

    public static final String LOCALHOST = NetUtils.getLocalAddress().getHostAddress();

//
}
