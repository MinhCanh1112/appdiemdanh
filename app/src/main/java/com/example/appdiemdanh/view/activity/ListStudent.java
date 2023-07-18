package com.example.appdiemdanh.view.activity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.StudentAdapter;
import com.example.appdiemdanh.data.model.Student;
import com.example.appdiemdanh.databinding.ActivityListStudentBinding;
import com.example.appdiemdanh.databinding.DialogUpdateclassBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.StudentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListStudent extends AppCompatActivity {
    ActivityListStudentBinding binding;
    StudentViewModel studentViewModel;
    DialogUpdateclassBinding bddialog;
    private List<Student> studentList;
    private StudentAdapter studentAdapter;
    boolean onStop = false;
    int iduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_list_student);

        anhxa();
        getData();
        actionToolbar();
        themhocsinh();

        binding.searchView.clearFocus();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<Student> filteredList = new ArrayList<>();
        for(Student student : studentList){
            if(student.getName().toLowerCase().contains(text.toLowerCase())
                        ||student.getUid().toLowerCase().contains(text.toLowerCase())
                        ||student.getMshs().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(student);
            }
        }

        if(filteredList.isEmpty()){
//            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }else{
            studentAdapter.setFilteredList(filteredList);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(onStop) getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        onStop = true;
    }

    private void themhocsinh() {
        binding.btnThemhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListStudent.this, AddStudent.class);
                startActivity(intent);
            }
        });
    }

    public void getData() {
        studentViewModel.liststudent(iduser).observe(this,students -> {
            studentList = students;
            if (!LoginUser.isInternetAvailable(this)) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else{
                studentAdapter = new StudentAdapter(ListStudent.this, studentList);
                binding.recyclerview.setAdapter(studentAdapter);
                getLopSiso();
                studentAdapter.notifyDataSetChanged();
            }
        });

    }

    public void deleteStudent( int id ) {
        studentViewModel.deleteStudent(id,iduser).observe(this,student -> {
            if (!LoginUser.isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
                getData();
            }
        });
    }

    public void getLopSiso(){
        studentViewModel.lopsiso(iduser).observe(this,tenlop -> {
            binding.tvlop.setText(tenlop.getLop());
            binding.tvsiso.setText(tenlop.getSiso() +"");
        });

    }

    public void DialogLop(){
        Dialog dialog = new Dialog(this);
        bddialog = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialog_updateclass, null, false);
        dialog.setContentView(bddialog.getRoot());
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        bddialog.progressUpdateClass.setVisibility(View.GONE);
        bddialog.btnDialogUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lop = bddialog.edtDialogTenlop.getText().toString().trim();
                if(lop.isEmpty()){
                    bddialog.edtDialogTenlop.setError("Không được để trống");
                }else {
                    bddialog.progressUpdateClass.setVisibility(View.VISIBLE);
                    studentViewModel.tenlop(iduser,lop).observe(ListStudent.this,tenlop -> {
                        if (!LoginUser.isInternetAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }else {
                            if (tenlop != null && tenlop.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                bddialog.progressUpdateClass.setVisibility(View.GONE);
                                dialog.dismiss();
                                getData();
                            } else {
                                bddialog.progressUpdateClass.setVisibility(View.GONE);
                                String message = tenlop.getMessage();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        bddialog.btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void anhxa() {
        binding.recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(layoutManager);
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        iduser = Utils.getUserId(getApplicationContext());

    }

    private void actionToolbar() {
        setSupportActionBar(binding.toolbarDshs);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarDshs.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hocsinh,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_tenlop) {
            DialogLop();
        }
        return super.onOptionsItemSelected(item);
    }

}
