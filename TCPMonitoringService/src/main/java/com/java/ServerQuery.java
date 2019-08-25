package com.java;

import java.util.Date;

import com.pojo.DownTime;
import com.pojo.Server;
import com.pojo.Server.State;
import com.util.QueryUtil;

public class ServerQuery implements Runnable {

	private Server servToQuery;

	public ServerQuery(Server server) {
		this.servToQuery = server;
	}

	@Override
	public void run() {
		System.out.println(new Date() + ": Query server " + servToQuery.getName());

		boolean status = QueryUtil.queryTCP(servToQuery);

		// When service is up
		if (status) {
			servToQuery.retrycnt = 0;
			System.out.println(new Date() + " Server " + servToQuery.getName() + " status --> UP ");
			
			if(servToQuery.getCurrstate() == State.UNKNOWN) servToQuery.setCurrstate(State.UP);
			
			// Check state change from prev to new
			if (servToQuery.getCurrstate() == State.DOWN) {
				servToQuery.setCurrstate(State.UP);
				sendNotification();
			}
		}
		// When service is down
		else {
			// update retry counter
			servToQuery.retrycnt++;
			System.out.println(new Date() + " Server " + servToQuery.getName() + " status --> DOWN ");

			if(servToQuery.getCurrstate() == State.UNKNOWN) servToQuery.setCurrstate(State.DOWN);

			if (servToQuery.getCurrstate() == State.UP) {
				servToQuery.setCurrstate(State.DOWN);
				sendNotification();
			}
		}

		// if service is down after trying more than 5 times.
		if (servToQuery.retrycnt > 5 && servToQuery.getStarttime() == 0l) {
			// Wait for grace time
			System.out.println(new Date() + " Waiting for grace time for server to change state for " + servToQuery.getName());
			servToQuery.initGraceTime();
		}
	}
	
	/**
	 * Send notification only if server not under maintainance.
	 */
	private void sendNotification() {
		if (!isUnderMaintainance(servToQuery.getMaintainance()) ) {
			if(servToQuery.checkGraceTimeexpired()) {
				System.out.println(new Date() + ": Server " + servToQuery.getName() + " is " + servToQuery.getCurrstate()
						+ ", sending notification -->  ");
				//QueryUtil.sendNotification(servToQuery.getAlertlist(), servToQuery.getName(), servToQuery.getCurrstate());
			}else {
				System.out.println(new Date() + ": Server " + servToQuery.getName() + " is in grace time : no notification will be send");
			}
		} else {
			System.out.println(new Date() + "Server " + servToQuery.getName() + " is under maintainance : no notification will be send");
		}
	}

	/**
	 * Check if server falls in maintainace time
	 * 
	 * @param dt
	 * @return
	 */
	private boolean isUnderMaintainance(DownTime dt) {
		if (dt != null) {
			Date currdate = new Date();
			return currdate.after(dt.getStart()) && currdate.before(dt.getEnd());
		}
		return false;
	}

}
