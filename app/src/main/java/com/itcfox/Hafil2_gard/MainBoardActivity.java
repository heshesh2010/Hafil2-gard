package com.itcfox.Hafil2_gard;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainBoardActivity extends Activity {
String android_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_board);


        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
		
		
		Button Gard = (Button) findViewById(R.id.button1);




        Gard.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent1 = new Intent(MainBoardActivity.this, MainGard.class);
			startActivity(intent1);


}

});
		
	}

	  @Override
	    public void onBackPressed() {
		 
		  AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainBoardActivity.this);

			// Setting Dialog Title
			alertDialog.setTitle("تحذير");

			// Setting Dialog Message
			alertDialog.setMessage("هل تود غلق البرنامج ");

			// On pressing Settings button
			alertDialog.setPositiveButton("نعم",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							android.os.Process.killProcess(android.os.Process.myPid());
						}
					});
			alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							
						}
			});
			alertDialog.show();
		  
		  
		  
		  
		  
		  
	       return;
	    }
	
	  
}
