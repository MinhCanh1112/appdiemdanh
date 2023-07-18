package com.example.appdiemdanh.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.view.activity.ChooseWeek;
import com.example.appdiemdanh.view.activity.StatisticsWeek;
import com.example.appdiemdanh.data.model.StatisWeek;

import java.util.List;

public class ChooseWeekAdapter extends RecyclerView.Adapter<ChooseWeekAdapter.MyHolderView> {
    ChooseWeek context;
    List<StatisWeek> statisWeekList;

    public ChooseWeekAdapter(ChooseWeek context, List<StatisWeek> statisWeekList) {
        this.context = context;
        this.statisWeekList = statisWeekList;
    }

    @NonNull
    @Override
    public ChooseWeekAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choose_week,parent,false);
        return new ChooseWeekAdapter.MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseWeekAdapter.MyHolderView holder, int position) {
        StatisWeek statisWeek = statisWeekList.get(position);
        holder.chooseweek.setText("Tuần : "+statisWeek.getWeek());
        holder.chooseyear.setText("Năm : "+statisWeek.getYear());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatisticsWeek.class);
                intent.putExtra("week",statisWeek.getWeek());
                intent.putExtra("year",statisWeek.getYear());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return statisWeekList != null ? statisWeekList.size() : 0;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        TextView chooseweek,chooseyear;
        View view;
        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            chooseweek = itemView.findViewById(R.id.chooseweek);
            chooseyear = itemView.findViewById(R.id.chooseyear);
        }
    }
}
