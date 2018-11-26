package com.site.visits.model;

import java.io.Serializable;
import java.util.List;

public class Site implements Serializable  {
	
	private String name;
	private List<Visit> visits;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Visit> getVisits() {
		return visits;
	}
	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}
	
	
}
