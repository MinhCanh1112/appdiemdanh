package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.data.model.RealPathUtil;
import com.example.appdiemdanh.data.model.Student;
import com.example.appdiemdanh.databinding.ActivityAddStudentBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.StudentViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddStudent extends AppCompatActivity {
    ActivityAddStudentBinding binding;
    StudentViewModel studentViewModel;
    private static final int MY_REQUEST_CODE = 1;

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
                           binding.picturehs.setImageBitmap(bitmap);
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_student);
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        actionToolbar();
        binding.progressadd.setVisibility(View.GONE);
        Chonhinhanh();
        uploadImage();
    }

    public void uploadImage(){
        // tự động hiển thị UID khi thêm học sinh và quét thẻ mới
        studentViewModel.showUID().observe(this,students -> {
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                if(students.isEmpty()){
                    Toast.makeText(AddStudent.this, "Vui lòng quét thẻ RFID", Toast.LENGTH_SHORT).show();
                }else {
                    for(Student item : students){
                        binding.edtuid.setText(item.getUid());
                    }
                }
            }
        });

        binding.them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uidi = binding.edtuid.getText().toString().trim();
                String namei = binding.edtHoTen.getText().toString().trim();
                String mshsi = binding.edtMshs.getText().toString();
                String mobilei = binding.edtSDT.getText().toString().trim();
                int i = binding.radioGroup.getCheckedRadioButtonId();
                if(uidi.isEmpty()){
                    binding.edtuid.setError("Không được để trống");
                }else if(namei.isEmpty()){
                    binding.edtHoTen.setError("Không được để trống");
                }else if(mshsi.isEmpty()){
                    binding.edtMshs.setError("Không được để trống");
                }else if (mobilei.length() != 10) {
                    binding.edtSDT.setError("Số điện thoại không chính xác");
                }else if (i == -1){
                    Toast.makeText(AddStudent.this, "Phải chọn giới tính", Toast.LENGTH_SHORT).show();
                }else {
                    binding.progressadd.setVisibility(View.VISIBLE);
                    RadioButton gioitinh = findViewById(i);
                    String genderi = gioitinh.getText().toString();
                    int idusera = Utils.getUserId(getApplicationContext());

                    Calendar calendar = Calendar.getInstance();
                    String weeka = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)-1);
                    String yeara = String.valueOf(calendar.get(Calendar.YEAR));

                    RequestBody iduser = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idusera));
                    RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"), uidi);
                    RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), namei);
                    RequestBody mshs = RequestBody.create(MediaType.parse("multipart/form-data"), mshsi);
                    RequestBody gender = RequestBody.create(MediaType.parse("multipart/form-data"), genderi);
                    RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobilei);
                    RequestBody week = RequestBody.create(MediaType.parse("multipart/form-data"), weeka);
                    RequestBody year = RequestBody.create(MediaType.parse("multipart/form-data"), yeara);

                    if(mUri != null) {
                        String realpath = RealPathUtil.getRealPath(AddStudent.this,mUri);
                        Log.e("a",realpath);
                        File file = new File(realpath);
                        // tạo đối tượng RequestBody từ file hình ảnh
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        // tạo đối tượng MultipartBody.Part từ requestFile với tên là "image"
                        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                        addStudent(iduser,name, uid, mshs, gender, mobile,week,year,avatar);
                    }else {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_people);
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        File file = saveBitmapToFile(bitmap);
                        // tạo đối tượng RequestBody từ file hình ảnh
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        // tạo đối tượng MultipartBody.Part từ requestFile với tên là "image"
                        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                        // gọi phương thức uploadImage của ApiService để gửi hình ảnh lên server
                        addStudent(iduser,name, uid, mshs, gender, mobile,week,year,avatar);
                    }
                }
            }
        });
    }

    private void addStudent(RequestBody iduser, RequestBody name, RequestBody uid, RequestBody mshs, RequestBody gender, RequestBody mobile,RequestBody week, RequestBody year,  MultipartBody.Part avatar){
        studentViewModel.addStudent(iduser,name, uid, mshs, gender, mobile,week,year,avatar)
                .observe(this,student -> {
            binding.progressadd.setVisibility(View.GONE);
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                if (student != null && student.isSuccess()) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    binding.progressadd.setVisibility(View.GONE);
                    finish();
                } else {
                    binding.progressadd.setVisibility(View.GONE);
                    String message = student.getMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

    private void Chonhinhanh() {
        binding.picturehs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();

            }
        });
        binding.picturehs1.setOnClickListener(new View.OnClickListener() {
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
        setSupportActionBar(binding.toolbarAddhs);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarAddhs.setNavigationOnClickListener(v -> finish());
    }
}