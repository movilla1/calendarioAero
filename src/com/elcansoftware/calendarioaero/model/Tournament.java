package com.elcansoftware.calendarioaero.model;

public class Tournament {
	int id;
	String title;
	int status;
	String description;
	
	
	public Tournament(){
		
	}
	
	public Tournament(int nid, String title, String desc) {
		this.id=nid;
		this.title=title;
		this.description=desc;
		this.status=1;
	}
	
	public Tournament(String title, String desc) {
		this.title=title;
		this.description=desc;
		this.status=1;
		this.id=0;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public void setTitle(String titl) {
		this.title=titl;
	}
	
	public void setDescription(String desc) {
		this.description=desc;
	}
	
	public void setStatus(int stat) {
		this.status=stat;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getStatus() {
		return this.status;
	}
}
