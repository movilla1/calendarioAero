package com.elcansoftware.calendarioaero.model;

public class ModelEvent {
	String title;
	String location;
	String coordinates;
	long responsable;
	String datetime;
	long id;
	boolean interest;
	boolean favourite;
	boolean remember;
	long category;
	long tournamentid;
	String eventlink;
	String notes;
	
	public ModelEvent(){ 
		this.id=0;
	}
	
	public ModelEvent(long id,String title, String location, String coordinates, long responsable, String datetime, boolean interest, boolean favourite, boolean remember, long categ, long tournament, String eventlink) {
		this.id=id;
		this.title=title;
		this.location=location;
		this.coordinates=coordinates;
		this.responsable=responsable;
		this.datetime=datetime;
		this.interest=interest;
		this.favourite=favourite;
		this.remember=remember;
		this.category=categ;
		this.tournamentid=tournament;
		this.eventlink=eventlink;
		this.notes="";
	}
	
	public ModelEvent(String title, String location, String coordinates, long responsables, String datetime) {
		this.title=title;
		this.location=location;
		this.responsable=responsables;
		this.datetime=datetime;
		this.id=0;
		this.interest=false;
		this.favourite=false;
		this.remember=false;
		this.coordinates=coordinates;
		this.eventlink="";
		this.notes="";
	}
	
	public void setId(long id) {
		this.id=id;
	}
	
	public void setTitle(String titl) {
		this.title=titl;
	}
	
	public void setLocation(String loc) {
		this.location=loc;
	}
	
	public void setCoordinates(String coord) {
		this.coordinates=coord;
	}
	
	public void setResponsable(long respid) {
		this.responsable=respid;
	}
	
	public void setDateTime(String dat) {
		this.datetime=dat;
	}
	
	public void setInterest(boolean inter) {
		this.interest=inter;
	}
	
	public void setRemember(boolean rem) {
		this.remember=rem;
	}
	
	public void setFavourite(boolean fav) {
		this.favourite=fav;
	}
	public void setCategoryId(long cid) {
		this.category=cid;
	}
	public void setTournamentId(long id) {
		this.tournamentid=id;
	}
	public void setEventLink(String link) {
		this.eventlink=link;
	}
	public void setNotes(String Notes) {
		this.notes=Notes;
	}
	
	public String getNotes() {
		return this.notes;
	}
	
	public String getEventLink() {
		return this.eventlink;
	}
	
	public long getTournamentId() {
		return this.tournamentid;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	public String getCoordinates() {
		return this.coordinates;
	}
	
	public long getResponsables(){
		return this.responsable;
	}
	
	public String getDatetime() {
		return this.datetime;
	}
	
	public boolean getInterest() {
		return this.interest;
	}
	
	public boolean getFavourite() {
		return this.favourite;
	}
	
	public boolean getRemember() {
		return this.remember;
	}
	
	public long getCategoryId(){
		return this.category;
	}
}
