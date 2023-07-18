package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.appdiemdanh.R;
import com.example.appdiemdanh.data.model.RealPathUtil;
import com.example.appdiemdanh.data.model.Student;
import com.example.appdiemdanh.data.api.ApiHocsinh;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.ActivityUpdateStudentBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.StudentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateStudent extends AppCompatActivity {
    private ActivityUpdateStudentBinding binding;
    private StudentViewModel studentViewModel;
    private static final int MY_REQUEST_CODE = 10;
    private Student student;

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
                            binding.updateAvtHs.setImageBitmap(bitmap);
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update_student);
        anhxa();
        actionToolbar();
        setData();
        Chonhinhanh();
        updatehs();
    }

    private void setData(){
        Intent intent = getIntent();
        student = (Student) intent.getSerializableExtra("dataHocsinh");
        binding.progressUpHs.setVisibility(View.GONE);
        binding.edtUpdateUid.setText(student.getUid());
        binding.edtUpdateName.setText(student.getName());
        binding.edtUpdateMshs.setText(student.getMshs());
        binding.edtUpdateSdt.setText(student.getMobile());
        String urlAvatar = Utils.BASE_URL+"hocsinh/image/"+student.getAvatar();
        Glide.with(getApplicationContext()).load(urlAvatar).into(binding.updateAvtHs);
        if(student.getGender().equals("Nam")){
            binding.radioButtonUpdateNam.setChecked(true);
        }else if(student.getGender().equals("Nữ")){
            binding.radioButtonUpdateNu.setChecked(true);
        }else {
            Toast.makeText(this, "Chọn giới tính", Toast.LENGTH_SHORT).show();
        }
    }

    private void anhxa() {
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        binding.progressUpHs.setVisibility(View.GONE);

    }

    public void updatehs() {
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uidi = binding.edtUpdateUid.getText().toString().trim();
                String namei = binding.edtUpdateName.getText().toString().trim();
                String mshsi = binding.edtUpdateMshs.getText().toString();
                String mobilei = binding.edtUpdateSdt.getText().toString().trim();
                int i = binding.radioGroupUpdate.getCheckedRadioButtonId();
                if(uidi.isEmpty()){
                    binding.edtUpdateUid.setError("Không được để trống");
                }else if(namei.isEmpty()){
                    binding.edtUpdateName.setError("Không được để trống");
                }else if(mshsi.isEmpty()){
                    binding.edtUpdateMshs.setError("Không được để trống");
                }else if(mobilei.isEmpty()){
                    binding.edtUpdateSdt.setError("Không được để trống");
                } else if (mobilei.length() != 10) {
                    binding.edtUpdateSdt.setError("Số điện thoại không chính xác");
                }else if (i == -1){
                    Toast.makeText(getApplicationContext(), "Phải chọn giới tính", Toast.LENGTH_SHORT).show();
                }else {
                    binding.progressUpHs.setVisibility(View.VISIBLE);
                    int idi = student.getId();
                    RadioButton radioButton1 = (RadioButton) findViewById(i);
                    String genderi = radioButton1.getText().toString();
                    int iduseri = Utils.getUserId(getApplicationContext());

                    Calendar calendar = Calendar.getInstance();
                    String weeka = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)-1);
                    String yeara = String.valueOf(calendar.get(Calendar.YEAR));

                    RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idi));
                    RequestBody iduser = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(iduseri));
                    RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"), uidi);
                    RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), namei);
                    RequestBody mshs = RequestBody.create(MediaType.parse("multipart/form-data"), mshsi);
                    RequestBody gender = RequestBody.create(MediaType.parse("multipart/form-data"), genderi);
                    RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobilei);
                    RequestBody week = RequestBody.create(MediaType.parse("multipart/form-data"), weeka);
                    RequestBody year = RequestBody.create(MediaType.parse("multipart/form-data"), yeara);

                    if(mUri != null){
                        String realpath = RealPathUtil.getRealPath(UpdateStudent.this,mUri);
                        File file = new File(realpath);
                        // tạo đối tượng RequestBody từ file hình ảnh
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        // tạo đối tượng MultipartBody.Part từ requestFile với tên là "image"
                        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                        updateStudent(id,iduser,name,uid,mshs,gender,mobile,week,year,avatar);
                    }else {
                        String charToSend = "a";
                        RequestBody charRequestBody = RequestBody.create(MediaType.parse("text/plain"), charToSend);
                        MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", charToSend, charRequestBody);
                        updateStudent(id,iduser,name,uid,mshs,gender,mobile,week,year,avatar);
                    }
                }
            }
        });
    }

    private void updateStudent(RequestBody id,RequestBody iduser, RequestBody name, RequestBody uid, RequestBody mshs, RequestBody gender, RequestBody mobile,RequestBody week, RequestBody year, MultipartBody.Part avatar){
        studentViewModel.updateStudent(id,iduser,name,uid,mshs,gender,mobile,week,year,avatar).observe(this,student -> {
            binding.progressUpHs.setVisibility(View.GONE);
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                if (student != null && student.isSuccess()) {
                    binding.progressUpHs.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    binding.progressUpHs.setVisibility(View.GONE);
                    String message = student.getMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void Chonhinhanh() {
        binding.updateAvtHs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });
        binding.update1AvtHs.setOnClickListener(new View.OnClickListener() {
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
        setSupportActionBar(binding.toolbarUphs);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarUphs.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}