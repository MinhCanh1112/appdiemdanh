package com.example.appdiemdanh.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.appdiemdanh.data.api.ApiUser;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.data.model.User;
import com.example.appdiemdanh.util.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiUser apiUser;
    public UserRepository(){
        apiUser = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiUser.class);
    }

    //activity LoginUser
    public MutableLiveData<User> ResetPass(String email){
        MutableLiveData<User> data = new MutableLiveData<>();
        apiUser.quenmatkhau(email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("resetpass",t.getMessage());
            }
        });
        return data;
    }

    //activity LoginUser
    public MutableLiveData<User> LoginUser(String email,String pass){
        MutableLiveData<User> data = new MutableLiveData<>();
        Call<User> call = apiUser.longinUser(email,pass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("loginUser",t.getMessage());
            }
        });
        return data;
    }

    //activity RegisterUser
    public MutableLiveData<User> RegisterUser(RequestBody email, RequestBody username, RequestBody mobile,
                                              RequestBody lop, RequestBody pass, MultipartBody.Part avatar){
        MutableLiveData<User> data = new MutableLiveData<>();
        Call<User> call = apiUser.registerUser(email,username,mobile,lop,pass,avatar);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("RegisterUser",t.getMessage());
            }
        });
        return data;
    }

    //activity InformationUser
    public MutableLiveData<User> UpdateUser(RequestBody iduser, RequestBody email, RequestBody username,
                                            RequestBody mobile, MultipartBody.Part avatar){
        MutableLiveData<User> data = new MutableLiveData<>();
        Call<User> call = apiUser.updateUser(iduser,email,username,mobile, avatar);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("UpdateUser",t.getMessage());
            }
        });
        return data;
    }

    //activity InformationUser
    public MutableLiveData<User> Doimatkhau(int iduser,String pass,String newpass){
        MutableLiveData<User> data = new MutableLiveData<>();
        Call<User> call = apiUser.doimatkhau(iduser,pass,newpass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("Doimatkhau",t.getMessage());
            }
        });
        return data;
    }
}
