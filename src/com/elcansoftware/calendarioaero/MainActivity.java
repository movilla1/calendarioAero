package com.elcansoftware.calendarioaero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.elcansoftware.calendarioaero.helper.DatabaseHelper;
import com.elcansoftware.calendarioaero.helper.EventFilter;
import com.elcansoftware.calendarioaero.model.Category;
import com.elcansoftware.calendarioaero.model.ModelEvent;
import com.elcansoftware.calendarioaero.model.Responsable;
import com.elcansoftware.calendarioaero.model.Tournament;
import com.elcansoftware.calendarioaero.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	DatabaseHelper db;
	EventFilter flt;
	Cursor mCursor, res2;
	ListView lvMain;
	Button btf;
	//SharedPreferences settings;
	public static final int ACT_SETTINGS_EDIT = 1;
	public static final int ACT_REQUEST_CONFIG = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHelper(getApplicationContext());
		setContentView(R.layout.activity_main);
		flt = new EventFilter(getString(R.string.all));
		String datt = (String) DateFormat.format("yyyy-MM-dd", Calendar
				.getInstance().getTime());
		flt.setDateFrom(datt);
		lvMain = (ListView) findViewById(R.id.lvMainCat);
		btf = (Button) findViewById(R.id.btFilter);
		btf.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent inte = new Intent(getApplicationContext(),
						FilterActivity.class);
				Bundle extras = new Bundle();
				extras.putString("config", flt.getConfig());
				inte.putExtras(extras);
				startActivityForResult(inte, ACT_REQUEST_CONFIG);
			}
		});
		fillItems();
	}

	public void fillItems() {
		SQLiteDatabase mydb = db.getReadableDatabase();
		try {
			String sql = "SELECT ev._id, ev.title,ev.evtdate,cat.title as categ,tou.title as tourn, pcat.title as parentt ";
			sql += "FROM events as ev LEFT JOIN categories as cat ON ev.category=cat._id LEFT JOIN";
			sql += " tournaments AS tou ON tou._id=ev.tournament LEFT JOIN categories as pcat ON ";
			sql += "cat.parent=pcat._id WHERE 1 "
					+ flt.getFilter() + " ORDER BY ev.evtdate";
			mCursor = mydb.rawQuery(sql, null);
			ListAdapter adapter = new SimpleCursorAdapter(this, // Context.
					R.layout.mainlistitem, // Specify the row template
											// to use (here, two
											// columns bound to the
											// two retrieved cursor
											// rows).
					mCursor, // Pass in the cursor to bind to.
					// Array of cursor columns to bind to.
					new String[] { "ev.title", "evtdate", "tourn" },
					// Parallel array of which template objects to bind to those
					// columns.
					new int[] { R.id.tvTitleDetails, R.id.tvDetailshort,
							R.id.tvtTournament }, 0);
			lvMain.setAdapter(adapter);
			lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
		} catch (Exception ex) {
			Toast t = Toast.makeText(this.getApplicationContext(),
					"Error fill:" + ex.getMessage(), Toast.LENGTH_LONG);
			t.show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == ACT_REQUEST_CONFIG) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				Bundle bnd = data.getExtras();
				String config = bnd.getString("config");
				flt.clear();
				flt.setConfigString(config);
				fillItems();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent inte;
		switch (item.getItemId()) {
		/*case R.id.action_settings:
			inte = new Intent(getApplicationContext(), SettingsMTC.class);
			startActivity(inte);
			break;*/
		case R.id.action_about:
			inte = new Intent(getApplicationContext(), AboutUs.class);
			startActivity(inte);
			break;
		case R.id.action_sync:
			AsyncTaskRunner ast = new AsyncTaskRunner();
			ast.execute();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void fetchWebSiteUsingHttpClient() throws JSONException {
		HttpClient client = new DefaultHttpClient();
		/*SharedPreferences sharedPref = getSharedPreferences("ElcanMCT",
				MODE_PRIVATE);*/
		String url = "http://www.elcansoftware.com/elcancalendar/index.php?r=export/json";//sharedPref.getString("server", "");
		db.truncateCategories();
		db.truncateEvents();
		db.truncateResponsables();
		db.truncateTournaments();
		if (url.trim().length()<10) return;
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStreamReader in = new InputStreamReader(entity.getContent());
			BufferedReader reader = new BufferedReader(in);

			StringBuilder stringBuilder = new StringBuilder();
			String line = "";

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);

			}

			JSONObject jsonArry = new JSONObject(stringBuilder.toString());
			JSONObject jsonObject;
			JSONArray jso = jsonArry.getJSONArray("categories"); // categories
			Category cat; // first
			int parent;

			for (int i = 0; i < jso.length(); i++) {
				jsonObject = jso.getJSONObject(i);
				cat = new Category();
				cat.setId(jsonObject.getInt("id"));
				cat.setTitle(jsonObject.getString("title"));
				cat.setStatus(jsonObject.getInt("status"));
				cat.setDescription(jsonObject.getString("description"));
				parent=jsonObject.getInt("parent");
				if (parent>-1) cat.setParent(parent); else cat.setParent(0);
				db.createCategory(cat);
			}

			Tournament tour;
			jso = jsonArry.getJSONArray("tournaments"); // categories
			for (int i = 0; i < jso.length(); i++) {
				jsonObject = jso.getJSONObject(i);
				tour = new Tournament();

				tour.setId(jsonObject.getInt("id"));
				tour.setTitle(jsonObject.getString("title"));
				tour.setStatus(jsonObject.getInt("status"));
				tour.setDescription(jsonObject.getString("description"));
				db.createTournament(tour);
			}

			Responsable resp;
			jso = jsonArry.getJSONArray("responsables");
			for (int i = 0; i < jso.length(); i++) {
				jsonObject = jso.getJSONObject(i);
				resp = new Responsable();
				resp.setId(jsonObject.getInt("id"));
				resp.setName(jsonObject.getString("name"));
				resp.setEmail(jsonObject.getString("email"));
				resp.setPhone(jsonObject.getString("phone"));
				if (jsonObject.has("club")) resp.setClub(jsonObject.getString("club"));
				db.createResponsable(resp);
			}

			ModelEvent event;
			jso = jsonArry.getJSONArray("events");
			long catid, respid, tourid;
			for (int i = 0; i < jso.length(); i++) {
				jsonObject = jso.getJSONObject(i);
				event = new ModelEvent();
				event.setId(jsonObject.getInt("id"));
				event.setTitle(jsonObject.getString("title"));
				event.setLocation(jsonObject.getString("location"));
				event.setCoordinates(jsonObject.getString("coordinates"));
				event.setDateTime(jsonObject.getString("evtdate"));
				catid = jsonObject.getInt("category");
				event.setCategoryId(catid);
				respid = jsonObject.getInt("responsable");
				event.setResponsable(respid);
				tourid = jsonObject.getInt("tournamentid");
				event.setTournamentId(tourid);
				if (jsonObject.has("eventlink")) event.setEventLink(jsonObject.getString("eventlink"));
				if (jsonObject.has("notes")) event.setNotes(jsonObject.getString("notes"));
				db.createEvent(event, catid, respid, tourid);
			}

		} catch (IOException e) {
			throw new JSONException("Failed to work");
		}
	}

	private class AsyncTaskRunner extends AsyncTask<String, String, String> {
		protected ProgressDialog ringProgressDialog;

		public AsyncTaskRunner() {
			ringProgressDialog = ProgressDialog.show(MainActivity.this,
					getString(R.string.pleasewait),
					getString(R.string.syncronizing), true);
			ringProgressDialog.setCancelable(true);
		}

		protected String doInBackground(String... params) {
			try {
				fetchWebSiteUsingHttpClient();
			} catch (Exception ex) {
				Toast.makeText(getBaseContext(),
						"Error:" + ex.getMessage(), Toast.LENGTH_LONG).show();
				return "failed";
			}
			return null;
		}

		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			fillItems();
			if (ringProgressDialog.isShowing()) {
				ringProgressDialog.dismiss();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			ringProgressDialog.show();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(String... text) {
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
		}
	}
	
	
}

