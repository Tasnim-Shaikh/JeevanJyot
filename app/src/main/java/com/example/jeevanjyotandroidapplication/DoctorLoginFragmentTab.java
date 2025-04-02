package com.example.jeevanjyotandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class DoctorLoginFragmentTab extends Fragment {
    EditText email,pass;
    TextView forget,register;
    Button login;
    DatabaseHelper databaseHelper;  // Your SQLite database helper class

    float v=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root=(ViewGroup)inflater.inflate(R.layout.loginfragment2,container,false);
        email=root.findViewById(R.id.email);
        pass=root.findViewById(R.id.pass);
        forget=root.findViewById(R.id.forget);
        login=root.findViewById(R.id.login_btn);
        register=root.findViewById(R.id.NotRegister);

        databaseHelper = new DatabaseHelper(getActivity());


        email.setTranslationX(800);
        pass.setTranslationX(800);
        forget.setTranslationX(800);
        login.setTranslationX(800);
        register.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        forget.setAlpha(v);
        login.setAlpha(v);
        register.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forget.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        register.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getActivity(),SignupActivity.class);
                startActivity(it);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getActivity(),ForgetDoctor.class);
                startActivity(it);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = email.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    if (databaseHelper.checkUser(username, password)) {
                        Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_SHORT).show();

                        // Navigate to Home Activity
                        Intent intent = new Intent(getActivity(), DoctorHome.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return root;
    }
}
