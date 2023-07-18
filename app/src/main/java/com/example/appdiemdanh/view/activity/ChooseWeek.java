package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.ChooseWeekAdapter;
import com.example.appdiemdanh.data.model.StatisWeek;
import com.example.appdiemdanh.data.api.ApiDiemdanh;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.ActivityChooseWeekBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.AttendanceViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseWeek extends AppCompatActivity {
    ActivityChooseWeekBinding binding;
    AttendanceViewModel attendanceViewModel;
    ChooseWeekAdapter chooseWeekAdapter;
    ApiDiemdanh apiDiemdanh;
    String week ="";
    String yeari ="";
    private Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_choose_week);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        actionToolbar();

        binding.chooseweek.setFocusableInTouchMode(false);
        binding.chooseweek.setFocusable(false);
        binding.chooseweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChooseWeek.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        chooseWeek();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.chooseweek.setText(sdf.format(myCalendar.getTime()));
            week = String.valueOf(myCalendar.get(Calendar.WEEK_OF_YEAR)-1);
            yeari = String.valueOf(myCalendar.get(Calendar.YEAR));

            // Hiển thị số tuần và số năm hiện tại trên TextView tương ứng
            binding.txtweek.setText("Tuần: "+week);
            binding.txtyear.setText("Năm: "+yeari);
        }
    };

    private void chooseWeek() {
        binding.btnFindweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iduser = Utils.getUserId(getApplicationContext());
                attendanceViewModel.getChooseWeek(iduser,week,yeari).observe(ChooseWeek.this,statisWeekList -> {
                    if (!isInternetAvailable(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }else {
                        if(statisWeekList.isEmpty()){
                            binding.txtdswtrong.setVisibility(View.VISIBLE);
                            binding.recycChooseweek.setVisibility(View.GONE);
                        }else {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ChooseWeek.this);
                            binding.recycChooseweek.setLayoutManager(layoutManager);
                            chooseWeekAdapter = new ChooseWeekAdapter(ChooseWeek.this,statisWeekList);
                            binding.recycChooseweek.setAdapter(chooseWeekAdapter);
                            binding.recycChooseweek.setVisibility(View.VISIBLE);
                            binding.txtdswtrong.setVisibility(View.GONE);
                            chooseWeekAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    private void actionToolbar() {
        setSupportActionBar(binding.toolbarChooseweek);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarChooseweek.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}