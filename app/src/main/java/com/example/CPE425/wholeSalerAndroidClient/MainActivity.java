package com.example.abdallah.palestine;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
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
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
         ,search.OnFragmentInteractionListener,
        products.OnFragmentInteractionListener,
        ProductInfo.OnFragmentInteractionListener,
        demand_list.OnFragmentInteractionListener{
    String userName2;
     String prodName;
    String pidInfo;
    private AbsListView mListView;
    private ListAdapter mAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle valueSent=getIntent().getExtras();
        if(valueSent!=null)
        {
            userName2=String.valueOf(valueSent.getString("userName"));

        }

        loadTheImage();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment myfragment=null;
        int id = item.getItemId();

        if (id == R.id.nav_p) {

            myfragment= new search();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, myfragment)
                    .commit();
        }  else if (id == R.id.nav_d) {

          /*  myfragment= new demand_list();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, myfragment)
                    .commit();*/
            Intent d=new Intent(this,demanActivity.class);
            startActivity(d);
           // showDataToTableFromSqlLite();

        } else if (id == R.id.nav_l) {
            Toast.makeText(this,"See you "+userName2,Toast.LENGTH_LONG).show();
            Intent logOut=new Intent(this,loginActivity.class);
            startActivity(logOut);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // to download a bitmap

    private InputStream OpenHttpConnection(String urlString)

            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    private Bitmap DownloadImage(String URL)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            Log.d("NetworkingActivity", e1.getLocalizedMessage());
        }
        return bitmap;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            return DownloadImage(urls[0]);
        }

        protected void onPostExecute(Bitmap result) {
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageBitmap(result);

            TextView welMsg=(TextView) findViewById(R.id.welcomeMsg);
            welMsg.setText("Welcome "+userName2+"!");
        }
    }
    public void loadTheImage()
    {
        new DownloadImageTask().execute("http://umut.tekguc.info/webservices/wholesaler/img/"+userName2+".jpg");

    }
    //this is killed me :)
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
    public void prodSearch(View v){
        EditText pSearch=(EditText) findViewById(R.id.searchE);
        prodName=pSearch.getText().toString();

/*
        Fragment myfrero=null;
        myfrero= new products();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, myfrero)
                .commit();*/
        Bundle bSearch=new Bundle();
        bSearch.putString("searchFor",prodName);
        Intent rA=new Intent(this,resultActivity.class);
        rA.putExtras(bSearch);
        startActivityForResult(rA,555);
    //   pppSearch();
    }
    public void onActivityResult(int reqcode,int resultcode,Intent data)
    {
        if(reqcode==555)
        {
            Fragment myfragment=null;
            if(resultcode==RESULT_OK) {
                pidInfo=data.getData().toString();
              //  Toast.makeText(this,pidInfo,Toast.LENGTH_LONG).show();
                myfragment= new ProductInfo();
                //
                Bundle args = new Bundle();
                args.putString("pidFragment", pidInfo);
                myfragment.setArguments(args);
                //
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, myfragment)
                        .commit();


            }
        }
    }

/*
    // to Consume JSON service
    private String[] dataPp = new String[20];

    ArrayList<String> arrList = new ArrayList<String>();
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
                    dataPp[i]=(jsonObject.getString("P"));
                    //TextView tv=(TextView) findViewById(R.id.textView);
                    //tv.setText(jsonObject.getString("midterm"));
                    arrList.add(jsonObject.getString("P"));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void pppSearch() {
        new ReadJSONFeedTask().execute("http://umut.tekguc.info/webservices/wholesaler/productSearch.php?P=" + prodName);
        String[] cars = {"nasser", "yasser"};
       setContentView(R.layout.fragment_products);
        final ListView listView = (ListView) findViewById(R.id.list);
        arrList.add("hhhhhhhhhhhhhhhhhhhhhh");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
    }*/
public void addToSqlLite(View v){


    DbAdapter db = new DbAdapter(this);
    db.open();
    //EditText ed1=(EditText) findViewById(R.id.editText);
    //int stid= Integer.parseInt(ed1.getText().toString());
      TextView pname=(TextView) findViewById(R.id.pname);
     String prodc=pname.getText().toString();
      EditText qua=(EditText) findViewById(R.id.quantity);
    int quant= Integer.parseInt(qua.getText().toString());
    long sid = db.insertContact(prodc,quant);
    db.close();
    Toast.makeText(this,"We have added Product Successfully (Cost you 13 points)",Toast.LENGTH_LONG).show();
}


}
