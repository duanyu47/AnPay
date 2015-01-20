package com.example.anpaydialog;

import pay.PL;
import pay.Payment;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((Button)this.findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Payment.doPayment(100, MainActivity.this, new PL(1,2,3,4));
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
