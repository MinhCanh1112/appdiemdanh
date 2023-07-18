package com.example.appdiemdanh.data.api;

import com.example.appdiemdanh.data.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiUser {
    // tai khoan
    @Multipart
    @POST("taikhoan/registerUser.php")
    Call<User> registerUser(
            @Part("email") RequestBody email,
            @Part("username") RequestBody username,
            @Part("mobile")RequestBody mobile,
            @Part("lop")RequestBody lop,
            @Part("pass")RequestBody pass,
            // phương thức uploadImage với tham số là một đối tượng MultipartBody.Part
            @Part MultipartBody.Part avatar
    );

    @POST("taikhoan/loginUser.php")
    @FormUrlEncoded
    Call<User> longinUser(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @Multipart
    @POST("taikhoan/updateUser.php")
    Call<User> updateUser(
            @Part("iduser")RequestBody iduser,
            @Part("email") RequestBody email,
            @Part("username") RequestBody username,
            @Part("mobile")RequestBody mobile,
            // phương thức uploadImage với tham số là một đối tượng MultipartBody.Part
            @Part MultipartBody.Part avatar
    );

    @POST("taikhoan/quenmatkhau.php")
    @FormUrlEncoded
    Call<User> quenmatkhau(
            @Field("email") String email );

    @POST("taikhoan/doimatkhau.php")
    @FormUrlEncoded
    Call<User> doimatkhau(
            @Field("id") int iduser,
            @Field("pass") String pass,
            @Field("newpass") String newpass );

    @POST("taikhoan/thongtintaikhoan.php")
    @FormUrlEncoded
    Call<User> thongtintaikhoan(
            @Field("iduser") int iduser );

    @POST("taikhoan/getHomeNav.php")
    @FormUrlEncoded
    Call<List<User>> getHomeNav(
            @Field("iduser") int iduser );
}
