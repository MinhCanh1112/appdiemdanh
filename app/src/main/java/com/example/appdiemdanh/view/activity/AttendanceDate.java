package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.AttendanceDateAdapter;
import com.example.appdiemdanh.data.model.DateTimediemdanh;
import com.example.appdiemdanh.data.model.Attendance;
import com.example.appdiemdanh.data.api.ApiDiemdanh;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.ActivityAttendanceDateBinding;
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

public class AttendanceDate extends AppCompatActivity {
    ActivityAttendanceDateBinding binding;
    AttendanceViewModel attendanceViewModel;
    private AttendanceDateAdapter ngaydiemdanhAdapter;
    private Calendar myCalendar = Calendar.getInstance();
    int iduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_attendance_date);
        initView();
        actionToolbar();
        ngayhientai();
        binding.choosengaydd.setFocusableInTouchMode(false);
        binding.choosengaydd.setFocusable(false);
        binding.choosengaydd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AttendanceDate.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.btnFinddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNgaydiemdanh();
            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setBirth();
        }
    };

    private void setBirth() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.choosengaydd.setText(sdf.format(myCalendar.getTime()));
    }

    private void ngayhientai() {
        // Lấy ngày và tháng hiện tại
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // tháng bắt đầu từ 0 nên cần cộng thêm 1
        int year = calendar.get(Calendar.YEAR); // tháng bắt đầu từ 0 nên cần cộng thêm 1
        // Hiển thị ngày hiện tại trên TextView
        binding.choosengaydd.setText(year+"-"+ month + "-" + day);
    }

    private void getNgaydiemdanh() {
        String date = binding.choosengaydd.getText().toString();
        if(date.isEmpty()){
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
        }else{
            attendanceViewModel.getNgayDiemdanh(iduser,date).observe(this,ngaydiemdanhs -> {
                if (!isInternetAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }else {
                    if(ngaydiemdanhs.isEmpty()){
                        binding.txtdstrong.setVisibility(View.VISIBLE);
                        binding.recyclerviewNgay.setVisibility(View.GONE);
                    }else{
                        ngaydiemdanhAdapter = new AttendanceDateAdapter(AttendanceDate.this,ngaydiemdanhs);
                        binding.recyclerviewNgay.setAdapter(ngaydiemdanhAdapter);
                        binding.recyclerviewNgay.setVisibility(View.VISIBLE);
                        binding.txtdstrong.setVisibility(View.GONE);
                        ngaydiemdanhAdapter.notifyDataSetChanged();
                    }
                }
            });

        }
    }

    public void deleteNgaydd( int idngaydd,int idsetupdd ) {
        attendanceViewModel.deleteAttendanceDate(idngaydd,idsetupdd).observe(this,ngaydiemdanh -> {
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
                getNgaydiemdanh();
            }
        });

    }

    private void initView() {
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        iduser = Utils.getUserId(this);
        binding.recyclerviewNgay.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerviewNgay.setLayoutManager(layoutManager);
    }

    private void actionToolbar() {
        setSupportActionBar(binding.toolbarNgay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarNgay.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}