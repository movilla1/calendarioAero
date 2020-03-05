package com.elcansoftware.calendarioaero;

import com.elcansoftware.calendarioaero.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class AboutUs extends Activity {
	Button btn;
	AdView ads;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.aboutus);
		btn=(Button) findViewById(R.id.btnAboutClose);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		try {
			ads = new AdView(this);
			ads.setAdUnitId("ca-app-pub-4265440469365469/4470102109");
			ads.setAdSize(AdSize.BANNER);
			LinearLayout layout = (LinearLayout)findViewById(R.id.LinearLayout3a);
			layout.addView(ads);
			AdRequest adRequest = new AdRequest.Builder().build();

			// Cargar adView con la solicitud de anuncio.
			ads.loadAd(adRequest);
		} catch (Exception ex) {
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


}
