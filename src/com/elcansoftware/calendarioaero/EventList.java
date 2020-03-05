package com.elcansoftware.calendarioaero;

import com.elcansoftware.calendarioaero.helper.DatabaseHelper;
import com.elcansoftware.calendarioaero.model.Category;
import com.elcansoftware.calendarioaero.model.Tournament;
import com.elcansoftware.calendarioaero.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventList extends Activity {
	public static final int TYPE_CATEGORIES=1;
	public static final int TYPE_TOURNAMENTS=2;
	DatabaseHelper db;
	Category cat;
	Tournament tour;
	Cursor mCursor, res2;
	ListView lvMain;
	Button btBack;
	boolean out;

	protected void onCreate(Bundle savedInstanceState) {
		long catid = 0;
		int  type=0;
		Intent inte = this.getIntent();
		Bundle bnd = inte.getExtras();
		catid = bnd.getLong("id");
		type= bnd.getInt("type");
		String titl="";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventlisting);
		TextView tv = (TextView) findViewById(R.id.tvEventTitleItem);
		out = false;
		lvMain = (ListView) findViewById(R.id.lvEvents);
		//btBack = (Button) findViewById(R.id.btBack);
		try {
			db = new DatabaseHelper(getApplicationContext());
			// SQLiteDatabase dba=db.getReadableDatabase();
			if (type==TYPE_CATEGORIES) {
				cat = db.getCategory(catid);
				titl = cat.getTitle();
			}
			if (type==TYPE_TOURNAMENTS) {
				tour = db.getTournament(catid);
				titl = tour.getTitle();
			}
			tv.setText(titl);
			fillItems(catid,type);
		} catch (Exception ex) {
			Toast tt = Toast.makeText(getApplicationContext(),
					"ERROR evtl:" + ex.getMessage(), Toast.LENGTH_LONG);
			tt.show();
		}
	}

	public void onDestroy() {
		Intent inte = new Intent();
		inte.putExtra("event", "0");
		if (!out)
			setResult(RESULT_CANCELED, inte);
		super.onDestroy();
	}

	public void fillItems(long categ, int typ) {
		DatabaseHelper edb = new DatabaseHelper(getApplicationContext());
		SQLiteDatabase mydb = edb.getReadableDatabase();

		try {
			if (typ==TYPE_CATEGORIES) {
			res2 = mydb.rawQuery(
					"SELECT _id, title,evtdate FROM events WHERE category=?",
					new String[] { String.valueOf(categ) });
			} 
			if (typ==TYPE_TOURNAMENTS) {
				res2 = mydb.rawQuery(
						"SELECT _id, title,evtdate FROM events WHERE tournament=?",
						new String[] { String.valueOf(categ) });				
			}
			ListAdapter adapter = new SimpleCursorAdapter(this, // Context.
					R.layout.item_eventlist, // Specify the row template
					// to use (here, two
					// columns bound to the
					// two retrieved cursor
					// rows).
					res2, // Pass in the cursor to bind to.
					// Array of cursor columns to bind to.
					new String[] { "title", "evtdate" },
					// Parallel array of which template objects to bind to those
					// columns.
					new int[] { R.id.tvEventTitleItem, R.id.tvEvtDateItem },0);
			lvMain.setAdapter(adapter);
			lvMain.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent inte = new Intent(getApplicationContext(),
							EventDetails.class);
					Bundle extras = new Bundle();
					extras.putLong("eventid", id);
					inte.putExtras(extras);
					startActivity(inte);
				}
			});
		} catch (Exception e) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Error:" + e.getMessage(), Toast.LENGTH_LONG);
			t.show();
		}
	}
}
