package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.appdiemdanh.R;
import com.example.appdiemdanh.data.model.RealPathUtil;
import com.example.appdiemdanh.data.model.User;
import com.example.appdiemdanh.data.api.ApiUser;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.ActivityInformationUserBinding;
import com.example.appdiemdanh.databinding.DialogDoimatkhauBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.UserViewModel;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class InformationUser extends AppCompatActivity {
    ActivityInformationUserBinding binding;
    UserViewModel userViewModel;
    User user;
    private static final int MY_REQUEST_CODE = 20;
    private Uri mUri;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            binding.inforAvt.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_information_user);
        setData();
        actionToolbar();
        Chonhinhanh();
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("dataInforUser");
        Thongtintaikhoan();

    }


    private void Thongtintaikhoan() {
        binding.tvEmail.setText(user.getEmail());
        binding.tvUsername.setText(user.getUsername());
        binding.tvMobile.setText(user.getMobile());
        binding.tvPassword.setText(user.getPass());
        Glide.with(getApplicationContext()).load(Utils.BASE_URL+"taikhoan/imageUser/"+user.getAvatar()).into(binding.inforAvt);

        binding.capnhatInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaili = binding.tvEmail.getText().toString();
                String usernamei = binding.tvUsername.getText().toString();
                String mobilei = binding.tvMobile.getText().toString();

                if (emaili.isEmpty()){
                    binding.tvEmail.setError("Không được để trống");
                }else if (usernamei.isEmpty()){
                    binding.tvUsername.setError("Không được để trống");
                }else if (mobilei.isEmpty()){
                    binding.tvMobile.setError("Không được để trống");
                }else{
                    int idusera = Utils.getUserId(getApplicationContext());
                    RequestBody iduser = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idusera));
                    RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emaili);
                    RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), usernamei);
                    RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobilei);

                    if(mUri!= null){
                        String realpath = RealPathUtil.getRealPath(InformationUser.this,mUri);
                        File file = new File(realpath);
                        // tạo đối tượng RequestBody từ file hình ảnh
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        // tạo đối tượng MultipartBody.Part từ requestFile với tên là "avatar"
                        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                        // gọi phương thức uploadImage của ApiService để gửi hình ảnh lên server
                        Log.e("avatar", String.valueOf(avatar));
                        updateUser(iduser,email,username,mobile, avatar);
                    }else {
                        String charToSend = "a";
                        RequestBody charRequestBody = RequestBody.create(MediaType.parse("text/plain"), charToSend);
                        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", charToSend, charRequestBody);
                        updateUser(iduser,email,username,mobile, avatar);
                    }
                }
            }
        });
    }

    private void updateUser(RequestBody iduser, RequestBody email, RequestBody username, RequestBody mobile, MultipartBody.Part avatar){
        // gửi yêu cầu đền API
        userViewModel.UpdateUser(iduser,email,username,mobile, avatar).observe(this,user -> {
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                if (user != null && user.isSuccess()) {
                    Toast.makeText(getApplicationContext(), "Thay đổi thành công", Toast.LENGTH_LONG).show();
                }else{
                    String message = user.getMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Doimatkhau() {
        Dialog dialog = new Dialog(InformationUser.this);
        DialogDoimatkhauBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.dialog_doimatkhau,null,false);
        dialog.setContentView(binding1.getRoot());
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        binding1.progressChangepass.setVisibility(View.GONE);
        binding1.btnDialogChangepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = binding1.edtOldpass.getText().toString();
                String newpass = binding1.edtNewpass.getText().toString();
                String confirmnewpass = binding1.edtConfirmnewpass.getText().toString();
                if (pass.isEmpty()){
                    binding1.edtOldpass.setError("Mật khẩu tối thiểu 6 ký tự");
                }else if (newpass.isEmpty()||newpass.length()<6){
                    binding1.edtNewpass.setError("Mật khẩu tối thiểu 6 ký tự");
                }else if (!newpass.equals(confirmnewpass)){
                    binding1.edtConfirmnewpass.setError("Mật khẩu xác nhận không chính xác");
                }else{
                    binding1.progressChangepass.setVisibility(View.VISIBLE);
                    int iduser = Utils.getUserId(getApplicationContext());
                    // gửi yêu cầu đền API
                    userViewModel.Doimatkhau(iduser,pass,newpass).observe(InformationUser.this,user -> {
                        if (!isInternetAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            binding1.progressChangepass.setVisibility(View.GONE);
                        }else {
                            if (user != null && user.isSuccess()) {
                                dialog.dismiss();
                                Thongtintaikhoan();
                                Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                            }else{
                                String message = user.getMessage();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                            binding1.progressChangepass.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        binding1.btnDialogHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setData() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }



    private void Chonhinhanh() {
        binding.inforAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();

            }
        });
        binding.infor1Avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();

            }
        });
    }

    private void onClickRequestPermission() {
        // neu version sdk(android 6) < M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openThuvien();
            return;
        }
        // nếu đã cho phép thì mở thư viện hình ảnh
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
            openThuvien();
        }
        // nếu chưa cho phép thì cho phép request
        else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE){
            // nếu người dùng chấp nhận truy cập
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openThuvien();
            }
        }

    }


    private void openThuvien() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Chọn hình ảnh"));
    }


    private void actionToolbar() {
        setSupportActionBar(binding.toolbarInforUser);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarInforUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_infor,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit_pass){
            Doimatkhau();
        }
        return super.onOptionsItemSelected(item);
    }


}