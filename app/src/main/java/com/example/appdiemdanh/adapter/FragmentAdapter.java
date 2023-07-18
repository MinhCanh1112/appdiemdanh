package com.example.appdiemdanh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.databinding.ItemAttendanceDatetimeBinding;
import com.example.appdiemdanh.view.activity.AttendanceInfor;
import com.example.appdiemdanh.data.model.DateTimediemdanh;

import java.util.List;

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.ViewHolder> {
    private List<DateTimediemdanh> diemdanhList;
    private Context context;

    public FragmentAdapter() {
    }

    public void setData(List<DateTimediemdanh> userList, Context ct) {
        this.diemdanhList = userList;
        this.context = ct;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAttendanceDatetimeBinding attendanceDatetimeBinding = ItemAttendanceDatetimeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(attendanceDatetimeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setBinding(diemdanhList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AttendanceInfor.class);
                //Truyền toàn bộ data sang
                intent.putExtra("dateTimediemdanh", diemdanhList.get(position));
                context.startActivity(intent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return diemdanhList != null ? diemdanhList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAttendanceDatetimeBinding binding;
        public ViewHolder(ItemAttendanceDatetimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void setBinding(DateTimediemdanh dateTimediemdanh){
            binding.setDatetimeattendance(dateTimediemdanh);
        }
    }
}

