package com.codegears.angelhacksavyobills;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class SaveToDatabase extends AppCompatActivity {

    String userName;


    private TextView idData;
    private TextView tShirtData;
    private TextView jeansData;
    private TextView dressData;
    private TextView amountData;
    private TextView paymentData;
    private TextView balanceData;
    private TextView addressTxtData;
    private TextView dateTxtData;
    private TextView timeTxtData;
    private TextView companyTxtData;
    private ImageView imageViewData;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to_database);

        idData = (TextView) findViewById(R.id.txtId);
        tShirtData = (TextView) findViewById(R.id.txtTShirt);
        jeansData = (TextView) findViewById(R.id.txtJeans);
        dressData = (TextView) findViewById(R.id.txtDress);
        amountData = (TextView) findViewById(R.id.txtAmount);
        paymentData = (TextView) findViewById(R.id.txtPayment);
        balanceData = (TextView) findViewById(R.id.txtBalance);
        addressTxtData = (TextView) findViewById(R.id.txtAddress);
        dateTxtData = (TextView) findViewById(R.id.txtDate);
        timeTxtData = (TextView) findViewById(R.id.txtTime);
        companyTxtData = (TextView) findViewById(R.id.txtCompany);


        userName = getIntent().getStringExtra("USER_NAME");
        String id = getIntent().getStringExtra("id");
        String tShirt = getIntent().getStringExtra("tShirt");
        String dress = getIntent().getStringExtra("dress");
        String jeans = getIntent().getStringExtra("jeans");
        String total = getIntent().getStringExtra("total");
        String amount = getIntent().getStringExtra("amount");
        String balance = getIntent().getStringExtra("balance");
        String dateAndTime = getIntent().getStringExtra("dateAndTime");
        String companyName = getIntent().getStringExtra("companyName");
        String address = getIntent().getStringExtra("address");
        String uniqueKey = formatter(companyName, dateAndTime, id);

        Log.i("Hello", uniqueKey);

        String test2 = id + " " + tShirt + " " + dress + " " + jeans + " " + total + " " + amount + " " + balance;

        String[] items = formatter(dateAndTime.split(","));


        idData.setText(id);
        tShirtData.setText(tShirt);
        jeansData.setText(jeans);
        dressData.setText(dress);
        amountData.setText(total);
        paymentData.setText(amount);
        balanceData.setText(balance);
        addressTxtData.setText(address);
        companyTxtData.setText(companyName);
        dateTxtData.setText(items[1]);
        timeTxtData.setText(items[0]);

        word = uniqueKey + "#" + tShirt + "#" + dress + "#" +
                jeans + "#" + total + "#" + amount + "#" +
                balance + "#" + items[1] + " " + items[0] + "#" + companyName + "#" + address;

        qrCodeGen(word);


        DatabaseReference myRef = MyFireBase.database.getReference("Bills").child(userName).child(uniqueKey);
        myRef.child("tShirt").setValue(tShirt);
        myRef.child("dress").setValue(dress);
        myRef.child("jeans").setValue(jeans);
        myRef.child("amount").setValue(total);
        myRef.child("payment").setValue(amount);
        myRef.child("address").setValue(address);
        myRef.child("balance").setValue(balance);
        myRef.child("address").setValue(address);

        Toast.makeText(getApplicationContext(), "Bill Saved in Database", Toast.LENGTH_SHORT).show();

    }
    private void qrCodeGen(String word) {

        imageViewData = (ImageView) findViewById(R.id.imgViewCode);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(word, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewData.setImageBitmap(bitmap);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
    }

    private String formatter(String companyName, String dateAndTime, String id) {
        return companyName + "," + dateAndTime + "," + id;
    }

    private String[] formatter(String[] items) {
        String[] temp = new String[2];
        String time = items[0] + ":" + items[1];
        String date = items[2] + "/" + items[3]+ "/" + items[4];
        temp[0] = time;
        temp[1] = date;

        return temp;
    }
}
