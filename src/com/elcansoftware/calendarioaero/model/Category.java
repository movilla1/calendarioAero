package com.elcansoftware.calendarioaero.model;

public class Category {
	int id;
	String title;
	int status;
	String description;
	int parentcateg;
	
	public Category(){
		
	}
	
	public Category(int nid, String title, String desc,int parentcat) {
		this.id=nid;
		this.title=title;
		this.description=desc;
		this.status=1;
		this.parentcateg=parentcat;
	}
	
	public Category(String title, String desc) {
		this.title=title;
		this.description=desc;
		this.status=1;
		this.id=0;
		this.parentcateg=0;
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
	
	public void setParent(int pcateg) {
		this.parentcateg=pcateg;
	}
	
	public int getParentCateg() {
		return this.parentcateg;
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
