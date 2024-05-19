package com.example.eventfinder;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import com.example.eventfinder.ui.main.SectionsPagerAdapter;
import com.example.eventfinder.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ViewPager2 viewPager2;
    TabLayout tabView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_EventFinder_NoActionBar);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewPager2 = findViewById(R.id.viewPager);
        tabView =  findViewById(R.id.tabLayout);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabView, viewPager2,
                (tab, position) -> {
            switch(position){
                case 0:
                    tab.setText("Search");
                    break;
                case 1:
                    System.out.println(tab);
                    System.out.println(position);
                    tab.setText("Favorites");
                    break;
                default:
                    tab.setText("Tab");
                    break;
            }
        }
        ).attach();

    }
}