package com.youhaoxi.livelink.gateway;

import com.youhaoxi.livelink.gateway.common.util.RedisUtil;
import com.youhaoxi.livelink.gateway.server.starter.GatewayServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication implements CommandLineRunner {

    @Autowired
    GatewayServer gatewayServer;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        gatewayServer.startup();
	}
}
