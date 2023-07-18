package com.example.appdiemdanh.data.api;

import com.example.appdiemdanh.data.model.Student;
import com.example.appdiemdanh.data.model.Class;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiHocsinh {

    // hoc sinh
    @POST("hocsinh/getHs.php")
    @FormUrlEncoded
    Call<List<Student>> getHs(
            @Field("iduser") int iduser
    );


    @Multipart
    @POST("hocsinh/insertHs.php")
    Call<Student> insertHs(
            @Part("iduser") RequestBody iduser,
            @Part("name")RequestBody name,
            @Part("uid")RequestBody uid,
            @Part("mshs")RequestBody mshs,
            @Part("gender")RequestBody gender,
            @Part("mobile")RequestBody mobile,
            @Part("week")RequestBody week,
            @Part("year")RequestBody year,
            @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST("hocsinh/updateHs.php")
    Call<Student> updateHs(
            @Part("id") RequestBody id,
            @Part("iduser") RequestBody iduser,
            @Part("name")RequestBody name,
            @Part("uid")RequestBody uid,
            @Part("mshs")RequestBody mshs,
            @Part("gender")RequestBody gender,
            @Part("mobile")RequestBody mobile,
            @Part("week")RequestBody week,
            @Part("year")RequestBody year,
            // phương thức uploadImage với tham số là một đối tượng MultipartBody.Part
            @Part MultipartBody.Part avatar
    );

    @POST("hocsinh/deleteHs.php")
    @FormUrlEncoded
    Call<Student> deleteHs(
            @Field("id") int id,
            @Field("iduser") int iduser
    );

    @GET("hocsinh/showUID.php")
    Call<List<Student>> showUID();

    // lop
    @POST("hocsinh/lopHs.php")
    @FormUrlEncoded
    Call<Class> tenlop(
            @Field("iduser") int iduser,
            @Field("lop") String lop
    );

    @POST("hocsinh/getLopSiso.php")
    @FormUrlEncoded
    Call<Class> getLopSiso(
            @Field("iduser") int iduser
    );


}
