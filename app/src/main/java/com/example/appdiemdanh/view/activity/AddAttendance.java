package com.example.appdiemdanh.view.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;

import android.widget.TimePicker;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.databinding.ActivityAddAttendanceBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.AttendanceViewModel;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class AddAttendance extends AppCompatActivity {
    ActivityAddAttendanceBinding binding;
    AttendanceViewModel attendanceViewModel;
    private Calendar currentTime = Calendar.getInstance();
    private TimePickerDialog timePickerDialog;

    // Tạo biến để lưu các ngày đã chọn
    private String selectdate = "";
    //private ArrayList<String> date = new ArrayList<>();
    // Khởi tạo một mảng chứa các tên thứ trong tuần.
    final String[] weekdays = {"Thứ 2","Thứ 3","Thứ 4", "Thứ 5","Thứ 6","Thứ 7","Chủ nhật"};
    // Khởi tạo một mảng chứa các giá trị ngày tương ứng với các thứ trong tuần.
    final String[] dates = new String[7];
    // Khởi tạo một mảng chứa các trạng thái checked của các nút.
    boolean[] checkedItems = {false, false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_attendance);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        ActionToolbar();
        DateNow();
        Timediemdanh();

        // Tính toán các ngày ứng với các thứ trong tuần.
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < weekdays.length; i++) {
            int dayOfWeek = i + 2; // Thứ 2 là 2, thứ 3 là 3, và cứ tiếp tục như vậy.
            if (dayOfWeek > 7) {
                dayOfWeek -= 7; // Nếu thứ lớn hơn 7 (chủ nhật), trừ đi 7 để quay vòng lại từ thứ 2.
            }
            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek); // Thiết lập ngày hiện tại của Calendar thành thứ trong tuần tương ứng.
            Date date = calendar.getTime(); // Lấy ngày từ Calendar.
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày.
            dates[i] = dateFormat.format(date); // Lưu giá trị ngày vào mảng.
        }

        dialogdate();
        postTimedd();
    }

    private void dialogdate(){
        binding.selectCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo đối tượng AlertDialog.Builder để xây dựng hộp thoại.
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAttendance.this);
                // Thiết lập tiêu đề cho hộp thoại.
                builder.setTitle("Chọn ngày điểm danh");
                // Thiết lập một danh sách chứa tên thứ và các nút được đánh dấu tương ứng.
                builder.setMultiChoiceItems(weekdays, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    // Cập nhật trạng thái checked của nút khi người dùng chọn.
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                });
                // Thêm các nút "OK" và "Cancel" cho hộp thoại.
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng hộp thoại và xử lý các ngày đã chọn.
                        if(selectdate !=null){
                            selectdate ="";
                            binding.txtdate.setText(selectdate);
                        }
                        processSelectedDays();
                        checkedItems = new boolean[]{false, false, false, false, false, false, false};

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng hộp thoại nếu người dùng hủy bỏ.
                        checkedItems = new boolean[]{false, false, false, false, false, false, false};

                        dialog.dismiss();
                    }
                });
                // Hiển thị hộp thoại khi người dùng nhấn nút.
                builder.show();
            }
        });
    }


    private void processSelectedDays() {
        // Lặp lại mảng checkedItems và kiểm tra các ngày đã chọn.
        for (int i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i]) {
                // Ngày đã được chọn, sử dụng mảng dates để lấy ngày tương ứng.
                String selectedDate = dates[i];
                Log.d("MainActivity", "Selected date: " + selectedDate);
                // Xử lý ngày đã chọn ở đây.
                // Thêm ngày đã chọn vào biến selectedDays
                selectdate += selectedDate + " ";
            }
        }
        // Hiển thị các ngày đã chọn trên TextView
        binding.txtdate.setText(selectdate);
    }


    private void postTimedd() {
        binding.btnStartdd.setOnClickListener(v -> {
            String timestart = binding.edtGiostart.getText().toString();
            String timevao = binding.edtGiovao.getText().toString();
            String timera = binding.edtGiora.getText().toString();
            String date = binding.txtdate.getText().toString();
            if(timevao.isEmpty() || timera.isEmpty() || timestart.isEmpty() ) {
                Toast.makeText(AddAttendance.this, "Không được để trống", Toast.LENGTH_SHORT).show();
            }else if(selectdate.equals("")){
                Toast.makeText(AddAttendance.this, "Chọn ngày điểm danh", Toast.LENGTH_SHORT).show();
            }else {
                int iduser = Utils.getUserId(AddAttendance.this);

                Log.e("a",date);
                attendanceViewModel.addAttendanceTime(iduser,date,timestart,timevao,timera)
                        .observe(this,dateTimediemdanh -> {
                    if (!LoginUser.isInternetAvailable(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "Không có kết nối Internet. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }else {
                        if (dateTimediemdanh != null && dateTimediemdanh.isSuccess()) {
                            Toast.makeText(AddAttendance.this, "Thiết lập điểm danh thành công", Toast.LENGTH_SHORT).show();
                            binding.edtGiovao.setText("");
                            binding.edtGiora.setText("");
                            binding.edtGiostart.setText("");
                            binding.txtdate.setText("");
                            selectdate = "";
                        } else {
                            String message = dateTimediemdanh.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void DateNow() {
        // Lấy ngày và tháng hiện tại
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // tháng bắt đầu từ 0 nên cần cộng thêm 1
        int year = calendar.get(Calendar.YEAR); // tháng bắt đầu từ 0 nên cần cộng thêm 1
        // Hiển thị ngày hiện tại trên TextView
        binding.edtNgayhientai.setText(year+"-"+ month + "-" + day);
    }

    private void Timediemdanh() {
        binding.edtGiovao.setOnClickListener(v -> selectTime(binding.edtGiovao));
        binding.edtGiostart.setOnClickListener(v -> selectTime(binding.edtGiostart));
        binding.edtGiora.setOnClickListener(v -> selectTime(binding.edtGiora));
    }


    private void selectTime(EditText edt) {
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(AddAttendance.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                currentTime.set(Calendar.HOUR_OF_DAY,hour);
                currentTime.set(Calendar.MINUTE,minute);
                String myFormat = "HH:mm:00";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                edt.setText(dateFormat.format(currentTime.getTime()));
            }
        }, hour,minute,true);
         timePickerDialog.setTitle("Chọn giờ");
         timePickerDialog.show();
    }

    private void ActionToolbar() {
        setSupportActionBar(binding.toolbarAdddiemdanh);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbarAdddiemdanh.setNavigationOnClickListener(v -> finish());
    }


}