package com.example.abdallah.palestine;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class demanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deman);
        showDataToTableFromSqlLite();
    }
    int[] nums = new int[100];
    int flag=0;
    public void showDataToTableFromSqlLite() {
        int i = 0;
        DbAdapter db = new DbAdapter(this);
        TableLayout table = (TableLayout) findViewById(R.id.tbl);
        table.removeAllViews();
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst()) {
            do {
                TableRow row = new TableRow(this);
                TextView tv = new TextView(this);
                tv.setText(String.valueOf(i + 1));
                tv.setGravity(Gravity.LEFT);
                row.addView(tv);

                tv = new TextView(this);
                tv.setText(c.getString(0));
                tv.setGravity(Gravity.CENTER);
                row.addView(tv);

                tv = new TextView(this);
                tv.setText(c.getString(1));
                tv.setGravity(Gravity.RIGHT);
                tv.setTextColor(Color.BLUE);
                row.addView(tv);
                table.addView(row);
                nums[i] = Integer.parseInt(c.getString(1));
                i++;
            } while (c.moveToNext());
        }
        if(flag==1) {
            db.deleteContact(dataforremove());
            Log.w("remove","isItok");
            Toast.makeText(this, "We have Removed the required Item!", Toast.LENGTH_LONG).show();
            Intent xa=new Intent(this,demanActivity.class);
            startActivity(xa);
        }
        db.close();

    }
     int xx;
    public void delet(View v){
         EditText edIdToRemove=(EditText) findViewById(R.id.ied);
        String stringEdIdToRemove=edIdToRemove.getText().toString();
        int intEdIdToRemove=Integer.parseInt(stringEdIdToRemove.toString());
          this.xx=nums[intEdIdToRemove-1];
        Log.w("remove",String.valueOf(xx));
        this.flag=1;
        showDataToTableFromSqlLite();

    }
    public int dataforremove(){flag=0;return xx;}

    public void killDemand(View v){
      finish();
    }

}
