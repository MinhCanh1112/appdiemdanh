package com.example.appdiemdanh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.view.activity.StatisticsWeekOfEachStudent;
import com.example.appdiemdanh.data.model.DateTimediemdanh;
import com.example.appdiemdanh.data.model.StatisWeek;

import java.util.List;

public class StatisWeekHsAdapter extends RecyclerView.Adapter<StatisWeekHsAdapter.MyViewHolder> {
    StatisticsWeekOfEachStudent context;
    List<StatisWeek> list;

    public StatisWeekHsAdapter(StatisticsWeekOfEachStudent context, List<StatisWeek> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StatisWeekHsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_thongketuan_hs, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisWeekHsAdapter.MyViewHolder holder, int position) {
        DateTimediemdanh inforStatisHs = list.get(position);
        holder.date.setText(inforStatisHs.getDate());
        holder.timevao.setText(inforStatisHs.getTimevao());
        holder.timera.setText(inforStatisHs.getTimera());
        holder.trangthai.setText(inforStatisHs.getTrangthai());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,timevao,timera,trangthai;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_datetk);
            timevao = itemView.findViewById(R.id.tv_timevaotk);
            timera = itemView.findViewById(R.id.tv_timeratk);
            trangthai = itemView.findViewById(R.id.tv_trangthaitk);
        }
    }
}
