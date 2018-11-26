package com.site.visits.model;

import java.io.Serializable;

public class Visit implements Serializable {
	private long timeSpent;
	private long contentType;
	private long contentCategory;
	private String url;
	
	public long getTimeSpent() {
		return timeSpent;
	}
	public void setTimeSpent(long timeSpent) {
		this.timeSpent = timeSpent;
	}
	public long getContentType() {
		return contentType;
	}
	public void setContentType(long contentType) {
		this.contentType = contentType;
	}
	public long getContentCategory() {
		return contentCategory;
	}
	public void setContentCategory(long contentCategory) {
		this.contentCategory = contentCategory;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
