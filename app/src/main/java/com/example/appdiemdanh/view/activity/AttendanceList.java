package com.example.appdiemdanh.view.activity;

import static com.example.appdiemdanh.view.activity.LoginUser.isInternetAvailable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.AttendanceListAdapter;
import com.example.appdiemdanh.data.model.Attendance;
import com.example.appdiemdanh.data.model.Class;
import com.example.appdiemdanh.data.model.DateTimediemdanh;
import com.example.appdiemdanh.databinding.ActivityAttendanceListBinding;
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
import java.util.Objects;

public class AttendanceList extends AppCompatActivity {
    ActivityAttendanceListBinding binding;
    AttendanceViewModel attendanceViewModel;
    private List<Attendance> diemdanhList = new ArrayList<>();
    private AttendanceListAdapter diemdanhAdapter;
    Class classThongke;
    DateTimediemdanh dateDiemdanh;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_SAVE_FILE = 2;
    int iduser,idsetupdd,idngaydd;
    String ngaydd,timevao,timera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_attendance_list);
        setData();
        TabHost();
        actionToolbar();
        getDatadiemdanhvao();
        getDatadiemdanhra();
        getThongke();

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
        String FILE_NAME = ngaydd +"-"+timevao +"-"+ timera + ".xlsx";
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
        String lop = classThongke.getLop();
        int siso = classThongke.getSiso();
        int dadiemdanh = classThongke.getDadiemdanh();
        int vaotre = classThongke.getVaotre();
        int vang = classThongke.getVang();
        int vesom = classThongke.getVesom();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowIndex1 = 0;
        Row headerRow1 = sheet.createRow(rowIndex1++);
        headerRow1.createCell(0).setCellValue("Ngày điểm danh");
        headerRow1.createCell(1).setCellValue("Giờ điểm danh");
        Row row1 = sheet.createRow(rowIndex1++);
        row1.createCell(0).setCellValue(ngaydd);
        row1.createCell(1).setCellValue(timevao+"-"+timera);

        int rowIndex2 = 3;
        Row row2 = sheet.createRow(rowIndex2++);
        row2.createCell(0).setCellValue("Lớp");
        row2.createCell(1).setCellValue(lop);
        int rowIndex3 = 4;
        Row row3 = sheet.createRow(rowIndex3++);
        row3.createCell(0).setCellValue("Sĩ số");
        row3.createCell(1).setCellValue(siso);
        int rowIndex4 = 5;
        Row row4 = sheet.createRow(rowIndex4++);
        row4.createCell(0).setCellValue("Đã điểm danh");
        row4.createCell(1).setCellValue(dadiemdanh);
        int rowIndex5 = 6;
        Row row5 = sheet.createRow(rowIndex5++);
        row5.createCell(0).setCellValue("Vào trễ");
        row5.createCell(1).setCellValue(vaotre);
        int rowIndex6 = 7;
        Row row6 = sheet.createRow(rowIndex6++);
        row6.createCell(0).setCellValue("Vắng");
        row6.createCell(1).setCellValue(vang);
        int rowIndex7 = 8;
        Row row7 = sheet.createRow(rowIndex7++);
        row7.createCell(0).setCellValue("Về sớm");
        row7.createCell(1).setCellValue(vesom);

        int rowIndex = 10;
        Row headerRow = sheet.createRow(rowIndex++);
        headerRow.createCell(0).setCellValue("Mã UID");
        headerRow.createCell(1).setCellValue("Họ tên");
        headerRow.createCell(2).setCellValue("Mã số học sinh");
        headerRow.createCell(3).setCellValue("Ngày điểm danh");
        headerRow.createCell(4).setCellValue("Giờ điểm danh");
        headerRow.createCell(5).setCellValue("Trạng thái");
        headerRow.createCell(6).setCellValue("Lý do");

        // TODO: Thêm các cột khác
        for (Attendance data : diemdanhList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(data.getUid());
            row.createCell(1).setCellValue(data.getName());
            row.createCell(2).setCellValue(data.getMshs());
            row.createCell(3).setCellValue(data.getDate());
            row.createCell(4).setCellValue(data.getTime());
            row.createCell(5).setCellValue(data.getTrangthai());
            row.createCell(6).setCellValue(data.getLydo());
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


    @SuppressLint("SetTextI18n")
    public void getThongke(){
        attendanceViewModel.getThongkeTheoBuoi(iduser,idsetupdd).observe(this,thongke -> {
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                classThongke = thongke;
                binding.txttenlop.setText("Lớp: "+classThongke.getLop());
                binding.txtsiso.setText("Sĩ số: "+classThongke.getSiso() +"");
            }

        });

    }



    public void deleteDiemdanh() {
        attendanceViewModel.deleteDiemdanh(idsetupdd).observe(this,attendance -> {
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
                getDatadiemdanhvao();
                getDatadiemdanhra();
            }
        });
    }

    public void updateLydo(int id,String lydo) {
        attendanceViewModel.updateLydo(id,lydo).observe(this,attendance -> {
            if (!isInternetAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                getDatadiemdanhvao();
                getDatadiemdanhra();
            }
        });
    }

    private void getDatadiemdanhra() {
        attendanceViewModel.getDiemdanhra(iduser,idngaydd,idsetupdd).observe(this,attendanceListOut -> {
            diemdanhList.addAll(attendanceListOut);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recycRa.setLayoutManager(layoutManager);
            //Set list vào adapter
            diemdanhAdapter = new AttendanceListAdapter(AttendanceList.this, attendanceListOut);
            binding.recycRa.setAdapter(diemdanhAdapter);
            diemdanhAdapter.notifyDataSetChanged();
        });

    }

    private void getDatadiemdanhvao() {
        attendanceViewModel.getDiemdanhvao(iduser,idngaydd,idsetupdd).observe(this,attendanceListIn -> {
            diemdanhList.addAll(attendanceListIn);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recycVao.setLayoutManager(layoutManager);
            //Set list vào adapter
            diemdanhAdapter = new AttendanceListAdapter(AttendanceList.this, attendanceListIn);
            binding.recycVao.setAdapter(diemdanhAdapter);
            diemdanhAdapter.notifyDataSetChanged();
        });
    }

    private void actionToolbar() {
        setSupportActionBar(binding.toolbarDsdd);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarDsdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setData() {

        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        Intent intent = getIntent();
        dateDiemdanh = (DateTimediemdanh) intent.getSerializableExtra("dataDateDiemdanh");

        iduser = Utils.getUserId(getApplicationContext());
        idngaydd = dateDiemdanh.getIdngaydd();
        idsetupdd = dateDiemdanh.getIdsetupdd();
        ngaydd = dateDiemdanh.getDate();
        timevao = dateDiemdanh.getTimevao();
        timera = dateDiemdanh.getTimera();

        binding.txttimevao.setText("Giờ vào: "+timevao);
        binding.txttimera.setText("Giờ ra: "+timera);
    }

    private void TabHost() {
        // xu ly TabHost
        binding.mytab .setup();
        // khai bao cac tab con (TabSpec)
        TabHost.TabSpec spec1,spec2;
        //tab1
        spec1 = binding.mytab .newTabSpec("t1"); // tao moi tab
        spec1.setContent(R.id.tab1); // tham chieu id cho tab 1
        //spec1.setIndicator("", getResources().getDrawable(R.drawable.ic_people));
        spec1.setIndicator("Điểm danh vào");
        binding.mytab .addTab(spec1);
        //tab2
        spec2 = binding.mytab .newTabSpec("t2"); // tao moi tab
        spec2.setContent(R.id.tab2); // tham chieu id cho tab 1
        //spec2.setIndicator("", getResources().getDrawable(R.drawable.ic_people));
        spec2.setIndicator("Điểm danh ra");
        binding.mytab .addTab(spec2);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listdiemdanh,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.xuatfileex) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(AttendanceList.this);
            dialog.setMessage("Bạn có muốn xuất file ngày điểm danh "+ngaydd+"/"+timevao+"-" +timera+" không?");
            dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkAndRequestWriteExternalStoragePermission();
                }
            });
            dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }
        else if(item.getItemId() == R.id.clear_list){
            deleteDiemdanh();
        }
        else if(item.getItemId() == R.id.refreshdanhsach){
            getDatadiemdanhvao();
        }else if(item.getItemId() == R.id.thongkedd){
            Intent intent = new Intent(AttendanceList.this, AttendanceStatistics.class);
            intent.putExtra("dataThongke",classThongke);
            intent.putExtra("dataDateDiemdanh",dateDiemdanh);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}