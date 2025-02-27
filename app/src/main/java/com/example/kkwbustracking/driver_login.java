package com.example.kkwbustracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class driver_login extends AppCompatActivity
{
    EditText licence_no_edittext, name_edittext, phone_edittext, email_edittext;
    Button login;
    String licence_no, name, phone, email;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_login);

        // Set background color to beige
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFFFF0"));

        licence_no_edittext = findViewById(R.id.licence_no); // Textbox for entering the driver's license number
        phone_edittext = findViewById(R.id.phone); // Textbox for entering the driver's phone number
        name_edittext = findViewById(R.id.name); // Textbox for entering the driver's name
        email_edittext = findViewById(R.id.email); // Textbox for entering the driver's email address

        login = findViewById(R.id.login);
        databaseHelper = new DatabaseHelper(this);

        // Set custom colors for text boxes
        licence_no_edittext.setBackgroundColor(Color.parseColor("#F5E0C3"));
        licence_no_edittext.setTextColor(Color.WHITE); // Light beige
        phone_edittext.setBackgroundColor(Color.parseColor("#F5E0C3"));
        phone_edittext.setTextColor(Color.BLACK); // Light beige
        name_edittext.setBackgroundColor(Color.parseColor("#F5E0C3"));
        name_edittext.setTextColor(Color.BLACK); // Light beige
        email_edittext.setBackgroundColor(Color.parseColor("#F5E0C3"));
        email_edittext.setTextColor(Color.BLACK); // Light beige

        login.setBackgroundColor(Color.parseColor("#D19A66")); // Warm golden tone for the button
        login.setTextColor(Color.WHITE); // White text for contrast

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(driver_login.this, driver_tracklocation.class);

                name = name_edittext.getText().toString();
                phone = phone_edittext.getText().toString();
                licence_no = licence_no_edittext.getText().toString();
                email = email_edittext.getText().toString();

                if (TextUtils.isEmpty(name))
                    name_edittext.setError("Name is Mandatory");
                if (TextUtils.isEmpty(phone))
                    phone_edittext.setError("Phone No. is Mandatory");
                if (TextUtils.isEmpty(licence_no))
                    licence_no_edittext.setError("Licence No. is Mandatory");
                if (!(TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(licence_no)))
                {
                    databaseHelper.insertData(name, phone, licence_no, email); // Insert data into SqLite Database

                    startActivity(intent);
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Driver Register");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#A67B5B")); // Warm beige tone for toolbar
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // back button pressed
                onBackPressed();
            }
        });

        getWindow().setStatusBarColor(Color.parseColor("#7B5C3A")); // Darker shade for the status bar
        getWindow().setNavigationBarColor(Color.parseColor("#A67B5B")); // Match navigation bar to toolbar
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_withoutroute, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.share)
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
//        else
//        {
//            Intent intent = new Intent(driver_login.this, about.class);
//            startActivity(intent);
//        }

        return super.onOptionsItemSelected(item);
    }
}
