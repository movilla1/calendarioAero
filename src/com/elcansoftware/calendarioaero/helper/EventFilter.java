package com.elcansoftware.calendarioaero.helper;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class EventFilter {
	protected String title;
	protected String datefrom;
	protected String dateto;
	protected String categoryid;
	protected String Tournamentid;
	protected String strAll;
	
	public EventFilter(String allstr) {
		this.title=new String();
		this.datefrom=new String();
		this.dateto=new String();
		this.categoryid= new String();
		this.Tournamentid=  new String();
		this.strAll=allstr;
		this.clear();
	}
	
	private boolean validateDate(String date) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	        sdf.parse(date);
	        return true;
	    }
	    catch(ParseException ex) {
	        return false;
	    }
	}
	
	public void setDateFrom(String datef) {
		if (this.validateDate(datef)) {
			this.datefrom=datef;
		}
	}
	
	public void setDateTo(String datet) {
		if (this.validateDate(datet)) {
			this.dateto=datet;
		}
	}
	
	public void setTitle(String tit) {
		this.title=tit;
	}
	
	public void setCategory(String categ) {
		this.categoryid=categ;
	}
	
	public void setTournament(String arr) {
		this.Tournamentid=arr;
	}
	
	public void clear() {
		this.title=" ";
		this.datefrom=" ";
		this.dateto=" ";
		this.categoryid=" ";
		this.Tournamentid=" ";
	}
	
	public String getFilter() {
		String flstr="";
		if (this.title.trim().length()>2) {
			flstr+=" AND title LIKE '%"+this.title+"%'";
		}
		if (this.dateto.length()>=10 && this.datefrom.length()>=10) {
			flstr+=" AND evtdate  BETWEEN '"+this.datefrom+"' AND '"+this.dateto+"'";
		} else {
			if (this.datefrom.length()>=10) {
				flstr+=" AND strftime('%s',evtdate) >= strftime('%s', '"+this.datefrom+"')";
			} 
			if (this.dateto.length()>=10) {
				flstr+=" AND strftime('%s',evtdate) <= strftime('%s', '"+this.dateto+"')";
			}
		}
		
		if (this.categoryid.length()>2 && !this.categoryid.contentEquals(this.strAll)) {
			flstr +=" AND (cat.title='"+this.categoryid+"' OR (parentt='"+this.categoryid+"' AND cat.parent<>0))";
		}
		
		if (this.Tournamentid.length()>2 && !this.Tournamentid.contentEquals(this.strAll)) {
			flstr +=" AND tou.title='"+this.Tournamentid +"'";
		}
		return flstr;
	}
	
	public String getConfig() {
		String str=this.title+"|"+this.datefrom+"|"+this.dateto+"|";
		str+=this.categoryid.toString()+"|"+this.Tournamentid.toString();
		return str;
	}
	
	public boolean setConfigString(String conf) {
		String[] arr=conf.split("\\|");
		if (arr.length<4) return false;
		setTitle(arr[0]);
		setDateFrom(arr[1]);
		setDateTo(arr[2]);
		if (arr[3].startsWith("+")) {
			setCategory(arr[3].substring(1));
		} else {
			setCategory(arr[3]);
		}
		setTournament(arr[4]);
		return true;
	}
}
