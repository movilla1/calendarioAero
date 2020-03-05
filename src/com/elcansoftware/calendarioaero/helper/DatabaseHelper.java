package com.elcansoftware.calendarioaero.helper;

import java.util.ArrayList;
import java.util.List;

import com.elcansoftware.calendarioaero.model.Category;
import com.elcansoftware.calendarioaero.model.ModelEvent;
import com.elcansoftware.calendarioaero.model.Responsable;
import com.elcansoftware.calendarioaero.model.Tournament;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "calendarioaero.db";
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_RESPONSABLE = "responsables";
    private static final String TABLE_TOURNAMENTS = "tournaments";
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DESC = "description";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "evtdate";
    private static final String KEY_STATUS = "status";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_COORDINATES = "coordinates";
    private static final String KEY_RESPONSABLE = "responsable";
    private static final String KEY_INTERESTED = "interested";
    private static final String KEY_FAVOURITE = "favourite";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TOURNAMENT = "tournament";
    private static final String KEY_PARENT = "parent";
    private static final String KEY_LINK = "eventlink";
    private static final String KEY_CLUB = "club";
    private static final String KEY_NOTES = "notes";
    
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE "
            + TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE +" TEXT,"+KEY_STATUS+" INTEGER,"+KEY_DESC+" TEXT,"
            +KEY_PARENT+" INTEGER);";
    
    private static final String CREATE_TABLE_TOURNAMENTS = "CREATE TABLE "
            + TABLE_TOURNAMENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE +" TEXT,"+KEY_STATUS+" INTEGER,"+KEY_DESC+" TEXT);";
    
    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE "
            + TABLE_EVENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT, "+KEY_LOCATION+" TEXT,"+ KEY_COORDINATES+" TEXT,"
            + KEY_RESPONSABLE + " INTEGER, "+KEY_DATE+" DATETIME,"
            + KEY_INTERESTED +" INTEGER,"+KEY_FAVOURITE+" INTEGER,"
            + KEY_REMEMBER +" INTEGER, "+KEY_CATEGORY+" INTEGER,"+KEY_TOURNAMENT+" INTEGER,"
            + KEY_LINK+" TEXT, "+KEY_NOTES+" TEXT )";
    
    
    private static final String CREATE_TABLE_RESPONSABLE = "CREATE TABLE "
            + TABLE_RESPONSABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"+ KEY_EMAIL +" TEXT,"
            + KEY_PHONE + " TEXT, "+KEY_CLUB+" TEXT)";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_RESPONSABLE);
        db.execSQL(CREATE_TABLE_TOURNAMENTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldver, int newver) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURNAMENTS);
        // create new tables
        onCreate(db);
	}
	
	public long createCategory(Category cat) {
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.put(KEY_ID, cat.getId());
		val.put(KEY_TITLE,cat.getTitle());
		val.put(KEY_DESC,cat.getDescription());
		val.put(KEY_STATUS,cat.getStatus());
		val.put(KEY_PARENT, cat.getParentCateg());
		long catid=db.insert(TABLE_CATEGORY,null,val);
		return catid;
	}
	
	public long createResponsable(Responsable resp) {
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.put(KEY_ID, resp.getId());
		val.put(KEY_NAME, resp.getName());
		val.put(KEY_PHONE, resp.getPhone());
		val.put(KEY_EMAIL, resp.getEmail());
		val.put(KEY_CLUB, resp.getClub());
		long catid = db.insert(TABLE_RESPONSABLE, null, val);
		return catid;
	}

	public long createEvent(ModelEvent evt, long categoryid, long responsableid, long tourid) {
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.put(KEY_ID, evt.getId());
		val.put(KEY_TITLE, evt.getTitle());
		val.put(KEY_DATE, evt.getDatetime());
		val.put(KEY_COORDINATES, evt.getCoordinates());
		val.put(KEY_LOCATION, evt.getLocation());
		val.put(KEY_FAVOURITE, evt.getFavourite());
		val.put(KEY_REMEMBER, evt.getRemember());
		val.put(KEY_INTERESTED, evt.getInterest());
		val.put(KEY_RESPONSABLE, responsableid);
		val.put(KEY_CATEGORY, categoryid);
		val.put(KEY_TOURNAMENT, tourid);
		val.put(KEY_LINK, evt.getEventLink());
		long eventid = db.insert(TABLE_EVENTS,null,val);
		
		return eventid;
	}
	
	public Category getCategory(long catid) {
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE "
	            + KEY_ID + " = " + catid;
	 
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null)
	        c.moveToFirst();
	 
	    Category td = new Category();
	    td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	    td.setDescription(c.getString(c.getColumnIndex(KEY_DESC)));
	    td.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
	    td.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
	    td.setParent(c.getInt(c.getColumnIndex(KEY_PARENT)));
	    return td;
	}
	
	public Responsable getResponsable(long respons_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    String selectQuery = "SELECT  * FROM " + TABLE_RESPONSABLE + " WHERE "
	            + KEY_ID + " = " + respons_id;
		
	    Cursor c = db.rawQuery(selectQuery, null);
		 
	    if (c != null)
	        c.moveToFirst();
	    
	    Responsable res = new Responsable();
	    res.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	    res.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
	    res.setName(c.getString(c.getColumnIndex(KEY_NAME)));
	    res.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
	    res.setClub(c.getString(c.getColumnIndex(KEY_CLUB)));
	    return res;
	}
	
	public ModelEvent getEvent(long eventid) {
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    String selectQuery = "SELECT  * FROM " + TABLE_EVENTS + " WHERE "
	            + KEY_ID + " = " + eventid;
		
	    Cursor c = db.rawQuery(selectQuery, null);
		 
	    if (c != null)
	        c.moveToFirst();
	    
	    ModelEvent res = new ModelEvent();
	    res.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	    res.setDateTime(c.getString(c.getColumnIndex(KEY_DATE)));
	    res.setResponsable(c.getLong(c.getColumnIndex(KEY_RESPONSABLE)));
	    res.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
	    res.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
	    res.setInterest(c.getInt(c.getColumnIndex(KEY_INTERESTED))>0);
	    res.setRemember(c.getInt(c.getColumnIndex(KEY_REMEMBER))>0);
	    res.setFavourite(c.getInt(c.getColumnIndex(KEY_FAVOURITE))>0);
	    res.setCategoryId(c.getLong(c.getColumnIndex(KEY_CATEGORY)));
	    res.setCoordinates(c.getString(c.getColumnIndex(KEY_COORDINATES)));
	    res.setTournamentId(c.getLong(c.getColumnIndex(KEY_TOURNAMENT)));
	    res.setEventLink(c.getString(c.getColumnIndex(KEY_LINK)));
	    return res;
	}
	
	public List<Category> getAllCategories() {
	    List<Category> todos = new ArrayList<Category>();
	    String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
	 
	    Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            Category td = new Category();
	            td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
	            td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
	            td.setDescription(c.getString(c.getColumnIndex(KEY_DESC)));
	            td.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
	            // adding to todo list
	            todos.add(td);
	        } while (c.moveToNext());
	    }
	 
	    return todos;
	}
		
	public List<ModelEvent> getEventsByCategory(long catid) {
		List<ModelEvent> todos= new ArrayList<ModelEvent>();
		String selQuery = "SELECT * FROM "+TABLE_EVENTS+" WHERE "+KEY_CATEGORY+ "=?";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selQuery, new String[] { String.valueOf(catid)});
		if (c.moveToFirst()) {
			do {
				ModelEvent ev = new ModelEvent();
				ev.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				ev.setDateTime(c.getString(c.getColumnIndex(KEY_DATE)));
				ev.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				ev.setResponsable(c.getLong(c.getColumnIndex(KEY_RESPONSABLE)));
				ev.setFavourite(c.getInt(c.getColumnIndex(KEY_FAVOURITE))>0);
				ev.setInterest(c.getInt(c.getColumnIndex(KEY_INTERESTED))>0);
				ev.setRemember(c.getInt(c.getColumnIndex(KEY_REMEMBER))>0);
				ev.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
				todos.add(ev);
			} while (c.moveToNext());
		}
		return todos;
	}
	
	public Tournament getTournament(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    String selectQuery = "SELECT  * FROM " + TABLE_TOURNAMENTS + " WHERE "
	            + KEY_ID + " = " + id;
		
	    Cursor c = db.rawQuery(selectQuery, null);
		 
	    if (c != null)
	        c.moveToFirst();
	    Tournament tou = new Tournament();
	    tou.setDescription(c.getString(c.getColumnIndex(KEY_DESC)));
	    tou.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
	    tou.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
	    return tou;
	}
	
	public long createTournament(Tournament tour) {
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues val = new ContentValues();
		val.put(KEY_TITLE,tour.getTitle());
		val.put(KEY_DESC,tour.getDescription());
		val.put(KEY_STATUS,tour.getStatus());
		long catid=db.insert(TABLE_TOURNAMENTS,null,val);
		return catid;
	}
	
	public void truncateCategories() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CATEGORY, "_id>0", null);
	}
	
	public void truncateEvents() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EVENTS, "_id>0", null);
	}
	
	public void truncateResponsables() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RESPONSABLE, "_id>0", null);
	}
	
	public void truncateTournaments() {
		SQLiteDatabase db= this.getWritableDatabase();
		db.delete(TABLE_TOURNAMENTS, "_id>0", null);
	}
	
	public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
