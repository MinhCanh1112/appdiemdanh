package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.data.model.Class;
import com.example.appdiemdanh.data.model.DateTimediemdanh;
import com.example.appdiemdanh.databinding.ActivityAttendanceStatisticsBinding;

import java.util.Objects;

public class AttendanceStatistics extends AppCompatActivity {
    ActivityAttendanceStatisticsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_attendance_statistics);
        actionToolbar();
        getThongke();

    }

    @SuppressLint("SetTextI18n")
    public void getThongke(){
        Intent intent = getIntent();
        Class classThongke = (Class) intent.getSerializableExtra("dataThongke");
        DateTimediemdanh dateDiemdanh = (DateTimediemdanh) intent.getSerializableExtra("dataDateDiemdanh");
        if (!isInternetAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }else {
            binding.txtdate.setText("Ngày điểm danh: "+ dateDiemdanh.getDate());
            binding.txtgiovao.setText("Giờ vào: "+ dateDiemdanh.getTimevao());
            binding.txtgiora.setText("Giờ ra: "+ dateDiemdanh.getTimera());
            binding.txtclass.setText("Lớp: "+classThongke.getLop());
            binding.txtSiso.setText("Sĩ số: "+classThongke.getSiso());
            binding.txtDadiemdanh.setText("Đã điểm danh: "+classThongke.getDadiemdanh());
            binding.txtVang.setText("Vắng: "+classThongke.getVang());
            binding.txtVaotre.setText("Đi trễ: "+classThongke.getVaotre());
            binding.txtVesom.setText("Về sớm: "+classThongke.getVesom());
        }
    }

    private void actionToolbar() {
        setSupportActionBar(binding.toolbarTkdd);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarTkdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}