package com.codegears.angelhacksavyobills;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRToWord extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button scanBtn;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrto_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = getIntent().getStringExtra("USER_NAME");

        scanBtn = (Button) findViewById(R.id.scanBtn);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(QRToWord.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                //pass the scanned result to next activity
                String code = result.getContents();
                String[] words = code.split("#");


                Intent intent = new Intent(QRToWord.this,SaveToDatabase.class);
                intent.putExtra("id",words[0]);
                intent.putExtra("tShirt",words[1]);
                intent.putExtra("dress",words[2]);
                intent.putExtra("jeans",words[3]);
                intent.putExtra("total",words[4]);
                intent.putExtra("amount",words[5]);
                intent.putExtra("balance",words[6]);
                intent.putExtra("dateAndTime",formatter(words[7]));
                intent.putExtra("companyName",words[8]);
                intent.putExtra("address",words[9]);
                intent.putExtra("USER_NAME",userName);
                startActivity(intent);

//                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public String formatter(String dateTime){
        String[] items = dateTime.split(" ");

        String[] dates = items[0].split("/");
        String date1 = dates[0] + "," + dates[1] + "," + dates[2];


        String[] times = items[1].split(":");
        String time = times[0] + "," + times[1];

        String dateAndTime = time + "," + date1;

        return dateAndTime;
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
        getMenuInflater().inflate(R.menu.qrto_word, menu);
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
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_viewBill) {
            Intent intent = new Intent(QRToWord.this, ListOfBills.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        } else if (id == R.id.nav_warranty) {

        } else if (id == R.id.nav_viewWarranty) {

        } else if (id == R.id.nav_Logout) {
            Intent intent = new Intent(QRToWord.this,LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplication(),"Successfully Logout ! ",Toast.LENGTH_LONG).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
