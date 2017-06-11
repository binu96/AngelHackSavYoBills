package com.codegears.angelhacksavyobills;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Map;

import static com.codegears.angelhacksavyobills.MyFireBase.database;

public class BillInfo extends AppCompatActivity {

    private String uniqueId, uniqueKey, company, time, date;
    private TextView id;
    private TextView tShirt;
    private TextView jeans;
    private TextView dress;
    private TextView amount;
    private TextView payment;
    private TextView balance;
    private TextView addressTxt;
    private TextView dateTxt;
    private TextView timeTxt;
    private TextView companyTxt;

    private ImageView imageView;
    private String word;

    String userName;

    private String addressData, amountData, balanceData, dressData, jeansData, paymentData, tShirtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_info);

        id = (TextView) findViewById(R.id.txtId);
        tShirt = (TextView) findViewById(R.id.txtTShirt);
        jeans = (TextView) findViewById(R.id.txtJeans);
        dress = (TextView) findViewById(R.id.txtDress);
        amount = (TextView) findViewById(R.id.txtAmount);
        payment = (TextView) findViewById(R.id.txtPayment);
        balance = (TextView) findViewById(R.id.txtBalance);
        addressTxt = (TextView) findViewById(R.id.txtAddress);
        dateTxt = (TextView) findViewById(R.id.txtDate);
        timeTxt = (TextView) findViewById(R.id.txtTime);
        companyTxt = (TextView) findViewById(R.id.txtCompany);

        userName = getIntent().getStringExtra("USER_NAME");
        uniqueId = getIntent().getStringExtra("uniqueId");
        uniqueKey = getIntent().getStringExtra("uniqueKey");
        company = getIntent().getStringExtra("company");
        time = getIntent().getStringExtra("time");
        date = getIntent().getStringExtra("date");

        DatabaseReference myRef = database.getReference("Bills").child(userName).child(uniqueId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                };
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                addressData = map.get("address");
                amountData = map.get("amount");
                balanceData = map.get("balance");
                dressData = map.get("dress");
                jeansData = map.get("jeans");
                paymentData = map.get("payment");
                tShirtData = map.get("tShirt");

                id.setText(uniqueKey);
                tShirt.setText(tShirtData);
                jeans.setText(jeansData);
                dress.setText(dressData);
                amount.setText(amountData);
                payment.setText(paymentData);
                balance.setText(balanceData);
                addressTxt.setText(addressData);
                companyTxt.setText(company);
                dateTxt.setText(date);
                timeTxt.setText(time);

                word = uniqueKey + "#" + tShirtData + "#" + dressData + "#" +
                        jeansData + "#" + amountData + "#" + paymentData + "#" +
                        balanceData + "#" + date + " " + time + "#" + company + "#" + addressData;


                qrCodeGen(word);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void qrCodeGen(String word) {

        imageView = (ImageView) findViewById(R.id.imgViewCode);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(word, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_logout, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_profile:
                deleteProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteProfile(){
        AlertDialog.Builder alert = new AlertDialog.Builder(BillInfo.this);
        alert.setTitle("Delete Profile");
        alert.setMessage("Are you sure you want to delete the profile?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference myRef = MyFireBase.database.getReference().child("Bills").child(userName).child(uniqueId);
                myRef.setValue(null);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BillInfo.this,ListOfBills.class);
                intent.putExtra("USER_NAME",userName);
                startActivity(intent);
                finish();

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }
}
