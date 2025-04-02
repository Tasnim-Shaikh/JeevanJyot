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

public class DoctorSignupFragmentTab extends Fragment {
    EditText username,email,mobile,specialisation,pass;
    Button sign;
    float v=0;
    DatabaseHelper databaseHelper;
    TextView login;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup root=(ViewGroup)inflater.inflate(R.layout.sign_upfragment2,container,false);
        email=root.findViewById(R.id.email);
        pass=root.findViewById(R.id.pass);
        username=root.findViewById(R.id.name);
        mobile=root.findViewById(R.id.Mobile);
        specialisation=root.findViewById(R.id.Specialisation);
        sign=root.findViewById(R.id.sign_up);
        login=root.findViewById(R.id.login);

        email.setTranslationX(800);
        pass.setTranslationX(800);
        username.setTranslationX(800);
        mobile.setTranslationX(800);
        specialisation.setTranslationX(800);
        sign.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        username.setAlpha(v);
        mobile.setAlpha(v);
        specialisation.setAlpha(v);
        sign.setAlpha(v);
        login.setAlpha(v);

        databaseHelper = new DatabaseHelper(getContext());
        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        mobile.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        specialisation.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        sign.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(getActivity(), LoginActivity.class);
                startActivity(it);
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString();
                String specialization = specialisation.getText().toString();
                String phone = mobile.getText().toString();
                String email1 = email.getText().toString();
                String password=pass.getText().toString();

                if (name.isEmpty() || specialization.isEmpty() || phone.isEmpty() || email1.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean insertSuccess = databaseHelper.addDoctor(name, specialization, phone, email1,password);
                    if (insertSuccess) {
                        Toast.makeText(getActivity(), "Doctor Registered Successfully!", Toast.LENGTH_SHORT).show();
                        username.setText("");
                        email.setText("");
                        mobile.setText("");
                        specialisation.setText("");
                        pass.setText("");
                    } else {
                        Toast.makeText(getActivity(), "Failed to Register!", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent it=new Intent(getActivity(),DoctorHome.class);
                startActivity(it);
            }
        });


        return root;

    }
}
