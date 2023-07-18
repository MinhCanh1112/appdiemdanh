package com.example.appdiemdanh.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.data.model.User;
import com.example.appdiemdanh.data.api.ApiUser;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.ActivityLoginUserBinding;
import com.example.appdiemdanh.databinding.DialogQuenmatkhauBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUser extends AppCompatActivity {
    ActivityLoginUserBinding binding;
    UserViewModel userViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ApiUser apiUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_user);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setData();
        binding.progressLogin.setVisibility(View.GONE);

        EventLogin();
        EventRegister();
        EventResetPass();
    }


    private void EventResetPass() {
        binding.tvQuenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(LoginUser.this);
                DialogQuenmatkhauBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(LoginUser.this),R.layout.dialog_quenmatkhau,null,false);
                dialog.setContentView(binding1.getRoot());
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                binding1.progressQuenmk.setVisibility(View.GONE);
                binding1.btnResetpass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = binding1.edtDialogEmail.getText().toString();
                        if (email.isEmpty()){
                            binding1.edtDialogEmail.setError("Không được để trống");
                        } else{
                            binding1.progressQuenmk.setVisibility(View.VISIBLE);
                            userViewModel.resetPass(email).observe(LoginUser.this,user -> {
                                if (!isInternetAvailable(getApplicationContext())) {
                                    Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    binding1.progressQuenmk.setVisibility(View.GONE);
                                }else {
                                    if (user != null && user.isSuccess()) {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Đã gửi mật khẩu mới đến email. Vui lòng kiểm tra email", Toast.LENGTH_LONG).show();
                                    }else{
                                        String message = user.getMessage();
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                    binding1.progressQuenmk.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });
                binding1.btnCancelRspass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void EventRegister() {
        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterUser.class);
                startActivity(intent);
            }
        });
    }

    private void EventLogin() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.edtEmail.getText().toString().trim();
                String pass = binding.edtPw.getText().toString().trim();
                if(email.isEmpty()){
                    binding.edtEmail.setError("Không được để trống");
                }else if(pass.isEmpty()|| pass.length() < 6) {
                    binding.edtPw.setError("Mật khẩu tối thiểu 6 ký tự");
                }else{
                    binding.progressLogin.setVisibility(View.VISIBLE);
                    userViewModel.loginUser(email,pass).observe(LoginUser.this,user -> {
                        if (!isInternetAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            binding.progressLogin.setVisibility(View.GONE);
                        }else {
                            if (user != null && user.isSuccess()) {
                                Utils.saveUser(LoginUser.this, user.getId());
                                saveinfo();
                                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intent);

                            }else {
                                // Xử lý khi đăng nhập thất bại
                                String message = user.getMessage();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }binding.progressLogin.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void setData() {
        apiUser = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiUser.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.progressLogin.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);
        binding.edtEmail.setText(sharedPreferences.getString("email",""));
        binding.edtPw.setText(sharedPreferences.getString("matkhau",""));
        binding.checkboxSave.setChecked(sharedPreferences.getBoolean("checkbox",false));
    }

    private void saveinfo() {
        if(binding.checkboxSave.isChecked()){
            editor = sharedPreferences.edit();
            editor.putString("email", binding.edtEmail.getText().toString().trim());
            editor.putString("matkhau",binding.edtPw.getText().toString().trim());
            editor.putBoolean("checkbox",true);
            editor.commit();
        } else{
            editor = sharedPreferences.edit();
            binding.edtEmail.setText("");
            binding.edtPw.setText("");
            editor.remove("email");
            editor.remove("matkhau");
            editor.remove("checkbox");
            editor.commit();
        }
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}