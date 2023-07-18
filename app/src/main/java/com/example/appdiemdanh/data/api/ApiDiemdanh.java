package com.example.appdiemdanh.data.api;

import com.example.appdiemdanh.data.model.DateTimediemdanh;
import com.example.appdiemdanh.data.model.Attendance;
import com.example.appdiemdanh.data.model.StatisWeek;
import com.example.appdiemdanh.data.model.Class;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiDiemdanh {
    // diem danh
    @POST("diemdanh/getDiemdanhvao.php")
    @FormUrlEncoded
    Call<List<Attendance>> getDiemdanhvao(
            @Field("iduser") int iduser,
            @Field("idngaydd") int idngaydd,
            @Field("idsetupdd") int idsetupdd
    );

    @POST("diemdanh/getDiemdanhra.php")
    @FormUrlEncoded
    Call<List<Attendance>> getDiemdanhra(
            @Field("iduser") int iduser,
            @Field("idngaydd") int idngaydd,
            @Field("idsetupdd") int idsetupdd
    );

    @POST("diemdanh/deleteDiemdanh.php")
    @FormUrlEncoded
    Call<Attendance> deleteDiemdanh(
            @Field("idsetupdd") int idsetupdd
    );

    // ngay diem danh
    @POST("diemdanh/getNgayDiemdanh.php")
    @FormUrlEncoded
    Call<List<DateTimediemdanh>> getNgayDiemdanh(
            @Field("iduser") int iduser,
            @Field("date") String date
    );
    @POST("diemdanh/deleteNgayDiemdanh.php")
    @FormUrlEncoded
    Call<DateTimediemdanh> deleteNgayDiemdanh(
            @Field("idngaydd") int idngaydd,
            @Field("idsetupdd") int idsetupdd
    );

    
    // thong ke diem danh
    @POST("diemdanh/getThongkeBuoi.php")
    @FormUrlEncoded
    Call<Class> getThongkeDiemdanh(
            @Field("iduser") int iduser,
            @Field("idsetupdd") int idsetupdd
    );

    @POST("diemdanh/getThongkeTuan.php")
    @FormUrlEncoded
    Call<List<StatisWeek>> getThongkeTuan(
            @Field("iduser") int iduser,
            @Field("week") String week,
            @Field("year") String year
    );

    @POST("diemdanh/getWeek.php")
    @FormUrlEncoded
    Call<List<StatisWeek>> getChooseWeek(
            @Field("iduser") int iduser,
            @Field("week") String week,
            @Field("year") String yeari
    );

    @POST("diemdanh/getTuanDiemdanh.php")
    @FormUrlEncoded
    Call<StatisWeek> getSolandd(
            @Field("iduser") int iduser,
            @Field("week") String week,
            @Field("year") String yeari
    );


    @POST("diemdanh/getThongkeHocsinh.php")
    @FormUrlEncoded
    Call<List<StatisWeek>> getThongkeHocsinh(
            @Field("iduser") int iduser,
            @Field("idweek") int idweek,
            @Field("idhs") int idhs,
            @Field("trangthai") String trangthai
    );

    // time diem danh

    @POST("diemdanh/insertTimeDiemdanh.php")
    @FormUrlEncoded
    Call<DateTimediemdanh> insertTime(
            @Field("iduser") int iduser,
            @Field("date") String date,
            @Field("timestart") String timestart,
            @Field("timevao") String timevao,
            @Field("timera") String timera
    );

    @POST("diemdanh/updateTimeDiemdanh.php")
    @FormUrlEncoded
    Call<DateTimediemdanh> updateTime(
            @Field("id") int id,
            @Field("timestart") String timestart,
            @Field("timevao") String timevao,
            @Field("timera") String timera
    );

    @POST("diemdanh/getTimeDiemdanh.php")
    @FormUrlEncoded
    Call<List<DateTimediemdanh>> getTimeDiemdanh(
            @Field("iduser") int iduser,
            @Field("date") String date
    );

    @POST("diemdanh/deleteTimeDiemdanh.php")
    @FormUrlEncoded
    Call<DateTimediemdanh> deleteTimeDiemdanh(
            @Field("id") int idsetupdd
    );

    //time ve som
    @POST("diemdanh/insertTimeVesom.php")
    @FormUrlEncoded
    Call<DateTimediemdanh> insertTimeVesom(
            @Field("iduser") int iduser,
            @Field("idsetupdd") int idsetupdd,
            @Field("timevesom") String timevesom

    );

    @POST("diemdanh/getTimeVesom.php")
    @FormUrlEncoded
    Call<List<DateTimediemdanh>> getTimeVesom(
            @Field("iduser") int iduser,
            @Field("idsetupdd") int idsetupdd
    );

    @POST("diemdanh/deleteTimeVesom.php")
    @FormUrlEncoded
    Call<DateTimediemdanh> deleteTimeVesom(
            @Field("iduser") int iduser,
            @Field("idsetupdd") int idsetupdd
    );

    @POST("diemdanh/updateLydo.php")
    @FormUrlEncoded
    Call<Attendance> updateLydo(
            @Field("id") int id,
            @Field("lydo") String lydo

    );


}
