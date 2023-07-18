package com.example.appdiemdanh.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appdiemdanh.data.api.ApiUser;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.data.model.User;
import com.example.appdiemdanh.data.repository.UserRepository;
import com.example.appdiemdanh.util.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;
    ApiUser apiUser;
    public UserViewModel(){
        userRepository = new UserRepository();
        apiUser = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiUser.class);
    }

    //activity LoginUser
    public MutableLiveData<User> resetPass(String email){
        return userRepository.ResetPass(email);
    }

    
    //activity LoginUser
    public MutableLiveData<User> loginUser(String email,String pass){
        return userRepository.LoginUser(email,pass);
    }

    //activity RegisterUser
    public MutableLiveData<User> RegisterUser(RequestBody email, RequestBody username, RequestBody mobile, RequestBody lop,
                                              RequestBody pass, MultipartBody.Part avatar){
        return userRepository.RegisterUser(email,username,mobile,lop,pass,avatar);
    }

    //activity InformationUser
    public MutableLiveData<User> UpdateUser(RequestBody iduser, RequestBody email, RequestBody username, RequestBody mobile, MultipartBody.Part avatar){
        return userRepository.UpdateUser(iduser,email,username,mobile, avatar);
    }

    //activity InformationUser
    public MutableLiveData<User> Doimatkhau(int iduser,String pass,String newpass){
        return userRepository.Doimatkhau(iduser,pass,newpass);
    }

}
