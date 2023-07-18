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
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdiemdanh.R;
import com.example.appdiemdanh.databinding.ItemListStudentBinding;
import com.example.appdiemdanh.view.activity.ListStudent;
import com.example.appdiemdanh.view.activity.UpdateStudent;
import com.example.appdiemdanh.data.model.Student;
import com.example.appdiemdanh.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>{
    ListStudent context;
    List<Student> array;

    public StudentAdapter(ListStudent context, List<Student> array) {
        this.context = context;
        this.array = array;
    }

    public void setFilteredList(List<Student> filteredList){
        this.array = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Set layout cho adapter để hiển thị lên list
        ItemListStudentBinding itemListStudentBinding = ItemListStudentBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(itemListStudentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setBinding(array.get(position));
        Glide.with(holder.itemView).load(Utils.BASE_URL+"hocsinh/image/"+array.get(position).getAvatar()).into(holder.binding.pichs);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.menu_lisths);
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_edit:
                            Intent i = new Intent(context, UpdateStudent.class);
                            i.putExtra("dataHocsinh", array.get(position));
                            context.startActivity(i);
                            return true;
                        case R.id.action_delete:
                            XacNhanXoa(array.get(position).getName(), array.get(position).getId());
                            return true;
                        default:
                            return false;
                    }
                });
                popupMenu.show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return array !=null ? array.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemListStudentBinding binding;
        public MyViewHolder(ItemListStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setBinding(Student student){
            binding.setStudent(student);
        }
    }

    private void XacNhanXoa(String ten, int id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Bạn có muốn xoá học sinh "+ten+" không?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.deleteStudent(id);
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
