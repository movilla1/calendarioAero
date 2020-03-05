package com.elcansoftware.calendarioaero;

import com.elcansoftware.calendarioaero.helper.DatabaseHelper;
import com.elcansoftware.calendarioaero.model.Responsable;
import com.elcansoftware.calendarioaero.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResponsableDetails extends Activity {
	DatabaseHelper dbh;
	Responsable res;
	TextView tvRespName;
	TextView tvRespPhone, tvClub;
	TextView tvRespEmail, tvRespEvent;
	Button btEmail, btCall;
	AdView ads;

	protected void onCreate(Bundle savedInstanceState) {
		long id = 0;
		super.onCreate(savedInstanceState);
		dbh = new DatabaseHelper(getApplicationContext());

		Intent inte = this.getIntent();
		Bundle bnd = inte.getExtras();
		id = bnd.getLong("respid");
		String evtName = bnd.getString("eventname");
		if (id > 0) {
			res = dbh.getResponsable(id);
		}
		setContentView(R.layout.responsabledetails);
		tvRespName = (TextView) findViewById(R.id.tvRespName);
		tvRespPhone = (TextView) findViewById(R.id.tvResPhone);
		tvRespEmail = (TextView) findViewById(R.id.tvRespEmail);
		tvRespEvent = (TextView) findViewById(R.id.tvRespEvent);
		tvClub = (TextView) findViewById(R.id.tvClub);
		btEmail = (Button) findViewById(R.id.btEmail);
		btCall = (Button) findViewById(R.id.btCancelset);
		btEmail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					String mailstr = tvRespEmail.getText().toString();
					if (mailstr.trim().length() > 3
							&& mailstr.trim().contains("@")) {
						Intent inte = new Intent(Intent.ACTION_SEND);
						inte.setType("message/rfc822");
						inte.putExtra(Intent.EXTRA_EMAIL,
								new String[] { tvRespEmail.getText().toString()
										.trim() });
						inte.putExtra(Intent.EXTRA_SUBJECT,
								new String[] { getApplicationContext()
										.getString(R.string.emailsubject) });
						startActivity(Intent.createChooser(inte,
								getString(R.string.sendmail)));
					} else {
						Toast.makeText(getApplicationContext(),
								getString(R.string.noemailaddr),
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(),
							"Error trying to email:" + ex.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
		tvRespEvent.setText(evtName);
		btCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phonenum = tvRespPhone.getText().toString().trim();
				String url = "tel:" + phonenum;
				try {
					if (phonenum.length() >= 5) {
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						callIntent.setData(Uri.parse(url));
						startActivity(Intent.createChooser(callIntent,
								getString(R.string.callresponsable)));
					} else {
						Toast.makeText(getApplicationContext(),
								getString(R.string.nophonenum),
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(),
							"Error trying to call:" + ex.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
		tvRespName.setText(res.getName());
		tvRespPhone.setText(res.getPhone());
		tvRespEmail.setText(res.getEmail());
		tvClub.setText(res.getClub());
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

	public void onDestroy() {
		Intent inte = new Intent();
		inte.putExtra("event", "0");
		setResult(RESULT_CANCELED, inte);
		ads.destroy();
		super.onDestroy();
	}
	public void onPause() {
		ads.pause();
		super.onPause();
	}
	
	public void onResume() {
		ads.resume();
		super.onResume();
	}
}
