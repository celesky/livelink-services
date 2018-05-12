package com.youhaoxi.livelink.gateway.common;

import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

public class FileUtil {
    AtomicLong count = new AtomicLong(0);

    public  void readF1(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath)));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            //System.out.println(line);
            splitGroupId( line);
        }
        br.close();
    }


    public void start(){

        try {
            readF1("/Users/pan/Desktop/ddd");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            readF1("/Users/pan/Desktop/222");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("count = " + count.get());
    }


    private void splitGroupId(String groupIds) {
        if(groupIds!=null&&groupIds.contains(",")){
            String userId = groupIds.split(",")[0];

            Iterator<String> it = Splitter.on(",").split(groupIds).iterator();
            while(it.hasNext()){
                String groupId = it.next();
                count.incrementAndGet();
                //System.out.println("groupId = " + groupId);
                //RedisUtil.cache().pfAdd("ALLGROUPSCOUNT",groupId);
            }
        }else{
            count.incrementAndGet();
            RedisUtil.cache().pfAdd("ALLGROUPSCOUNT",groupIds);
        }
    }

    public static void main(String[] args) {
        new FileUtil().start();
    }
}
