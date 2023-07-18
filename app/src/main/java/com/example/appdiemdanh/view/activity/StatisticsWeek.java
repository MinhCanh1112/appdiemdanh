package com.example.appdiemdanh.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.StatisWeekAdapter;
import com.example.appdiemdanh.data.model.StatisWeek;
import com.example.appdiemdanh.data.api.ApiDiemdanh;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.ActivityStatisticsWeekBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.AttendanceViewModel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsWeek extends AppCompatActivity {
    ActivityStatisticsWeekBinding binding;
    AttendanceViewModel attendanceViewModel;
    private StatisWeekAdapter statisWeekAdapter;
    private List<StatisWeek> statisWeekList;
    String week = "";
    String year = "";

    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 3;
    private static final int REQUEST_SAVE_FILE = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_statistics_week);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        actionToolbar();
        week = getIntent().getStringExtra("week");
        year = getIntent().getStringExtra("year");
        getThongkeTuan();
        getSolandd();
        EventExportToExcel();

        binding.searchViewThongke.clearFocus();
        binding.searchViewThongke.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


    private void getSolandd(){
        int iduser = Utils.getUserId(StatisticsWeek.this);
        attendanceViewModel.getSolandd(iduser,week,year).observe(StatisticsWeek.this,statisWeek -> {
            binding.tvCount.setText("Số buổi điểm danh: "+statisWeek.getSolandd()+" lần");
        });
    }

    private void getThongkeTuan() {
        binding.tvWeek.setText("Tuần : "+week +", Năm : "+year);
        int iduser = Utils.getUserId(StatisticsWeek.this);
        attendanceViewModel.getThongkeTuan(iduser,week,year).observe(StatisticsWeek.this,statisWeeks -> {
            if (!LoginUser.isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                statisWeekList = statisWeeks;
                LinearLayoutManager layoutManager = new LinearLayoutManager(StatisticsWeek.this);
                binding.recycWeek.setLayoutManager(layoutManager);
                statisWeekAdapter = new StatisWeekAdapter(StatisticsWeek.this,statisWeekList);
                binding.recycWeek.setAdapter(statisWeekAdapter);
                statisWeekAdapter.notifyDataSetChanged();
            }
        });
    }

    private void filterList(String text) {
        List<StatisWeek> filteredList = new ArrayList<>();
        for(StatisWeek statisWeek : statisWeekList){
            if(statisWeek.getName().toLowerCase().contains(text.toLowerCase())
                    ||statisWeek.getUid().toLowerCase().contains(text.toLowerCase())
                    ||statisWeek.getMshs().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(statisWeek);
            }
        }

        if(filteredList.isEmpty()){
//            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }else{
            statisWeekAdapter.setFilteredList(filteredList);
        }
    }

    private void EventExportToExcel() {
        binding.xuatfileexall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsWeek.this);
                builder.setMessage("Bạn có muốn xuất file excel không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkAndRequestWriteExternalStoragePermission();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    private void checkAndRequestWriteExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
        } else {
            openSaveFileDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openSaveFileDialog();
            }
        }
    }

    private void openSaveFileDialog() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String FILE_NAME ="Tuần:"+week + "-"+ year+".xlsx";
        intent.putExtra(Intent.EXTRA_TITLE,  FILE_NAME);
        startActivityForResult(intent, REQUEST_SAVE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SAVE_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                saveExcelFile(data.getData());
            }
        }
    }

    private void saveExcelFile(Uri uri) {
        try {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            String solandd = binding.tvCount.getText().toString().trim();

            int rowIndex1 = 0;
            Row row1 = sheet.createRow(rowIndex1++);
            row1.createCell(0).setCellValue("Tuần");
            row1.createCell(1).setCellValue(week);

            int rowIndex2 = 1;
            Row row2 = sheet.createRow(rowIndex2++);
            row2.createCell(0).setCellValue("Năm");
            row2.createCell(1).setCellValue(year);

            int rowIndex3 = 2;
            Row row3 = sheet.createRow(rowIndex3++);
            row3.createCell(0).setCellValue(solandd);
            int rowIndex = 5;
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("Mã UID");
            headerRow.createCell(1).setCellValue("Họ tên");
            headerRow.createCell(2).setCellValue("Mã số học sinh");
            headerRow.createCell(3).setCellValue("Số lần đi trễ");
            headerRow.createCell(4).setCellValue("Số lần vắng");
            headerRow.createCell(5).setCellValue("Số lần về sớm");
            // TODO: Thêm các cột khác
            for (StatisWeek data : statisWeekList) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(data.getUid());
                row.createCell(1).setCellValue(data.getName());
                row.createCell(2).setCellValue(data.getMshs());
                row.createCell(3).setCellValue(data.getVaotre());
                row.createCell(4).setCellValue(data.getVang());
                row.createCell(5).setCellValue(data.getVesom());

            }


            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();

            Toast.makeText(this, "Lưu file thành công", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void actionToolbar() {
        setSupportActionBar(binding.toolbarStatisweek);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarStatisweek.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_thongketuan,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refreshdanhsachtkt) {
            getThongkeTuan();
            getSolandd();
        }
        return super.onOptionsItemSelected(item);
    }

}