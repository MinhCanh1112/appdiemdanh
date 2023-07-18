package com.example.appdiemdanh.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdiemdanh.R;
import com.example.appdiemdanh.databinding.DialogSuaGhichuBinding;
import com.example.appdiemdanh.databinding.ItemAttendanceListBinding;
import com.example.appdiemdanh.view.activity.AttendanceList;
import com.example.appdiemdanh.data.model.Attendance;
import com.example.appdiemdanh.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.MyViewHolder>{
    AttendanceList context;
    List<Attendance> array;

    public AttendanceListAdapter(AttendanceList context, List<Attendance> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public AttendanceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Set layout cho adapter để hiển thị lên list
        ItemAttendanceListBinding listBinding = ItemAttendanceListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MyViewHolder(listBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setBinding(array.get(position));
        holder.binding.txtdate.setText(array.get(position).getDate()+" / "+array.get(position).getTime());
        if(array.get(position).getLydo() == null){
            holder.binding.txttrangthai.setText(array.get(position).getTrangthai());
        }else{
            holder.binding.txttrangthai.setText(array.get(position).getTrangthai() + " / "+array.get(position).getLydo());
        }
        String urlAvatar = Utils.BASE_URL+"hocsinh/image/"+array.get(position).getAvatar();
        Glide.with(context).load(urlAvatar).into(holder.binding.pichsdd);

        holder.binding.btnupdateGhichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                DialogSuaGhichuBinding ghichuBinding;
                ghichuBinding = DataBindingUtil.inflate(dialog.getLayoutInflater(),R.layout.dialog_sua_ghichu,null,false);
                dialog.setContentView(ghichuBinding.getRoot());
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                ghichuBinding.edtTrangthai.setText(array.get(position).getTrangthai());
                ghichuBinding.btnUpdateLydo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = array.get(position).getId();
                        String lydo = ghichuBinding.edtLydo.getText().toString().trim();
                        if (lydo.isEmpty()) {
                            ghichuBinding.edtLydo.setError("Không được để trống");
                        } else {
                            context.updateLydo(id,lydo);
                            dialog.dismiss();
                        }
                    }
                });
                ghichuBinding.btnHuyLydo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemAttendanceListBinding binding;
        public MyViewHolder(ItemAttendanceListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(Attendance attendance){
            binding.setAttendancelist(attendance);
        }
    }




}
