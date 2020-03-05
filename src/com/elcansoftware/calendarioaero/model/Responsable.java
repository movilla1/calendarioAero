package com.elcansoftware.calendarioaero.model;

public class Responsable {
	String email;
	String name;
	String phone;
	String club;
	int id;
	
	public Responsable() {
		
	}
	
	public Responsable(int id, String name, String email, String phone, String club) {
		this.id=id;
		this.name=name;
		this.email=email;
		this.phone=phone;
		this.club=club;
	}
	
	public Responsable(String name, String email, String phone, String clb) {
		this.name=name;
		this.email=email;
		this.phone=phone;
		this.id=0;
		this.club=clb;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	
	public void setPhone(String phone) {
		this.phone=phone;
	}
	public void setClub(String club) {
		this.club=club;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getClub() {
		return this.club;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPhone() {
		return this.phone;
	}
}
