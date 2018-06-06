package com.youhaoxi.livelink.dispatch;

import com.youhaoxi.livelink.dispatch.server.starter.DispatchServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"applicationContext-core.xml"})
public class DispatchApplication implements CommandLineRunner {

	@Autowired
	DispatchServer dispatchServer;

	public static void main(String[] args) {
		SpringApplication.run(DispatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dispatchServer.startup();
	}
}
