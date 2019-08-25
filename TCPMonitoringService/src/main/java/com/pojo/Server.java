package com.pojo;

import java.util.ArrayList;

public class Server {

	private String name;

	private String hostname;

	private int port;

	private int pollingfreq;

	private int gracetime;

	private ArrayList<String> alertlist = new ArrayList<>();

	private DownTime maintainance = null;

	private State currstate = State.UNKNOWN;

	private String error = null;
	
	public int retrycnt;
	
	private long starttime;

	public enum State {
		UP, DOWN, UNKNOWN
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPollingfreq() {
		return pollingfreq;
	}

	public void setPollingfreq(int pollingfreq) {
		this.pollingfreq = pollingfreq;
	}

	public int getGracetime() {
		return gracetime;
	}

	public void setGracetime(int gracetime) {
		this.gracetime = gracetime;
	}

	public ArrayList<String> getAlertlist() {
		return alertlist;
	}

	public void setAlertlist(ArrayList<String> alertlist) {
		this.alertlist = alertlist;
	}

	public DownTime getMaintainance() {
		return maintainance;
	}

	public void setMaintainance(DownTime maintainance) {
		this.maintainance = maintainance;
	}

	public State getCurrstate() {
		return currstate;
	}

	public void setCurrstate(State currstate) {
		this.currstate = currstate;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public void initGraceTime() {
		starttime = System.currentTimeMillis();	
	}
	
	public boolean checkGraceTimeexpired() {
		long now = System.currentTimeMillis();
		
		if(now - starttime > (gracetime * 60 * 1000)) {
			starttime = 0l;
			return true;
		}
		return false;
	}

	public long getStarttime() {
		return starttime;
	}
	
	@Override
	public String toString() {
		return "Server [name=" + name + ", hostname=" + hostname + ", port=" + port + ", poll=" + pollingfreq + ", gracetime=" + gracetime 
				+ ", alertlist=" + alertlist + ", maintainance=" + maintainance + ", currstate=" + currstate
				+ ", error=" + error + "]";
	}
}
