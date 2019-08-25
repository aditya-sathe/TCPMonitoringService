package com.pojo;

import java.util.Date;

public class DownTime {
	
	private Date start;
	
	private Date end;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "DownTime [start=" + start + ", end=" + end + "]";
	}
	
}
