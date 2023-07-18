package com.example.appdiemdanh.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.DaysPagerAdapter;
import com.example.appdiemdanh.databinding.ActivityAttendanceBinding;

import java.util.Objects;

public class AttendanceActivity extends AppCompatActivity {
    ActivityAttendanceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_attendance);
        ActionBar();
        EventFragment();

    }

    private void EventFragment() {

        // Create and set adapter
        DaysPagerAdapter adapter = new DaysPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);

        // Connect TabLayout and ViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        // Customize tab appearance
        int todayColor = ContextCompat.getColor(this, R.color.teal_200);
        int otherDaysColor = ContextCompat.getColor(this, R.color.black);
        binding.tabLayout.setSelectedTabIndicatorColor(todayColor);
        binding.tabLayout.setTabTextColors(otherDaysColor, todayColor);
    }

    private void ActionBar() {
        setSupportActionBar(binding.toolbarDiemdanh);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarDiemdanh.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_diemdanh,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_diemdanh) {
            Intent intent = new Intent(this, AddAttendance.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}