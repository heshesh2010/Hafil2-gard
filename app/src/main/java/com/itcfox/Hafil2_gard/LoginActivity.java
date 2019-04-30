package com.itcfox.Hafil2_gard;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends Activity {
	ProgressDialog progressDialog;
	public static Context contextOfApplication;
	SharedPreferences prefs = null;
	 AlertDialog.Builder alertDialog;
	// Check if internet is enabled 
	boolean isOnline() {
		ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

			return false;
		}
		return true;
	}
		
	// For shared prfences .
	String PREFS = "MyPrefs";
	SharedPreferences mPrefs;

	
	private void saveLoginDetails() {
		final EditText LoginId = (EditText) findViewById(R.id.editText1);
		final EditText LoginPass = (EditText) findViewById(R.id.editText2);

		String login = LoginId.getText().toString();
		String pass = LoginPass.getText().toString();

		Editor e = mPrefs.edit();
		e.putBoolean("rememberMe", true);
		e.putString("login", login);
		e.putString("password", pass);

		e.commit();

	}
	
	private void removeLoginDetails() {
		Editor e = mPrefs.edit();
		e.putBoolean("rememberMe", false);
		e.remove("login");
		e.remove("password");
		e.commit();
	}

	public static Context getContextOfApplication() {
		return contextOfApplication;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		 prefs = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);
		
		
		contextOfApplication = getApplicationContext();

		 alertDialog = new AlertDialog.Builder(this);
	
		//();
		
		alertDialog.setTitle("الانترنت مغلق");
		alertDialog.setMessage("يجب تشغيل الانترنت لاول استخدام لجلب البيانات");
		alertDialog.setPositiveButton("الاعدادات",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
						
					}
				});

		Button loginBtn = (Button) findViewById(R.id.button1);
		final CheckBox RembmberMe = (CheckBox) findViewById(R.id.radioButton1);
		final EditText LoginId = (EditText) findViewById(R.id.editText1);
		final EditText LoginPass = (EditText) findViewById(R.id.editText2);

		
		
		
		
		
		
		
		// When LoginButtin Clicked 
				loginBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						final EditText LoginId = (EditText) findViewById(R.id.editText1);
						final EditText LoginPass = (EditText) findViewById(R.id.editText2);

						String login = LoginId.getText().toString();
						String pass = LoginPass.getText().toString();


						prefs.edit().putString("login1", login).apply();
						prefs.edit().putString("password1", pass).apply();

                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_WEEK);
                        MySQLiteHelper dbHandler = new MySQLiteHelper(LoginActivity.this);
                        Date cDate = new Date();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                      /*  int numberDays = Days.daysBetween( new LocalDate(dbHandler.getLastModifitedDate()),  new LocalDate(date)).getDays();
                        if(numberDays>=7){
                            prefs.edit().putBoolean("firstRun", true).commit();
                            dbHandler.deletall();
                            dbHandler.InsertLastModifidedDate(date);
                        }*/
						if(prefs.getBoolean("firstRun", true)&& isOnline()){
							prefs.edit().putBoolean("firstRun", false).commit();
							 progressDialog = new ProgressDialog(
								LoginActivity.this);
						progressDialog.setMessage("جاري جلب البيانات لبضع دقائق .. برجاء الانتظار");
						progressDialog.show();
						progressDialog.setCancelable(false);
						progressDialog.setCanceledOnTouchOutside(false);


						
						ExcelImport MyTask = new ExcelImport(
								LoginActivity.this, progressDialog,
								getApplicationContext(),login,pass );
						MyTask.execute();
						
						
						}
						

						else if (prefs.getBoolean("firstRun", true)&&isOnline()==false){
							alertDialog.show();
							
							
						}
						
				        else if (LoginId.getText().toString().isEmpty()||LoginPass.getText().toString().isEmpty()){
				        	
				        	
				        	Toast.makeText(getApplicationContext(), "قم بوضع اسم المستخدم وكلمة المرور اولا ", Toast.LENGTH_SHORT).show();
				        	
				        	
				        }
				        else{
						 try {

							if(dbHandler.CheckLoginForID(login, pass)>0){
								String user2 = dbHandler.CheckLogin(login, pass);
								progressDialog.dismiss();
								Toast.makeText(LoginActivity.this, String.valueOf(user2), Toast.LENGTH_LONG).show();
								prefs.edit().putString("UserID", user2).apply();
							Intent intent1 = new Intent(LoginActivity.this, MainBoardActivity.class);
							startActivity(intent1);	
							}
							else{
								Toast.makeText(getApplicationContext(), "تاكد من اسم المستخدم وكلمة المرور", Toast.LENGTH_SHORT).show();
							}
						} catch (ParseException e) {
							Toast.makeText(getApplicationContext(), "قم بالتاكد من الاسم المستخدم وكلمة المرور", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
						//
					}
				});

		
		
		
		
		
		
		
		
		
		
		// using coustom font
		Typeface font = Typeface.createFromAsset(getAssets(), "HelveticaNeueW23-Reg.ttf");
		LoginId.setTypeface(font);
		LoginPass.setTypeface(font);
		
		mPrefs = getSharedPreferences(PREFS, 0);
		boolean rememberMe = mPrefs.getBoolean("rememberMe", false);

		final String login1 = LoginId.getText().toString();
		final String pass1 = LoginPass.getText().toString();

		prefs.edit().putString("login1", login1).apply();
		prefs.edit().putString("password1", pass1).apply();


		if (rememberMe) {
			// get previously stored login details
			String login = mPrefs.getString("login", null);
			String pass = mPrefs.getString("password", null);

			if (login != null && pass != null) {
				// fill input boxes with stored login and pass

				LoginId.setText(login);
				LoginPass.setText(pass);
				RembmberMe.setChecked(true);
			}
		}

	
		
		// This for Remember PassWord RadioButton !
		RembmberMe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (RembmberMe.isChecked()) {
					saveLoginDetails();
				} else {
					removeLoginDetails();
				}
			}
		});

		
		
		
		

		
	} // End of On create

	  @Override
	    public void onBackPressed() {

	    }
	
	
	  @Override
	    protected void onResume() {
	        super.onResume();
         //backup();
	        Button loginBtn = (Button) findViewById(R.id.button1);
    		final EditText LoginId = (EditText) findViewById(R.id.editText1);
    		final EditText LoginPass = (EditText) findViewById(R.id.editText2);
			loginBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 prefs = PreferenceManager
								.getDefaultSharedPreferences(LoginActivity.this);

                    MySQLiteHelper dbHandler = new MySQLiteHelper(LoginActivity.this);


                    /*Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_WEEK);


                    Date cDate = new Date();
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                    int numberDays = Days.daysBetween( new LocalDate(dbHandler.getLastModifitedDate()),  new LocalDate(date)).getDays();
                    if(numberDays>=7){
                        prefs.edit().putBoolean("firstRun", true).commit();
                        dbHandler.deletall();
                        dbHandler.InsertLastModifidedDate(date);
                    }*/

	        if (prefs.getBoolean("firstRun", true)&&LoginActivity.this.isOnline()) {
	        	prefs.edit().putBoolean("firstRun", false).commit();
	        	ProgressDialog progressDialog;
	        	progressDialog = new ProgressDialog(
						LoginActivity.this);
				progressDialog.setMessage("جاري جلب البيانات لاول استخدام .. برجاء الانتظار لدقائق ");
				progressDialog.show();
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
	    		final EditText LoginId = (EditText) findViewById(R.id.editText1);
	    		final EditText LoginPass = (EditText) findViewById(R.id.editText2);


				
				ExcelImport MyTask = new ExcelImport(
						LoginActivity.this, progressDialog,
						getApplicationContext(), LoginId.getText().toString(), LoginPass.getText().toString());
				MyTask.execute();
					
				//Toast.makeText(LoginActivity.this, "firstTime", Toast.LENGTH_SHORT).show();
				
	        }
	        
	        else if(prefs.getBoolean("firstRun", true)&&LoginActivity.this.isOnline()){
	        	
	        	final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
	        	
	    		//backup();
	    		
	    		alertDialog.setTitle("الانترت مغلق!");
	    		alertDialog.setMessage("برجاء تشيل الانترنت لاول استخدام");
	    		alertDialog.setPositiveButton("الاعدادات",
	    				new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog, int which) {
	    						
	    						startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
	    						
	    					}
	    				});
	    		
	    		alertDialog.show();
	        }
	        
	        
	        else if (LoginId.getText().toString().isEmpty()||LoginPass.getText().toString().isEmpty()){
	        	
	        	
	        	Toast.makeText(getApplicationContext(), "قم بادخال اسم المستخدم وكلمة المرور اولا", Toast.LENGTH_SHORT).show();
	        	
	        	
	        }

	        else{
//progressDialog.dismiss();
				try {
					if(dbHandler.CheckLoginForID(LoginId.getText().toString(), LoginPass.getText().toString())>0){
						String user2 = dbHandler.CheckLogin(LoginId.getText().toString(), LoginPass.getText().toString());
						Toast.makeText(LoginActivity.this, String.valueOf(user2), Toast.LENGTH_LONG).show();
						prefs = PreferenceManager
								.getDefaultSharedPreferences(LoginActivity.this);
						prefs.edit().putString("UserID", user2).commit();
					Intent intent1 = new Intent(LoginActivity.this, MainBoardActivity.class);
					startActivity(intent1);					
      }
					else {Toast.makeText(getApplicationContext(), "تاكد من المعلومات المدخله", Toast.LENGTH_SHORT).show();}
				} catch (ParseException e) {
					 Toast.makeText(getApplicationContext(), "تاكد من المعلومات المدخله", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				
	        }
	        
	        
				}
			});
	    }
	  
	  
	  
	
	 public void backup() {
	        try {
	            File sdcard = Environment.getExternalStorageDirectory();


	            File outputFile = new File(sdcard,
	                    "HafilSTC");



                Toast.makeText(getApplicationContext(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() , Toast.LENGTH_SHORT).show();


                if (!outputFile.exists())
	                 outputFile.createNewFile();


	            File data = Environment.getDataDirectory();
	            File inputFile = new File(data,
	                    "data/"+LoginActivity.this.getPackageName()+"/databases/"+"HafilSTC");
	            InputStream input = new FileInputStream(inputFile);

                Toast.makeText(getApplicationContext(), inputFile.toString() , Toast.LENGTH_SHORT).show();

                OutputStream output = new FileOutputStream(outputFile);
	            byte[] buffer = new byte[1024];
	            int length;
	            while ((length = input.read(buffer)) > 0) {
	                output.write(buffer, 0, length);
	            }
	            output.flush();
	            output.close();
	            input.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new Error(e.toString());
	        }
	    }
	 
	 
	 
} // End of class				   

