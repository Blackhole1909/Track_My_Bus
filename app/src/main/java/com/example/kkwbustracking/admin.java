package com.example.kkwbustracking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin extends AppCompatActivity {

    private TextView txtActiveUsers, txtDriverName, txtDriverLicense, txtDriverStatus;
    private ListView listDrivers, listStudents;
    private Button btnLogout;
    private DatabaseReference driversRef, studentsRef;

    private ArrayList<String> driverList = new ArrayList<>();
    private ArrayList<String> studentList = new ArrayList<>();
    private ArrayAdapter<String> driverAdapter, studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        txtActiveUsers = findViewById(R.id.txtActiveUsers);
        txtDriverName = findViewById(R.id.txtDriverName);
        txtDriverLicense = findViewById(R.id.txtDriverLicense);
        txtDriverStatus = findViewById(R.id.txtDriverStatus);
        listDrivers = findViewById(R.id.listDrivers);
        listStudents = findViewById(R.id.listStudents);
        btnLogout = findViewById(R.id.btnLogout);

        driversRef = FirebaseDatabase.getInstance().getReference("drivers");
        studentsRef = FirebaseDatabase.getInstance().getReference("students");

        driverAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, driverList);
        studentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        listDrivers.setAdapter(driverAdapter);
        listStudents.setAdapter(studentAdapter);

        fetchDrivers();
        fetchStudents();

        listDrivers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDriver = driverList.get(position);
                displayDriverDetails(selectedDriver);
            }
        });

        listStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStudent = studentList.get(position);
                callParent(selectedStudent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchDrivers() {
        driversRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                driverList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String driverName = data.child("name").getValue(String.class);
                    driverList.add(driverName);
                }
                driverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(admin.this, "Failed to load drivers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchStudents() {
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String studentName = data.child("name").getValue(String.class);
                    studentList.add(studentName);
                }
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(admin.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDriverDetails(String driverName) {
        driversRef.orderByChild("name").equalTo(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    txtDriverName.setText("Driver Name: " + data.child("name").getValue(String.class));
                    txtDriverLicense.setText("License Number: " + data.child("license").getValue(String.class));
                    txtDriverStatus.setText("Status: " + data.child("status").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(admin.this, "Error loading driver details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callParent(String studentName) {
        studentsRef.orderByChild("name").equalTo(studentName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String parentPhone = data.child("parentPhone").getValue(String.class);
                    if (parentPhone != null) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + parentPhone));
                        startActivity(callIntent);
                    } else {
                        Toast.makeText(admin.this, "Parent contact not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(admin.this, "Error fetching parent contact", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
