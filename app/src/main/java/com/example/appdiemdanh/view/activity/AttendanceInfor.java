package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.os.Bundle;
import android.view.View;

import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.TimeVesomAdapter;
import com.example.appdiemdanh.data.model.DateTimediemdanh;

import com.example.appdiemdanh.databinding.ActivityAttendanceInforBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.AttendanceViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class AttendanceInfor extends AppCompatActivity {
    ActivityAttendanceInforBinding binding;
    AttendanceViewModel attendanceViewModel;
    DateTimediemdanh dateTimediemdanh;
    int idsetupdd = 0;
    private TimeVesomAdapter timeVesomAdapter;
    int iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_attendance_infor);
        ActionToolbar();
        setData();
        EndDiemdanh();
        Vesom();
        getTimeVesom();
        EventUpdateTime();
    }


    private void setData() {
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        iduser = Utils.getUserId(AttendanceInfor.this);
        binding.recycVesom.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycVesom.setLayoutManager(layoutManager);

        dateTimediemdanh = (DateTimediemdanh) getIntent().getSerializableExtra("dateTimediemdanh");
        idsetupdd = dateTimediemdanh.getId();
        binding.edtDateAttendance.setText(dateTimediemdanh.getDate());
        binding.edtTimestartUp.setText(dateTimediemdanh.getTimestart());
        binding.edtTimevaoUp.setText(dateTimediemdanh.getTimevao());
        binding.edtTimeraUp.setText(dateTimediemdanh.getTimera());
    }

    private void EventUpdateTime() {
        binding.btnUpdatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timestart= binding.edtTimestartUp.getText().toString().trim();
                String timevao= binding.edtTimevaoUp.getText().toString().trim();
                String timera= binding.edtTimeraUp.getText().toString().trim();
                if(timestart.isEmpty()||timevao.isEmpty()||timera.isEmpty()){
                    Toast.makeText(AttendanceInfor.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                }else{
                    attendanceViewModel.updateAttendanceTime(idsetupdd,timestart,timevao,timera).observe(AttendanceInfor.this,dateTimediemdanh -> {
                        if (!isInternetAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }else{
                            if(dateTimediemdanh!=null&&dateTimediemdanh.isSuccess()){
                                Toast.makeText(AttendanceInfor.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AttendanceInfor.this, dateTimediemdanh.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void getTimeVesom() {
        attendanceViewModel.getTimeVesom(iduser,idsetupdd).observe(this,dateTimediemdanhs -> {
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else{
                timeVesomAdapter = new TimeVesomAdapter(AttendanceInfor.this,dateTimediemdanhs);
                binding.recycVesom.setAdapter(timeVesomAdapter);
                timeVesomAdapter.notifyDataSetChanged();
            }
        });

    }

    private void Vesom() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        // Định dạng thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:00");
        // Gán giá trị vào EditText
        binding.edtTimenow.setText(dateFormat.format(calendar.getTime()));
        binding.btnVesom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timevesom = binding.edtTimenow.getText().toString();
                if (timevesom.isEmpty()) {
                    binding.edtTimenow.setError("Không được để trống");
                }else {
                    attendanceViewModel.insertTimeVesom(iduser,idsetupdd,timevesom).observe(AttendanceInfor.this,dateTimediemdanh -> {
                        if (!isInternetAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }else{
                            if (dateTimediemdanh != null && dateTimediemdanh.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "Bắt đầu thiết lập về sớm", Toast.LENGTH_SHORT).show();
                                getTimeVesom();
                            } else {
                                // Xử lý khi đăng nhập thất bại
                                String message = dateTimediemdanh.getMessage();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    public void huyvesom(){
        attendanceViewModel.deteleTimeVesom(iduser,idsetupdd).observe(this,dateTimediemdanh -> {
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                if (dateTimediemdanh != null && dateTimediemdanh.isSuccess()) {
                    getTimeVesom();
                    Toast.makeText(AttendanceInfor.this, "Đã kết thúc thiết lập về sớm", Toast.LENGTH_SHORT).show();

                } else {
                    // Xử lý khi đăng nhập thất bại
                    String message = dateTimediemdanh.getMessage();
                    Toast.makeText(AttendanceInfor.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void EndDiemdanh() {
        binding.btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceViewModel.deteleAttendanceTime(idsetupdd).observe(AttendanceInfor.this,timediemDanh -> {
                    if (!isInternetAvailable(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }else {
                        if (timediemDanh != null && timediemDanh.isSuccess()) {
                            Toast.makeText(AttendanceInfor.this, "Kết thúc điểm danh", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // Xử lý khi đăng nhập thất bại
                            String message = timediemDanh.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void ActionToolbar() {
        setSupportActionBar(binding.toolbarDate);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarDate.setNavigationOnClickListener(v -> finish());
    }

}