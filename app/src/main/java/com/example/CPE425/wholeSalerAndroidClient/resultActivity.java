package com.example.abdallah.palestine;

import android.widget.AbsListView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class resultActivity extends AppCompatActivity {
   public String prodName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle valueSent=getIntent().getExtras();
           prodName=getIntent().getExtras().getString("searchFor");
      //  Toast.makeText(this," "+prodName,Toast.LENGTH_LONG).show();
        pppSearch();
    }
    // to Consume JSON service
    String[] prSearch;
    private String[] dataPp = new String[20];


    public String readJSONFeed(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(10000);
            c.setReadTimeout(10000);
            c.connect();
            int statusCode = c.getResponseCode();
            switch (statusCode) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line+"\n");
                    }
                    br.close();
                    return stringBuilder.toString();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

  //  ArrayList<String> arrList = new ArrayList<String>();
    public class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }
        //ArrayList<String> arrList = new ArrayList<String>();
        ArrayList<String> arrList = new ArrayList<String>();
      ArrayList<String> pid = new ArrayList<String>();
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                //---print out the content of the json feed---
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //dataPp[i]=(jsonObject.getString("P"));
                    //TextView tv=(TextView) findViewById(R.id.textView);
                    //tv.setText(jsonObject.getString("midterm"));
                    arrList.add(jsonObject.getString("P"));
                    pid.add(jsonObject.getString("Pid"));
                }
                ReadJSONFeedTask x=new ReadJSONFeedTask();
                setContentView(R.layout.activity_result);
                final ListView listView = (ListView) findViewById(R.id.list2);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(resultActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, arrList);

                listView.setAdapter(adapter);

                // ListView Item Click Listener

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition = position;

                        // ListView Clicked item value
                        String itemValue = (String) listView.getItemAtPosition(position);

                        // Show Alert
                       /* Toast.makeText(getApplicationContext(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();*/
                        int fi=0;
                        for(String fpid:pid){
                            if(fi==position)
                            {
                                Intent data=new Intent();
                                data.setData(Uri.parse(fpid));
                                setResult(RESULT_OK,data);
                                finish();

                            }
                            else{
                                fi++;
                            }
                        }


                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void pppSearch() {
        new ReadJSONFeedTask().execute("http://umut.tekguc.info/webservices/wholesaler/productSearch.php?P="+prodName);
    }
    public void killResult(View v){finish();};
}
