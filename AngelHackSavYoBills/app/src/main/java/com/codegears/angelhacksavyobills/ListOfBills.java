package com.codegears.angelhacksavyobills;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.R.attr.id;

public class ListOfBills extends AppCompatActivity {

    List<String> billList = new ArrayList<String>();
    List<String> arrayList = new ArrayList<String>();

    private ListView billsListView;
    private String uniqueId, uniqueKey, userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_bills);


        billsListView = (ListView) findViewById(R.id.billsListView);

        userName = getIntent().getStringExtra("USER_NAME");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, billList);

        billsListView.setAdapter(arrayAdapter);

        DatabaseReference myRef = MyFireBase.database.getReference("Bills").child(userName);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                billList.clear();

                GenericTypeIndicator<Map<String, Map<String, String>>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Map<String, String>>>() {
                };
                Map<String, Map<String, String>> map = dataSnapshot.getValue(genericTypeIndicator);

                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    uniqueId = (String) pair.getKey();
                    String[] items = uniqueId.split(",");
                    String company = items[0];
                    String time = items[1] + ":" + items[2];
                    String date = items[5] + "/" + items[4] + "/" + items[3];

//                    billList.add(company+"\t\t\t\t\t\t"+time+"\t\t\t\t\t\t"+date+"\n\n"+"\t\t\t\t\t\t\t\t\tReceipt Number -> "+id);
                    billList.add(" Company Name : " + company + "\n" + " Date & Time : " + time + " && " + date + "\n" + " Receipt Number : " + id);
                    arrayList.add(uniqueId);

//                    Log.d("myTag", company);
                    it.remove(); // avoids a ConcurrentModificationException
                }

                arrayAdapter.notifyDataSetChanged();

                billsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < billList.size(); i++) {
                            if (position == i) {
                                Intent intent = new Intent(ListOfBills.this, BillInfo.class);

                                String uniqueId = arrayList.get(i);
                                String[] items = uniqueId.split(",");
                                String company = items[0];
                                String time = items[1] + ":" + items[2];
                                String date = items[5] + "/" + items[4] + "/" + items[3];
                                String uniqueKey = items[6];

                                intent.putExtra("uniqueId", uniqueId);
                                intent.putExtra("uniqueKey", uniqueKey);
                                intent.putExtra("company", company);
                                intent.putExtra("time", time);
                                intent.putExtra("date", date);
                                intent.putExtra("USER_NAME", userName);

                                startActivity(intent);
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
