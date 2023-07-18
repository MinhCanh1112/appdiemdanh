package com.example.appdiemdanh.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdiemdanh.R;
import com.example.appdiemdanh.view.activity.StatisticsWeek;
import com.example.appdiemdanh.view.activity.StatisticsWeekOfEachStudent;
import com.example.appdiemdanh.data.model.StatisWeek;
import com.example.appdiemdanh.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatisWeekAdapter extends RecyclerView.Adapter<StatisWeekAdapter.MyViewHolder> {
    StatisticsWeek context;
    List<StatisWeek> statisWeeks;

    public StatisWeekAdapter(StatisticsWeek context, List<StatisWeek> statisWeeks) {
        this.context = context;
        this.statisWeeks = statisWeeks;
    }

    public void setFilteredList(List<StatisWeek> filteredList){
        this.statisWeeks = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatisWeekAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_week_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisWeekAdapter.MyViewHolder holder, int position) {
        StatisWeek statisWeek = statisWeeks.get(position);
        holder.uid.setText(statisWeek.getUid());
        holder.name.setText(statisWeek.getName());
        holder.mshs.setText(statisWeek.getMshs());
        holder.vaotre.setText(statisWeek.getVaotre() +"");
        holder.vang.setText(statisWeek.getVang()+"");
        holder.vesom.setText(statisWeek.getVesom()+"");
        Glide.with(context).load(Utils.BASE_URL+"hocsinh/image/"+statisWeek.getAvatar()).into(holder.pic);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, StatisticsWeekOfEachStudent.class);
                i.putExtra("dataThongke",statisWeek);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return statisWeeks != null ? statisWeeks.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView uid,name,mshs,vaotre,vang,vesom;
        CircleImageView pic;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            uid = itemView.findViewById(R.id.uidw);
            name = itemView.findViewById(R.id.namew);
            mshs = itemView.findViewById(R.id.mshsw);
            vaotre = itemView.findViewById(R.id.trew);
            vang = itemView.findViewById(R.id.vangw);
            vesom = itemView.findViewById(R.id.vesomw);
            pic = itemView.findViewById(R.id.pichsw);
        }
    }
}
