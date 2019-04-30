package com.itcfox.Hafil2_gard;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshe_000 on 9/4/2014.
 */
public class MainGard extends ListActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {




List<GardHelper> fleetNumberList = new ArrayList<GardHelper>();
List<GardHelper> gateNameList = new ArrayList<GardHelper>();
List<GardHelper> typeList = new ArrayList<GardHelper>();
List<GardHelper> dateList = new ArrayList<GardHelper>();

int type=0;
String gateName="";
//int FleetNumberView=0;
Cursor cursor;
TextView Date;
int GateID=1;
 EditText FleetNumber;
EditText note;
GardAdapter adapter3;
TextView TextView8;
TextView TextView10;
TextView TextView11;
 Spinner spinnertech;
int tempPostion=0;
List<String> list;
int tempGate=1;
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.maingard);

    FleetNumber  = (EditText) findViewById(R.id.EditText);
    Button SAVE  = (Button) findViewById(R.id.button2);
    Button sync = (Button) findViewById(R.id.button3);
    note  = (EditText) findViewById(R.id.editText);

    Typeface font = Typeface.createFromAsset(getAssets(),
            "HelveticaNeueW23-Reg.ttf");

    TextView TextView1 = (TextView)  findViewById(R.id.TextView1);
    TextView TextView3 = (TextView)  findViewById(R.id.TextView3);
    TextView TextView6 = (TextView)  findViewById(R.id.TextView6);
    TextView TextView2 = (TextView)  findViewById(R.id.TextView2);
    TextView TextView5 = (TextView)  findViewById(R.id.TextView5);
    TextView TextView7 = (TextView)  findViewById(R.id.TextView7);
    TextView TextView9 = (TextView)  findViewById(R.id.TextView9);

    TextView8 = (TextView)  findViewById(R.id.TextView8);
    TextView10 = (TextView)  findViewById(R.id.TextView10);
    TextView11 = (TextView)  findViewById(R.id.TextView11);

    SharedPreferences prefs = PreferenceManager
            .getDefaultSharedPreferences(this.getApplicationContext());

    TextView2.setText(prefs.getString("UserID", "0"));

    TextView1.setTypeface(font);
            TextView3.setTypeface(font);
    TextView6.setTypeface(font);


    TextView2.setTypeface(font);
    TextView5.setTypeface(font);
    TextView8.setTypeface(font);
    TextView7.setTypeface(font);
    TextView9.setTypeface(font);
    TextView10.setTypeface(font);
    TextView11.setTypeface(font);

    RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);



    RadioButton radio1 = (RadioButton) findViewById(R.id.radio1);
    RadioButton radio0 = (RadioButton) findViewById(R.id.radio0);





    radio1.setTypeface(font);
    radio0.setTypeface(font);
    MySQLiteHelper dbHandler = new MySQLiteHelper(MainGard.this);
    cursor = dbHandler.getAllGatesNames();
 list = new ArrayList<String>();

    if (cursor.getCount() >= 1) {
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

       spinnertech = (Spinner) findViewById(R.id.spinner1);



    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,list);


    spinnertech.setAdapter(adapter);
    spinnertech.setOnItemSelectedListener(this);




    TextView8.setText(String.valueOf(dbHandler.getNotSyncedRowCount(0,tempGate)));
            TextView10.setText(String.valueOf(dbHandler.getNotSyncedRowCount(1,tempGate)));

    cursor = dbHandler.getAllNotSyncedRows(1);

    if (cursor.getCount() >= 1) {
        if (cursor.moveToFirst()) {
            do {
                fleetNumberList.add(get(String.valueOf(cursor.getString(0))));
                gateNameList.add(get(String.valueOf(cursor.getInt(1))));
                typeList.add(get(String.valueOf(cursor
                        .getInt(2))));
                dateList.add(get(cursor.getString(3)));

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    GardAdapter adapter2 = new GardAdapter(
            MainGard.this,getApplicationContext(),fleetNumberList,
            gateNameList, typeList, dateList);

    setListAdapter(adapter2);
  /*  fleetNumberList.clear();
    gateNameList.clear();
    typeList.clear();
            dateList.clear();*/
    radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId) {
                case R.id.radio0:
                    MainGard.this.type =0;
                    break;

                case R.id.radio1:
                    MainGard.this.type =1;
                    break;

            }//

        }
    });


    SAVE.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(FleetNumber.getText().toString().isEmpty()) {

                Toast.makeText(getApplicationContext(), "ادخل رقم الشركة اولا", Toast.LENGTH_SHORT).show();

            } else {

                MySQLiteHelper dbHandler = new MySQLiteHelper(MainGard.this);

                dbHandler.InsertIntoGard(GateID, FleetNumber.getText().toString(), type, 0 , note.getText().toString());


                cursor = dbHandler.getAllNotSyncedRows(tempGate);

                fleetNumberList.clear();
                gateNameList.clear();
                typeList.clear();
                dateList.clear();

                if(cursor.getCount() >= 1) {
                    if(cursor.moveToFirst()) {
                        do {
                            fleetNumberList.add(get(String.valueOf(cursor.getString(0))));
                            gateNameList.add(get(String.valueOf(cursor.getInt(1))));
                            typeList.add(get(String.valueOf(cursor
                                    .getInt(2))));
                            dateList.add(get(cursor.getString(3)));

                        } while(cursor.moveToNext());
                    }
                    cursor.close();
                }

                 adapter3 = new GardAdapter(
                        MainGard.this, getApplicationContext(), fleetNumberList,
                        gateNameList, typeList, dateList);
                setListAdapter(adapter3);
                TextView8.setText(String.valueOf(dbHandler.getNotSyncedRowCount(0,tempGate)));
                TextView10.setText(String.valueOf(dbHandler.getNotSyncedRowCount(1,tempGate)));
                FleetNumber.setText("");
               // adapter3.clear();
            }
        }
    });








    sync.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ProgressDialog progressDialog = new ProgressDialog(
                    MainGard.this);
            progressDialog.setMessage("جاري مزامنة البيانات ");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            MySQLiteHelper dbHandler = new MySQLiteHelper(MainGard.this);


            cursor = dbHandler.getAllNotSyncedRowsForSync();
            if (cursor.getCount() >= 1) {
                if(cursor.moveToFirst()) {
                    do {
                        fleetNumberList.add(get(String.valueOf(cursor.getString(0))));
                        gateNameList.add(get(String.valueOf(cursor.getInt(1))));
                        typeList.add(get(String.valueOf(cursor
                                .getInt(2))));
                        dateList.add(get(cursor.getString(3)));
                        //cursor.getString(1)
                        SyncGard MyTask = new SyncGard(MainGard.this, progressDialog, MainGard.this.getApplicationContext(), cursor.getInt(1), cursor.getString(0), cursor.getInt(2), cursor.getString(3) , cursor.getString(4));
                        MyTask.execute();

                    } while(cursor.moveToNext());
                }
                cursor.close();
               /* fleetNumberList.clear();
                gateNameList.clear();
                typeList.clear();
                dateList.clear();*/
            }



/*
            cursor = dbHandler.getAllNotSyncedRows();
            if (cursor.getCount() >= 1) {
                if(cursor.moveToFirst()) {
                    do {
                        fleetNumberList.add(get(String.valueOf(cursor.getInt(0))));
                        gateNameList.add(get(cursor.getString(1)));
                        typeList.add(get(String.valueOf(cursor
                                .getInt(2))));
                        dateList.add(get(cursor.getString(3)));

                    } while(cursor.moveToNext());
                }
                cursor.close();
            }

            GardAdapter adapter = new GardAdapter(
                    MainGard.this,getApplicationContext(),fleetNumberList,
                    gateNameList, typeList, dateList);

            setListAdapter(adapter);
          /*  fleetNumberList.clear();
            gateNameList.clear();
            typeList.clear();
            dateList.clear();*/



            }








// first send all not synced data to web service via loop
            // second update all record to 1



    });



















}


private GardHelper get(String s) {

    return new GardHelper(s);

}



@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
// to delete records and update the girdView

     Date = (TextView) v.findViewById(R.id.textView4);

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainGard.this);

    // Setting Dialog Title
    alertDialog.setTitle("تنبيه!");

    // Setting Dialog Message
    alertDialog.setMessage("هل أنت متأكد انك ترغب بحذف هذا الجرد ؟");

    // On pressing Settings button
    alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

            MySQLiteHelper dbHandler = new MySQLiteHelper(MainGard.this);
            fleetNumberList.clear();
            gateNameList.clear();
            typeList.clear();
            dateList.clear();

          dbHandler.DeleteThisRecord(Date.getText().toString());
            cursor = dbHandler.getAllNotSyncedRows(tempGate);
            if (cursor.getCount() >= 1) {
                if(cursor.moveToFirst()) {
                    do {
                        fleetNumberList.add(get(String.valueOf(cursor.getString(0))));
                        gateNameList.add(get(cursor.getString(1)));
                        typeList.add(get(String.valueOf(cursor
                                .getInt(2))));
                        dateList.add(get(cursor.getString(3)));

                    } while(cursor.moveToNext());
                }
                cursor.close();
            }
            GardAdapter adapter4 = new GardAdapter(
                    MainGard.this,getApplicationContext(),fleetNumberList,
                    gateNameList, typeList, dateList);

            setListAdapter(adapter4);
            TextView8.setText(String.valueOf(dbHandler.getNotSyncedRowCount(0,tempGate)));
            TextView10.setText(String.valueOf(dbHandler.getNotSyncedRowCount(1,tempGate)));
         /*   fleetNumberList.clear();
            gateNameList.clear();
            typeList.clear();
            dateList.clear();*/
        }
    });

    alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {




        }
    });
    // Showing Alert Message
    alertDialog.show();

}

@Override
public void onClick(View v) {

}

@Override
public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    int position0 = parent.getSelectedItemPosition();

    if(position == 0) {
this.GateID=1;
        tempPostion=0;
        tempGate=1;

        fleetNumberList.clear();
        gateNameList.clear();
        typeList.clear();
        dateList.clear();
        note.setText("");
        MySQLiteHelper dbHandler = new MySQLiteHelper(MainGard.this);

        TextView8.setText(String.valueOf(dbHandler.getNotSyncedRowCount(0,tempGate)));
        TextView10.setText(String.valueOf(dbHandler.getNotSyncedRowCount(1,tempGate)));

        cursor = dbHandler.getAllNotSyncedRows(1);

        if (cursor.getCount() >= 1) {
            if (cursor.moveToFirst()) {
                do {
                    fleetNumberList.add(get(String.valueOf(cursor.getString(0))));
                    gateNameList.add(get(String.valueOf(cursor.getInt(1))));
                    typeList.add(get(String.valueOf(cursor
                            .getInt(2))));
                    dateList.add(get(cursor.getString(3)));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        GardAdapter adapter2 = new GardAdapter(
                MainGard.this,getApplicationContext(),fleetNumberList,
                gateNameList, typeList, dateList);

        setListAdapter(adapter2);



    }
    if(position == 1) {
        this.GateID=2;
        tempPostion=1;
        tempGate=2;
        fleetNumberList.clear();
        gateNameList.clear();
        typeList.clear();
        dateList.clear();
        note.setText("");
        MySQLiteHelper dbHandler = new MySQLiteHelper(MainGard.this);

        TextView8.setText(String.valueOf(dbHandler.getNotSyncedRowCount(0,tempGate)));
        TextView10.setText(String.valueOf(dbHandler.getNotSyncedRowCount(1,tempGate)));

        cursor = dbHandler.getAllNotSyncedRows(2);

        if (cursor.getCount() >= 1) {
            if (cursor.moveToFirst()) {
                do {
                    fleetNumberList.add(get(String.valueOf(cursor.getString(0))));
                    gateNameList.add(get(String.valueOf(cursor.getInt(1))));
                    typeList.add(get(String.valueOf(cursor
                            .getInt(2))));
                    dateList.add(get(cursor.getString(3)));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        GardAdapter adapter2 = new GardAdapter(
                MainGard.this,getApplicationContext(),fleetNumberList,
                gateNameList, typeList, dateList);

        setListAdapter(adapter2);


    }
    if(position == 2) {
        this.GateID=3;
        tempPostion=2;
        tempGate=3;
        note.setText("");
        fleetNumberList.clear();
        gateNameList.clear();
        typeList.clear();
        dateList.clear();
        MySQLiteHelper dbHandler = new MySQLiteHelper(MainGard.this);

        TextView8.setText(String.valueOf(dbHandler.getNotSyncedRowCount(0,tempGate)));
        TextView10.setText(String.valueOf(dbHandler.getNotSyncedRowCount(1,tempGate)));

        cursor = dbHandler.getAllNotSyncedRows(3);

        if (cursor.getCount() >= 1) {
            if (cursor.moveToFirst()) {
                do {
                    fleetNumberList.add(get(String.valueOf(cursor.getString(0))));
                    gateNameList.add(get(String.valueOf(cursor.getInt(1))));
                    typeList.add(get(String.valueOf(cursor
                            .getInt(2))));
                    dateList.add(get(cursor.getString(3)));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        GardAdapter adapter2 = new GardAdapter(
                MainGard.this,getApplicationContext(),fleetNumberList,
                gateNameList, typeList, dateList);

        setListAdapter(adapter2);

    }



    //-----------

    if(position == 3) {
        this.GateID=4;
        tempPostion=3;
    }

    if(position == 4) {
        this.GateID=5;
        tempPostion=4;
    }


    if(position == 5) {
        this.GateID=6;
        tempPostion=5;
    }

    if(position == 6) {
        this.GateID=7;
        tempPostion=6;
    }

    if(position == 7) {
        this.GateID=8;
        tempPostion=7;
    }

    if(position == 8) {
        this.GateID=9;
        tempPostion=8;
    }

    if(position == 9) {
        this.GateID=10;
        tempPostion=9;
    }

    if(position == 10) {
        this.GateID=11;
        tempPostion=10;
    }

    if(position == 11) {
        this.GateID=12;
        tempPostion=11;
    }

    if(position == 12) {
        this.GateID=11;
        tempPostion=12;
    }

    if(position == 13) {
        this.GateID=12;
        tempPostion=13;
    }

    if(position == 14) {
        this.GateID=13;
        tempPostion=14;
    }

    if(position == 15) {
        this.GateID=14;
        tempPostion=15;
    }

    if(position == 16) {
        this.GateID=15;
        tempPostion=16;
    }

    if(position == 17) {
        this.GateID=16;
        tempPostion=17;
    }

    if(position == 18) {
        this.GateID=17;
        tempPostion=18;
    }

    if(position == 19) {
        this.GateID=18;
        tempPostion=19;
    }

    if(position == 20) {
        this.GateID=19;
        tempPostion=20;
    }


}

@Override
public void onNothingSelected(AdapterView<?> parent) {

}





@Override
public void onPause() {
    super.onPause();

    SharedPreferences prefs = PreferenceManager
            .getDefaultSharedPreferences(MainGard.this);

    prefs.edit().putInt("tempPostion",tempPostion).apply();
}
@Override
protected void onResume() {
    super.onResume();
    SharedPreferences prefs = PreferenceManager
            .getDefaultSharedPreferences(MainGard.this);

    tempPostion= prefs.getInt("tempPostion", 0);

    spinnertech.setSelection(tempPostion);
    tempGate=tempPostion+1;


}

}

