package com.example.jeevanjyotandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgetPatient extends AppCompatActivity {
    EditText e1,e2,e3;
    Button b1;
    DatabaseHelperForPatient helper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_of_patient);
        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.pass);
        e3 = findViewById(R.id.confirm_pass);
        b1 = findViewById(R.id.login_btn);
        helper = new DatabaseHelperForPatient(ForgetPatient.this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = e1.getText().toString();
                String password = e2.getText().toString();
                String confirm = e3.getText().toString();
                if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(ForgetPatient.this, "Enter Data", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirm)) {
                        if (helper.checkOrUpdatePassword(username, password)) {
                            Toast.makeText(ForgetPatient.this, "Password Updated", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(ForgetPatient.this, LoginActivity.class);
                            startActivity(it);
                        } else {
                            Toast.makeText(ForgetPatient.this, "No such user exists, register yourself", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgetPatient.this, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

}
