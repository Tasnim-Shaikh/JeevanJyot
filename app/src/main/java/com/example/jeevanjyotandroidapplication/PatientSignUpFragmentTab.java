package com.example.jeevanjyotandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class PatientSignUpFragmentTab extends Fragment {
    EditText email, pass, mobile, username;
    Button sign_up;
    float v = 0;
    TextView login;
    DatabaseHelperForPatient databaseHelperForPatient;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sign_upfragment, container, false);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        username = root.findViewById(R.id.name);
        mobile = root.findViewById(R.id.Mobile);
        sign_up = root.findViewById(R.id.sign_up);
        login=root.findViewById(R.id.login);
        email.setTranslationX(800);
        pass.setTranslationX(800);
        username.setTranslationX(800);
        mobile.setTranslationX(800);
        sign_up.setTranslationX(800);
        login.setTranslationX(800);
        email.setAlpha(v);
        pass.setAlpha(v);
        username.setAlpha(v);
        mobile.setAlpha(v);
        sign_up.setAlpha(v);
        login.setAlpha(v);
        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        mobile.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        sign_up.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();

        databaseHelperForPatient = new DatabaseHelperForPatient(getContext());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getActivity(), LoginActivity.class);
                startActivity(it);
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString().trim();
                String phone = mobile.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || email1.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean insertSuccess = databaseHelperForPatient.addPatient(name, phone, email1, password);
                    if (insertSuccess) {
                        Toast.makeText(getActivity(), "Patient Registered Successfully!", Toast.LENGTH_SHORT).show();
                        Log.d("DB_SUCCESS", "New patient inserted: " + name);

                        username.setText("");
                        email.setText("");
                        mobile.setText("");
                        pass.setText("");

                        Intent it = new Intent(getActivity(), PatientHome.class);
                        startActivity(it);
                    } else {
                        Toast.makeText(getActivity(), "Failed to Register!", Toast.LENGTH_SHORT).show();
                        Log.e("DB_ERROR", "Failed to insert patient: " + name);
                    }
                }
            }
        });

        return root;
    }
}
