package com.youhaoxi.livelink.gateway.im.msg;

import java.util.HashMap;

/**
 * 下发的消息协议
 */
public class ResultMsg {
    private Integer code;
    private String msg;
    public User from;
    public User dest;
    public Info info;
    public String roomId;//群id
    public Long timestamp;//群id

    private HashMap statsMap;//用于运行监测数据返回

    public ResultMsg(){

    }
    public ResultMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public ResultMsg setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultMsg setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public User getFrom() {
        return from;
    }

    public ResultMsg setFrom(User from) {
        this.from = from;
        return this;
    }

    public User getDest() {
        return dest;
    }

    public ResultMsg setDest(User dest) {
        this.dest = dest;
        return this;
    }

    public Info getInfo() {
        return info;
    }

    public ResultMsg setInfo(Info info) {
        this.info = info;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public ResultMsg setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    public class Info{

        public Integer itemId;//道具或者礼物id
        public String ItemName;//名称
        public String ItemImgUrl;//图片url
        public Integer itemCount;//道具数量



        public Integer getItemId() {
            return itemId;
        }

        public Info setItemId(Integer itemId) {
            this.itemId = itemId;
            return this;
        }

        public String getItemName() {
            return ItemName;
        }

        public Info setItemName(String itemName) {
            ItemName = itemName;
            return this;
        }

        public String getItemImgUrl() {
            return ItemImgUrl;
        }

        public Info setItemImgUrl(String itemImgUrl) {
            ItemImgUrl = itemImgUrl;
            return this;
        }

        public Integer getItemCount() {
            return itemCount;
        }

        public Info setItemCount(Integer itemCount) {
            this.itemCount = itemCount;
            return this;
        }
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public ResultMsg setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public HashMap getStatsMap() {
        return statsMap;
    }

    public ResultMsg setStatsMap(HashMap statsMap) {
        this.statsMap = statsMap;
        return this;
    }
}

