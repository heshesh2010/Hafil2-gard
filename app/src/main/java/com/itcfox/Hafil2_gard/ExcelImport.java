package com.itcfox.Hafil2_gard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;


public class ExcelImport extends AsyncTask<Void, Void, String>{
	Activity mActivity;
	Context context;
	ProgressDialog progressDialog;
	MySQLiteHelper dbHandler;
	String line = null;
	URL url;


	String str;
	String user;
	String pass;
	String test;
	public ExcelImport(Activity activity, ProgressDialog progressDialog,
                       Context context, String user, String pass) {
		super();
		this.progressDialog = progressDialog;
		this.mActivity = activity;
		this.context = context;
		this.user=user;
		this.pass=pass;

	}
	
	@Override
	protected String doInBackground(Void... voids) {
		
	

 
		boolean flag_is_header = false;
		  dbHandler = new MySQLiteHelper(this.context);






		//////////////////////////////////*

		try {
			url = new URL(
					"http://212.12.183.204/HajjWebServices/fleetmaster.aspx");




			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), "UTF-8"));
			dbHandler = new MySQLiteHelper(context);
			dbHandler.open();
			dbHandler.begin();
			line = in.readLine();

                String[] insertValues =
                        line.split("<br>");
            for(int i = 0 ; i < insertValues.length ; i++){

                String[] insertValuesTemp = insertValues[i].split(",");
                dbHandler.insertFleetMasterFile(insertValuesTemp[0],
                        insertValuesTemp[1], insertValuesTemp[2], insertValuesTemp[3],
                        insertValuesTemp[4], insertValuesTemp[5],insertValuesTemp[6]);
            }


                   // dbHandler.insertFleetNumberForMasterFile(line2);
					//Log.e("no. of rows inserted", "" + row);


			 dbHandler.setsucss();
			 dbHandler.end();
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			flag_is_header = false;
			dbHandler.close();
		}
		///////////////////////////////////////////*

		try {
			
			url = new URL("http://212.12.183.204/HajjWebServices/gaets.aspx");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			dbHandler = new MySQLiteHelper(context);
			dbHandler.open();
			dbHandler.begin();
			while ((line = in.readLine()) != null) {

				String[] insertValues = line.split("<br>");
                for(int i = 0 ; i < insertValues.length ; i++){

                    String[] insertValuesTemp = insertValues[i].split(",");
                    dbHandler.insertGateFile(insertValuesTemp[0],insertValuesTemp[1]);
                }

			}
			 dbHandler.setsucss();
			 dbHandler.end();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
finally {
	flag_is_header = false;
			dbHandler.close();
		}
		
		//===============================================
		try {
			url = new URL("http://212.12.183.204/HajjWebServices/UsersData.aspx");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			dbHandler = new MySQLiteHelper(context);
			dbHandler.open();
			dbHandler.begin();
			line = in.readLine();

				String[] insertValues = line.split("<br>");


            for(int i = 0 ; i < insertValues.length ; i++){

                String[] insertValuesTemp = insertValues[i].split(",");
                dbHandler.insertUserFile(insertValuesTemp[0],
                        insertValuesTemp[1],insertValuesTemp[2],insertValuesTemp[3]);
            }

					//Log.e("no. of rows inserted", ""+row);


			 dbHandler.setsucss();
			 dbHandler.end();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			flag_is_header = false;
			dbHandler.close();
		}
		
		//======================================================


		return line;
	}
	@Override
	protected void onPostExecute(String result) {
		progressDialog.dismiss();

        Toast.makeText(this.mActivity, test , Toast.LENGTH_SHORT).show();
		Toast.makeText(this.context, "تم تحميل البيانات بنجاح", Toast.LENGTH_LONG).show();
		SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this.mActivity);
		 try {
		if(dbHandler.CheckLoginForID(this.user, this.pass)>0){

			String user2 = dbHandler.CheckLogin(this.user, this.pass);
			
			prefs.edit().putString("UserID", user2).commit();
			Toast.makeText(this.context, String.valueOf(user2), Toast.LENGTH_LONG).show();
			 Intent intent = new Intent(this.context, MainBoardActivity.class);
	         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	         this.context.getApplicationContext().startActivity(intent);
		} 
		else{
			Toast.makeText(this.context, "برجاء التاكد من اسم المستخدم وكلمة المرور", Toast.LENGTH_LONG).show();
		}
		 
		 
		 
		 }catch (ParseException e) {
			Toast.makeText(this.context, "خطأ برجاء الاتصال بالدعم الفني ", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	
}
}