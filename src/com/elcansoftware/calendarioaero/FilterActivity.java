package com.elcansoftware.calendarioaero;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.elcansoftware.calendarioaero.helper.DatabaseHelper;
import com.elcansoftware.calendarioaero.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class FilterActivity extends Activity {
	DatabaseHelper dbh;
	Button btn, btPickFrom,btPickTo;
	TextView datefrom, dateto;
	Boolean exiting;
	Spinner spTour, spCat;
	static final int DATE_DIALOG_ID = 999;
	AdView ads;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent inte = this.getIntent();
		Bundle bnd = inte.getExtras();
		String conf = bnd.getString("config");
		String[] arr = conf.split("\\|");
		dbh = new DatabaseHelper(getApplicationContext());
		setContentView(R.layout.activity_filter);
		btn = (Button) findViewById(R.id.btdFilter);
		exiting = Boolean.FALSE;
		spTour = (Spinner) findViewById(R.id.spTournament);
		spCat = (Spinner) findViewById(R.id.spCategory);
		btPickFrom = (Button) findViewById(R.id.btPickFrom);
		btPickTo = (Button) findViewById(R.id.btPickTo);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateFields()) {
					exiting = Boolean.TRUE;
					Bundle bnd = new Bundle();
					bnd.putString("config", getMyConfig());
					Intent data = new Intent();
					data.putExtras(bnd);
					setResult(RESULT_OK, data);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Error:" + getString(R.string.pleasecheckfields),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM tournaments", null);
		mCursor.moveToFirst();
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.all));
		if (mCursor.getCount() > 0) {
			do {
				list.add(mCursor.getString(mCursor.getColumnIndex("title")));
			} while (mCursor.moveToNext());
		}
		String[] arr2 = list.toArray(new String[list.size()]);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr2);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mCursor.close();
		mCursor = db.rawQuery(
				"SELECT _id,title FROM categories where parent=0", null);
		list.clear();
		list.add(getString(R.string.all));
		Cursor cur2;
		String myString;
		String strid;
		mCursor.moveToFirst();
		if (mCursor.getCount() > 0) {
			do {

				strid = String.valueOf(mCursor.getInt(mCursor
						.getColumnIndex("_id")));
				if (mCursor.getCount() > 0) {
					list.add(mCursor.getString(mCursor.getColumnIndex("title")));
					cur2 = db.rawQuery(
							"SELECT _id,title FROM categories where parent=?",
							new String[] { strid });
					if (cur2 != null) {
						cur2.moveToFirst();
						do {
							if (cur2.getCount() > 0) {
								myString = "+"
										+ cur2.getString(cur2
												.getColumnIndex("title"));
								list.add(myString);
							}
						} while ((cur2.moveToNext()));
						cur2.close();
					}
				}
			} while (mCursor.moveToNext());
		}
		String[] arr3 = list.toArray(new String[list.size()]);
		ArrayAdapter<String> adap2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, arr3);
		spTour.setAdapter(dataAdapter);
		spCat.setAdapter(adap2);
		final Context ctx=this;
		datefrom = (TextView) findViewById(R.id.tvDateFrom);
		dateto = (TextView) findViewById(R.id.tvDateTo);
		// titl.setText(arr[0].trim());
		datefrom.setText(arr[1].trim());
		dateto.setText(arr[2].trim());
		btPickFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);
				 
				DatePickerDialog dpd = new DatePickerDialog(ctx,
				        new DatePickerDialog.OnDateSetListener() {
				            public void onDateSet(DatePicker view, int year,
				                    int monthOfYear, int dayOfMonth) {
				                datefrom.setText(String.valueOf(year) + "-"
				                        + String.format("%02d",(monthOfYear + 1)) + "-" + String.format("%02d",dayOfMonth) );
				 
				            }
				        }, mYear, mMonth, mDay);
				dpd.show();
			}
		});
		
		btPickTo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog dpd = new DatePickerDialog(ctx,
				        new DatePickerDialog.OnDateSetListener() {
				            public void onDateSet(DatePicker view, int year,
				                    int monthOfYear, int dayOfMonth) {
				                dateto.setText( String.valueOf(year)+ "-"
				                        + String.format("%02d",(monthOfYear + 1)) + "-" +String.format("%02d",dayOfMonth) );
				 
				            }
				        }, mYear, mMonth, mDay);
				dpd.show();
				
			}
		});
		
		try {
			ads = new AdView(this);
			ads.setAdUnitId("ca-app-pub-4265440469365469/4470102109");
			ads.setAdSize(AdSize.BANNER);
			LinearLayout layout = (LinearLayout)findViewById(R.id.LinearLayout2f);
			layout.addView(ads);
			AdRequest adRequest = new AdRequest.Builder().build();

			// Cargar adView con la solicitud de anuncio.
			ads.loadAd(adRequest);
		} catch (Exception ex) {
		}
	}

	protected String getMyConfig() {
		String rest;
		rest = " |" + datefrom.getText() + "|" + dateto.getText() + "|"
				+ spCat.getSelectedItem().toString() + "|"
				+ spTour.getSelectedItem().toString();
		return rest;
	}

	protected void onDestroy() {
		if (!exiting) {
			this.setResult(RESULT_CANCELED);
		}
		super.onDestroy();
	}

	protected boolean validateFields() {
		boolean ret;
		ret = true;
		if (dateto.getText().length() == 10) {
			ret = validateDate(dateto.getText().toString());
		} else if (dateto.getText().length() != 0) {
			ret = false;
		}
		if (datefrom.getText().length() == 10) {
			ret = ret && validateDate(datefrom.getText().toString());
		} else if (datefrom.getText().length() != 0) {
			ret = false;
		}
		return ret;
	}

	private boolean validateDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sdf.parse(date);
			return true;
		} catch (ParseException ex) {
			return false;
		}
	}
}
