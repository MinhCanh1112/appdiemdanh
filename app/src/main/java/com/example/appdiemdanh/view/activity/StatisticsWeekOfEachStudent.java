package com.example.appdiemdanh.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.StatisWeekHsAdapter;
import com.example.appdiemdanh.data.model.StatisWeek;
import com.example.appdiemdanh.data.api.ApiDiemdanh;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.ActivityStatisticsWeekOfEachStudentBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.AttendanceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsWeekOfEachStudent extends AppCompatActivity {
    ActivityStatisticsWeekOfEachStudentBinding b;
    AttendanceViewModel attendanceViewModel;
    StatisWeek statisWeek;
    int idhs = 0;
    int idweek = 0;
    int sl_vaotre = 0;
    int sl_vesom = 0;
    int sl_vang = 0;
    StatisWeekHsAdapter statisWeekHsAdapter;
    boolean isExpandvesom = true;
    boolean isExpandvaotre = true;
    boolean isExpandvang = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this,R.layout.activity_statistics_week_of_each_student);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        ActionToolbar();

        Intent intent = getIntent();
        statisWeek = (StatisWeek) intent.getSerializableExtra("dataThongke");

        setData();

        getThongkeHs(b.recycTkhsVaotre,"Đi học trễ");
        getThongkeHs(b.recycTkhsVesom,"Về sớm");
        getThongkeHs(b.recycTkhsVang,"Vắng");
        showOrHideTrangthaiDiemdanh(b.linearVaotre,b.tvSlVaotre,sl_vaotre,isExpandvaotre,b.recycTkhsVaotre,b.img1);
        showOrHideTrangthaiDiemdanh(b.linearVesom,b.tvSlVesom,sl_vesom,isExpandvesom,b.recycTkhsVesom,b.img2);
        showOrHideTrangthaiDiemdanh(b.linearVang,b.tvSlVang,sl_vang,isExpandvang,b.recycTkhsVang,b.img3);
    }

    private void setData() {
        idhs = statisWeek.getIdhs();
        idweek = statisWeek.getIdweek();
        sl_vaotre = statisWeek.getVaotre();
        sl_vesom = statisWeek.getVesom();
        sl_vang = statisWeek.getVang();

        b.tvUid.setText(statisWeek.getUid());
        b.tvHoten.setText(statisWeek.getName());
        b.tvMshs.setText(statisWeek.getMshs());
        b.tvSdt.setText(statisWeek.getMobile());
        b.tvGioitinh.setText(statisWeek.getGender());
        String url = Utils.BASE_URL +"hocsinh/image/"+statisWeek.getAvatar();
        Glide.with(StatisticsWeekOfEachStudent.this).load(url).into(b.picturetkhs);
    }


    private void showOrHideTrangthaiDiemdanh(LinearLayout ln, TextView tv, int sl, boolean a, RecyclerView recyc, ImageView img) {
        tv.setText(String.valueOf(sl));
        final boolean[] flag = {a};
        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0]) {
                    flag[0] = false;
                    recyc.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                } else {
                    flag[0] = true;
                    recyc.setVisibility(View.GONE);
                    img.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                }
            }
        });
    }


    private void getThongkeHs(RecyclerView recyc,String trangthai) {
        int iduser = Utils.getUserId(StatisticsWeekOfEachStudent.this);
        attendanceViewModel.getThongkeHocsinh(iduser,idweek,idhs,trangthai).observe(StatisticsWeekOfEachStudent.this,statisWeeks -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(StatisticsWeekOfEachStudent.this);
            recyc.setLayoutManager(layoutManager);
            statisWeekHsAdapter = new StatisWeekHsAdapter(StatisticsWeekOfEachStudent.this,statisWeeks);
            recyc.setAdapter(statisWeekHsAdapter);
            statisWeekHsAdapter.notifyDataSetChanged();
        });
    }

    private void ActionToolbar() {
        setSupportActionBar(b.toolbarTkhs);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        b.toolbarTkhs.setNavigationOnClickListener(v -> finish());
    }

}