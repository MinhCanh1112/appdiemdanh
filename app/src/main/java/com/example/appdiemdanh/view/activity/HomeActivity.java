package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.appdiemdanh.R;
import com.example.appdiemdanh.data.model.User;
import com.example.appdiemdanh.data.api.ApiUser;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.util.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private ImageView imageView;
    private CircleImageView imageView_header;
    private TextView txtusername;
    private LinearLayout linear,lineardd,lineardsdd;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView navname,navemail;
    private ApiUser apiUser;
    private long mBackPressed;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        anhxa();
        ActionBar();
        navigationView.setNavigationItemSelectedListener(this);
        getHomeNav();
        btn_home();
    }

    private void getHomeNav(){
        int iduser = Utils.getUserId(HomeActivity.this);
        Call<User> call = apiUser.thongtintaikhoan(iduser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                txtusername.setText(user.getUsername());
                navname.setText(user.getUsername());
                navemail.setText(user.getEmail());
                Glide.with(getApplicationContext()).load(Utils.BASE_URL+"taikhoan/imageUser/"+user.getAvatar()).into(imageView_header);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (!isInternetAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("View",t.getMessage());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getHomeNav();
    }

    private void btn_home(){
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dshs = new Intent(getApplicationContext(), ListStudent.class);
                startActivity(dshs);
            }
        });

        lineardsdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsdd = new Intent(getApplicationContext(), AttendanceDate.class);
                startActivity(dsdd);
            }
        });

        lineardd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dd = new Intent(getApplicationContext(), AttendanceActivity.class);
                startActivity(dd);
            }
        });

        Glide.with(this)
                .load("https://thuthuatnhanh.com/wp-content/uploads/2020/11/hinh-anh-co-giao-va-hoc-sinh-cham-chu-trong-gio-hoc.jpg")
                .into(imageView);


    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                getHomeNav();
            }
        });
    }

    private void anhxa() {
        navigationView =(NavigationView) findViewById(R.id.navigationview);
        navname =navigationView.getHeaderView(0).findViewById(R.id.navname);
        navemail = navigationView.getHeaderView(0).findViewById(R.id.navemail);
        imageView_header = navigationView.getHeaderView(0).findViewById(R.id.imageView_header);
        imageView = findViewById(R.id.my_image_view);
        linear = findViewById(R.id.linear);
        lineardsdd = findViewById(R.id.lineardsdd);
        lineardd = findViewById(R.id.lineardd);
        lineardsdd = findViewById(R.id.lineardsdd);
        txtusername =findViewById(R.id.txtusername);
        toolbar =(Toolbar) findViewById(R.id.toolbarmanhinhchinh);
        apiUser = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiUser.class);
        drawerLayout = findViewById(R.id.drawerlayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (mBackPressed + 2000 > System.currentTimeMillis()){
                super.onBackPressed();
                // Đăng xuất người dùng ở đây
                return;
            }
            else { Toast.makeText(getBaseContext(), "Nhấn Back lần nữa để đăng xuất", Toast.LENGTH_SHORT).show(); }
            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(id == R.id.nav_dshs){
            Intent dshs = new Intent(getApplicationContext(), ListStudent.class);
            startActivity(dshs);
        }else if(id == R.id.nav_inforuser){
            Intent hoso = new Intent(getApplicationContext(), InformationUser.class);
            hoso.putExtra("dataInforUser",user);
            startActivity(hoso);
        }else if(id == R.id.nav_attendWeek){
            Intent w = new Intent(getApplicationContext(), ChooseWeek.class);
            startActivity(w);
        }
        else if(id == R.id.nav_out){
            AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
            dialog.setMessage("Bạn có muốn đăng xuất không?");
            dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}