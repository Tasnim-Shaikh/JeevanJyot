//package com.example.jeevanjyotandroidapplication;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.viewpager.widget.ViewPager;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.tabs.TabLayout;
//
//public class LoginActivity extends AppCompatActivity {
//
//    TabLayout tabLayout;
//    ViewPager viewPager;
//    FloatingActionButton fb,google,twit;
//    float v=0;
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//        tabLayout=findViewById(R.id.tab_layout);
//        viewPager=findViewById(R.id.view_pager);
//        fb=findViewById(R.id.fab_facebook);
//        google=findViewById(R.id.fab_google);
//        twit=findViewById(R.id.fab_twitter);
//        int unselectedColor = ContextCompat.getColor(this, R.color.black);
//        int selectedColor = ContextCompat.getColor(this, R.color.lavendar);
//        tabLayout.setTabTextColors(unselectedColor, selectedColor);
//
//// Now add your tabs
//
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager); // This should be before addTab()
//
//        tabLayout.addTab(tabLayout.newTab().setText("Login as Patient"));
//        tabLayout.addTab(tabLayout.newTab().setText("Login as Doctor"));
//
//
//
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.black),
//                ContextCompat.getColor(this, R.color.lavendar));
//
//        final LoginAdapter adapter=new LoginAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        fb.setTranslationY(300);
//        google.setTranslationY(300);
//        twit.setTranslationY(300);
//        tabLayout.setTranslationY(300);
//        fb.setAlpha(v);
//        google.setAlpha(v);
//        twit.setAlpha(v);
//        tabLayout.setAlpha(v);
//
//        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
//        twit.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
//
//    }
//}
package com.example.jeevanjyotandroidapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static final float ANIMATION_START_TRANSLATION = 300f;
    private static final int ANIMATION_DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize UI components
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        // Set tab text colors
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.lavendar)
        );

        // Set up adapter for ViewPager
        LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, 2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager); // Automatically sets titles

        // Set tab gravity
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Floating Action Button Animations

    }
}
