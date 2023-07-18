package com.example.appdiemdanh.view.activity;


import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.data.api.ApiUser;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.ActivityRegisterUserBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.UserViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterUser extends AppCompatActivity {
    ActivityRegisterUserBinding binding;
    UserViewModel userViewModel;
    private  String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_user);
        setData();
        binding.progressRegister.setVisibility(View.GONE);
        EventRegister();
        EventLogin();
    }

    private void EventLogin() {
        binding.txtDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void EventRegister() {
        binding.btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaili = binding.edtEmail.getText().toString();
                String usernamei = binding.edtUsername.getText().toString();
                String mobilei = binding.edtSdt.getText().toString();
                String lopi = binding.edtTenlop.getText().toString();
                String passi = binding.edtPw.getText().toString();
                String confirmpassi = binding.edtConfpw.getText().toString();

                if(!emaili.matches(emailPattern)){
                    binding.edtEmail.setError("Nhập Email hợp lệ!");
                }else if(usernamei.isEmpty()){
                    binding.edtUsername.setError("Không được để trống");
                }else if(mobilei.isEmpty()){
                    binding.edtSdt.setError("Không được để trống");
                }else if(lopi.isEmpty()){
                    binding.edtTenlop.setError("Không được để trống");
                } else if (mobilei.length() != 10) {
                    binding.edtSdt.setError("Số điện thoại không chính xác");
                }else if(passi.isEmpty() || passi.length() < 6) {
                    binding.edtPw.setError("Mật khẩu tối thiểu 6 ký tự");
                }else if(!passi.equals(confirmpassi)){
                    binding.edtConfpw.setError("Mật khẩu xác nhận không chính xác");
                }else {
                    binding.progressRegister.setVisibility(View.VISIBLE);

                    RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emaili);
                    RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), usernamei);
                    RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobilei);
                    RequestBody lop = RequestBody.create(MediaType.parse("multipart/form-data"), lopi);
                    RequestBody pass = RequestBody.create(MediaType.parse("multipart/form-data"), passi);

                    Drawable drawable = getResources().getDrawable(R.drawable.ic_people);
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    File file = saveBitmapToFile(bitmap);
                    // tạo đối tượng RequestBody từ file hình ảnh
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    // tạo đối tượng MultipartBody.Part từ requestFile với tên là "image"
                    MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                    // gọi phương thức uploadImage của ApiService để gửi hình ảnh lên server

                    userViewModel.RegisterUser(email,username,mobile,lop,pass,avatar).observe(RegisterUser.this,user -> {
                        if (!isInternetAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            binding.progressRegister.setVisibility(View.GONE);
                        }else {
                            if (user != null &&user.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // Email đã tồn tại, hiển thị thông báo lỗi
                                String message = user.getMessage();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } binding.progressRegister.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }

    private File saveBitmapToFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(null), "ic_people.jpg");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void setData() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

}