package com.example.appdiemdanh.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.appdiemdanh.data.api.ApiHocsinh;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.data.model.Student;
import com.example.appdiemdanh.data.model.Class;
import com.example.appdiemdanh.util.Utils;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRepository {
    private final ApiHocsinh apiHocsinh;

    public StudentRepository (){
        apiHocsinh = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiHocsinh.class);

    }

    public MutableLiveData<List<Student>> getDataStudent(int iduser){
        MutableLiveData<List<Student>> data = new MutableLiveData<>();
        apiHocsinh.getHs(iduser).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                data.setValue(null);
                Log.e("listDataHocsinh",t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<Student> deleteStudent(int id,int iduser){
        MutableLiveData<Student> data = new MutableLiveData<>();
        apiHocsinh.deleteHs(id,iduser).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                data.setValue(null);
                Log.e("deleteStudent",t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<Class> getLopSiso(int iduser){
        MutableLiveData<Class> data = new MutableLiveData<>();
        apiHocsinh.getLopSiso(iduser).enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                data.setValue(null);
                Log.e("getLopSiso",t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<Class> updateLop(int iduser, String lop){
        MutableLiveData<Class> data = new MutableLiveData<>();
        apiHocsinh.tenlop(iduser,lop).enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                Class tenlop = response.body();
                if(tenlop != null){
                    data.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                data.setValue(null);
                Log.e("updatetenlop",t.getMessage());
            }
        });
        return data;
    }


    public MutableLiveData<Student> addStudent(RequestBody iduser, RequestBody name,
                                               RequestBody uid, RequestBody mshs, RequestBody gender,
                                               RequestBody mobile, RequestBody week, RequestBody year,
                                               MultipartBody.Part avatar){

        MutableLiveData<Student> data = new MutableLiveData<>();
        Call<Student> call = apiHocsinh.insertHs(iduser,name, uid, mshs, gender, mobile,week,year,avatar);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                Student student = response.body();
                if(student!=null) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                data.setValue(null);
                Log.e("addStudent",t.getMessage());
            }
        });
        return data;
    }


    public MutableLiveData<List<Student>> showUID(){
        MutableLiveData<List<Student>> data = new MutableLiveData<>();
        apiHocsinh.showUID().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                data.setValue(null);
                Log.e("showUID",t.getMessage());
            }
        });
        return data;
    }


    public MutableLiveData<Student> updateStudent(RequestBody id,RequestBody iduser, RequestBody name,
                                                  RequestBody uid, RequestBody mshs, RequestBody gender,
                                                  RequestBody mobile, RequestBody week, RequestBody year,
                                                  MultipartBody.Part avatar){
        MutableLiveData<Student> data = new MutableLiveData<>();
        Call<Student> call = apiHocsinh.updateHs(id,iduser,name,uid,mshs,gender,mobile,week,year,avatar);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                Student student = response.body();
                if(student!=null){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                data.setValue(null);
                Log.e("updateStudent",t.getMessage());
            }
        });
        return data;
    }



}
