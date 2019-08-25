package com.java;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.pojo.Server;
import com.util.AppProps;

public class MonitorService {

	public static void main(String[] args) {
		
		AppProps.loadConfigurartion("app-properties.xml");

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(AppProps.serverlist.size());

		for (Server server : AppProps.serverlist) {
			executor.scheduleAtFixedRate(new ServerQuery(server), 0, server.getPollingfreq(), TimeUnit.SECONDS);
		}
		
		if(executor.isTerminated())
			System.out.println("Terminated");
	}

}
