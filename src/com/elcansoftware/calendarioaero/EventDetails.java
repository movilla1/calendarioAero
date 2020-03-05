package com.elcansoftware.calendarioaero;

import com.elcansoftware.calendarioaero.helper.DatabaseHelper;
import com.elcansoftware.calendarioaero.model.Category;
import com.elcansoftware.calendarioaero.model.ModelEvent;
import com.elcansoftware.calendarioaero.model.Responsable;
import com.elcansoftware.calendarioaero.model.Tournament;
import com.elcansoftware.calendarioaero.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetails extends Activity {
	ModelEvent evt;
	Category categ;
	Responsable resp;
	Tournament tour;
	DatabaseHelper dbh;
	TextView tvTitle;
	TextView tvCateg;
	TextView tvResp;
	TextView tvDate;
	TextView tvLocat;
	TextView tvCoord, tvTour,tvNote;
	CheckBox cbFavourite, cbInterested, cbAlarm;
	Button btBack, btSubscribe;
	Button sMaps;

	protected void onCreate(Bundle savedInstanceState) {
		long eventid = 0;
		super.onCreate(savedInstanceState);
		dbh = new DatabaseHelper(getApplicationContext());

		Intent inte = this.getIntent();
		Bundle bnd = inte.getExtras();
		eventid = bnd.getLong("eventid");
		if (eventid > 0) {
			evt = dbh.getEvent(eventid);
			if (evt != null) {
				try {
					categ = dbh.getCategory(evt.getCategoryId());
					resp = dbh.getResponsable(evt.getResponsables());
					tour = dbh.getTournament(evt.getTournamentId());
				} catch (Exception ex) {
					if (resp == null)
						resp = new Responsable("n/a", "Fail", "Fail", "none");
					if (tour == null)
						tour = new Tournament("n/a", "fail");
				}
			}
		}
		setContentView(R.layout.eventdetails);
		tvTitle = (TextView) findViewById(R.id.tvEvtDetTitle);
		tvCateg = (TextView) findViewById(R.id.tvDetCategory);
		tvResp = (TextView) findViewById(R.id.tvDetResp);
		tvDate = (TextView) findViewById(R.id.tvDetDate);
		tvLocat = (TextView) findViewById(R.id.tvDetLocation);
		tvCoord = (TextView) findViewById(R.id.tvDetCoord);
		tvTour = (TextView) findViewById(R.id.tvtTournament);
		sMaps = (Button) findViewById(R.id.btMaps);
		btSubscribe = (Button) findViewById(R.id.btSubscribe);
		tvNote=(TextView) findViewById(R.id.tvNotes);
		/*
		 * cbFavourite=(CheckBox) findViewById(R.id.chbFav);
		 * cbInterested=(CheckBox) findViewById(R.id.chbInterest);
		 * cbAlarm=(CheckBox) findViewById(R.id.chbAlert);
		 */
		if (evt != null && categ != null) {
			if (resp != null) {
				tvResp.setText(resp.getName());
			}
			tvCateg.setText(categ.getTitle());
			tvTour.setText(tour.getTitle());

			tvTitle.setText(evt.getTitle());
			tvDate.setText(evt.getDatetime());
			tvLocat.setText(evt.getLocation());
			tvCoord.setText(evt.getCoordinates());
			String note=evt.getNotes();
			if (note!=null && note.length()>2) {
				tvNote.setText(note);
			}
			/*
			 * cbFavourite.setChecked(evt.getFavourite());
			 * cbInterested.setChecked(evt.getInterest());
			 */
			// cbAlarm.setChecked();
		}

		sMaps.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					if (tvCoord.getText().length() > 1) {
						String geoUri = "http://maps.google.com/maps?q=loc:"
								+ tvCoord.getText() + " (" + tvTitle.getText()
								+ ")";
						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW, Uri
										.parse(geoUri));
						startActivity(intent);
					} else {
						Toast.makeText(getApplicationContext(),
								getString(R.string.nocoordinateserror),
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(),
							"Error:" + ex.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		tvResp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (resp.getId() > 0) {
					Intent inte = new Intent(getApplicationContext(),
							ResponsableDetails.class);
					Bundle extr = new Bundle();
					extr.putLong("respid", resp.getId());
					extr.putString("eventname", evt.getTitle());
					inte.putExtras(extr);
					startActivity(inte);
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.errormissingresponsable),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		btSubscribe.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String url = evt.getEventLink().trim();
				if (url.length() > 5) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					startActivity(browserIntent);
				}
			}
		});
	}

	public void onDestroy() {
		Intent inte = new Intent();
		inte.putExtra("event", "0");
		setResult(RESULT_CANCELED, inte);
		super.onDestroy();
	}

}
