package com.itcfox.Hafil2_gard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.client.ClientProtocolException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * Created by heshe_000 on 9/6/2014.
 */
public class SyncGard  extends AsyncTask<Void, Void, String> {

private final String NAMESPACE = "http://tempuri.org/";
private final String URL = "http://212.12.183.204/HajjWebServices/HajjGardWS_new.asmx";
private final String SOAP_ACTION = "http://tempuri.org/GardWS_New";
private final String METHOD_NAME = "GardWS_New";

int GateId;
int Direction;

String ReadingTime;
String UserLoginName;
String FleetNumber;
String note ;
Activity mActivity;
Context context;
ProgressDialog progressDialog;


public SyncGard(Activity MainGard, ProgressDialog progressDialog ,  Context applicationContext, int GateId , String FleetNumber , int Direction , String ReadingTime, String note){

    this.GateId=GateId;
    this.FleetNumber=FleetNumber;
    this.Direction=Direction;
    this.ReadingTime=ReadingTime;
    this.context=applicationContext;
    this.mActivity=MainGard;
    this.progressDialog=progressDialog;
    this.note=note;
}

@Override
protected String doInBackground(Void... voids) {

    SharedPreferences prefs = PreferenceManager
            .getDefaultSharedPreferences(this.mActivity);

    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

    PropertyInfo pro =new PropertyInfo();
    pro.setName("GateId");
    pro.setValue(this.GateId);
    pro.setType(Integer.class);

    PropertyInfo pro2 =new PropertyInfo();
    pro2.setName("FleetNumber");
    pro2.setValue(this.FleetNumber);
    pro2.setType(String.class);

    PropertyInfo pro3 =new PropertyInfo();
    pro3.setName("Direction");
    pro3.setValue(this.Direction);
    pro3.setType(Integer.class);

    PropertyInfo pro4 =new PropertyInfo();
    pro4.setName("ReadingTime");
    pro4.setValue(this.ReadingTime);
    pro4.setType(String.class);

    PropertyInfo pro5 =new PropertyInfo();
    pro5.setName("UserLoginName");
    pro5.setValue(prefs.getString("UserID", "0"));
    pro5.setType(String.class);

    PropertyInfo pro6 =new PropertyInfo();
    pro6.setName("Comment");
    pro6.setValue(note);
    pro6.setType(String.class);

    request.addProperty(pro);
    request.addProperty(pro2);
    request.addProperty(pro3);
    request.addProperty(pro4);
    request.addProperty(pro5);
    request.addProperty(pro6);


    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    envelope.dotNet = true;
    envelope.setOutputSoapObject(request);
    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

    try {

        androidHttpTransport.call(SOAP_ACTION, envelope);
        SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
        Log.i("myApp", response.toString());




        return response.toString();
    }catch (UnknownHostException e) {//POST & GET section
        e.printStackTrace();
        return"0";
    }
    catch (ConnectException e) {//POST & GET section
        e.printStackTrace();
        return"0";
    }
    catch (NullPointerException e) {//POST & GET section
        e.printStackTrace();
        return"0";
    }
    catch (ClientProtocolException e) {//POST section
        e.printStackTrace();
        return"0";
    }
    catch (IOException e) { // POST & GET section
        e.printStackTrace();
        return"0";
    }

    catch (Exception e) {
        e.printStackTrace();
        return"0";
    }


}


@Override
protected void onPostExecute(String result) {
    MySQLiteHelper dbHandler = new MySQLiteHelper(this.mActivity);
    if(result.equals("0")) {

        Toast.makeText(context.getApplicationContext(), "خطأ لم تتم مزامنة البيانات", Toast.LENGTH_SHORT).show();

    }
    else {
        try {
            if((this.progressDialog != null) && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
        } catch(final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch(final Exception e) {
            // Handle or log or ignore
        } finally {

            dbHandler.updateAllNotSyncedRows(this.ReadingTime);
            this.progressDialog = null;
            Intent ii = new Intent(this.mActivity, MainBoardActivity.class);
            Toast.makeText(context.getApplicationContext(), "تم مزامنة البيانات ", Toast.LENGTH_SHORT).show();
            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ii);
        }
    }
}
}