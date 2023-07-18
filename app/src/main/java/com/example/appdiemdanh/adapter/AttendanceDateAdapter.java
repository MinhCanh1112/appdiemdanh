package com.example.appdiemdanh.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.databinding.ItemAttendanceDateBinding;
import com.example.appdiemdanh.view.activity.AttendanceList;
import com.example.appdiemdanh.view.activity.AttendanceDate;
import com.example.appdiemdanh.data.model.DateTimediemdanh;

import java.util.List;

public class AttendanceDateAdapter extends RecyclerView.Adapter<AttendanceDateAdapter.MyViewHolder> {
    AttendanceDate context;
    List<DateTimediemdanh> array;

    public AttendanceDateAdapter(AttendanceDate context, List<DateTimediemdanh> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public AttendanceDateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set layout cho adapter de hien thi len list
        ItemAttendanceDateBinding dateBinding = ItemAttendanceDateBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyViewHolder(dateBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceDateAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setBinding(array.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AttendanceList.class);
                intent.putExtra("dataDateDiemdanh",array.get(position));
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                XacNhanXoa(array.get(position).getIdngaydd(),array.get(position).getIdsetupdd());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemAttendanceDateBinding binding;
        public MyViewHolder(ItemAttendanceDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void setBinding(DateTimediemdanh dateTimediemdanh){
            binding.setAttendancedate(dateTimediemdanh);
        }
    }

    private void XacNhanXoa(int id,int idsetupdd){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Bạn có muốn xoá ngày điểm danh này không?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.deleteNgaydd(id,idsetupdd);
//                context.deleteAllDiemdanh(idsetupdd);

            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
}
