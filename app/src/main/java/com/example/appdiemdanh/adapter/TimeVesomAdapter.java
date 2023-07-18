package com.example.appdiemdanh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.databinding.ItemEndsomBinding;
import com.example.appdiemdanh.view.activity.AttendanceInfor;
import com.example.appdiemdanh.data.model.DateTimediemdanh;

import java.util.List;

public class TimeVesomAdapter extends RecyclerView.Adapter<TimeVesomAdapter.MyViewHolder> {
    AttendanceInfor context;
    List<DateTimediemdanh> array;

    public TimeVesomAdapter(AttendanceInfor context, List<DateTimediemdanh> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public TimeVesomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set layout cho adapter de hien thi len list
        ItemEndsomBinding itemEndsomBinding = ItemEndsomBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyViewHolder(itemEndsomBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeVesomAdapter.MyViewHolder holder, int position) {
        holder.setBinding(array.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.huyvesom();
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemEndsomBinding binding;

        public MyViewHolder(ItemEndsomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(DateTimediemdanh dateTimediemdanh){
            binding.setEndsom(dateTimediemdanh);

        }
    }


}
