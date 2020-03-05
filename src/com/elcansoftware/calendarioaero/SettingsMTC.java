package com.elcansoftware.calendarioaero;

import com.elcansoftware.calendarioaero.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsMTC extends Activity {
	String sets;
	Button btnSave;
	Button btnCancel;
	Button btnScan;
	TextView txt;
	// Boolean bn;
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.settings);
		btnSave = (Button) findViewById(R.id.buttonSave);
		btnCancel = (Button) findViewById(R.id.btCancelset);
		btnScan = (Button) findViewById(R.id.btScan);
		txt = (TextView) findViewById(R.id.etServerURL);
		try {
			sharedPreferences = getSharedPreferences("ElcanMCT", MODE_PRIVATE);
			sets = sharedPreferences.getString("server", "");
			txt.setText(sets);
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), "Error:" + ex.getMessage(),
					Toast.LENGTH_LONG).show();
		}
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sets = txt.getText().toString();
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("server", sets);
				editor.commit();
				finish();
			}
		});
		btnScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IntentIntegrator integrator = new IntentIntegrator(
						SettingsMTC.this);
				integrator.initiateScan();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// bn = false;
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*
		 * Bundle bnd = new Bundle(); bnd.putString("server", this.sets); Intent
		 * result = new Intent();
		 * 
		 * if (bn) { result.putExtras(bnd); setResult(Activity.RESULT_OK,
		 * result); } else { setResult(Activity.RESULT_CANCELED, result); }
		 */
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
	    if (result != null) {
	      String contents = result.getContents();
	      if (contents != null) {
	    	  txt.setText(contents);
	      }
	    }
	}
}
