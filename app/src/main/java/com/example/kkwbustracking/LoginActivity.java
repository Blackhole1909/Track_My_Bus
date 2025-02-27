package com.example.kkwbustracking;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.login_button);

        String correctPassword = "parent123";  // Set the password

        loginButton.setOnClickListener(v -> {
            String enteredPassword = passwordInput.getText().toString();
            if (enteredPassword.equals(correctPassword)) {
                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
            } else {
                Toast.makeText(this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

