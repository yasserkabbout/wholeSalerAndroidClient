package com.example.abdallah.palestine;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.json.JSONArray;
import org.json.JSONObject;


public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    String userName;
    String password;
    int logFlag = 544;


    // to Consume JSON service
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
                        stringBuilder.append(line + "\n");
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

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                //---print out the content of the json feed---
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                   /* TextView tv=(TextView) findViewById(R.id.textView);
                    tv.setText(jsonObject.getString("correct"));*/
                    String returnedResult = jsonObject.getString("correct");
                    Log.w("what", returnedResult);
                    if (returnedResult.equals("YES")) {
                        logFlag = 1;
                        Log.w("what", "WE ARE HERE");
                    } else
                        logFlag = 0;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void logToApp(View vs) {
        EditText e1 = (EditText) findViewById(R.id.userNameE);
        userName = e1.getText().toString();
        EditText e2 = (EditText) findViewById(R.id.passwordE);
        password = e2.getText().toString();
        //displayTheMidterm();
        new ReadJSONFeedTask().execute("http://umut.tekguc.info/webservices/wholesaler/checkLogin.php?UN=" + userName + "&Pass=" + password);
        if (logFlag == 1) {
            Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
            Bundle valueToSend = new Bundle();
            valueToSend.putString("userName", userName);
            Intent nextActivity = new Intent(this, MainActivity.class);
            nextActivity.putExtras(valueToSend);
            startActivity(nextActivity);

        }
        if (logFlag == 0) {
            Toast.makeText(this, "Wrong User Name and/or Password", Toast.LENGTH_LONG).show();
        }
    }

}
