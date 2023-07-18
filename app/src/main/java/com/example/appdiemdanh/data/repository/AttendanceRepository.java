package com.example.appdiemdanh.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.appdiemdanh.data.api.ApiDiemdanh;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.data.model.Attendance;
import com.example.appdiemdanh.data.model.Class;
import com.example.appdiemdanh.data.model.DateTimediemdanh;
import com.example.appdiemdanh.data.model.StatisWeek;
import com.example.appdiemdanh.util.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceRepository {
    private final ApiDiemdanh apiDiemdanh;
    public AttendanceRepository(){
        apiDiemdanh = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiDiemdanh.class);
    }

    //activity AddAttendance
    public MutableLiveData<DateTimediemdanh> addAttendanceTime(int iduser,String date,String timestart,String timevao,String timera){
        MutableLiveData<DateTimediemdanh> data = new MutableLiveData<>();
        Call<DateTimediemdanh> call = apiDiemdanh.insertTime(iduser,date,timestart,timevao,timera);
        call.enqueue(new Callback<DateTimediemdanh>() {
            @Override
            public void onResponse(@NonNull Call<DateTimediemdanh> call, @NonNull Response<DateTimediemdanh> response) {
                DateTimediemdanh dateTimediemdanh = response.body();
                if(dateTimediemdanh!=null){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DateTimediemdanh> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("addAttendanceTime",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceInfor
    public MutableLiveData<DateTimediemdanh> updateAttendanceTime(int idsetupdd,String timestart,String timevao,String timera){
        MutableLiveData<DateTimediemdanh> data = new MutableLiveData<>();
        Call<DateTimediemdanh> call = apiDiemdanh.updateTime(idsetupdd,timestart,timevao,timera);
        call.enqueue(new Callback<DateTimediemdanh>() {
            @Override
            public void onResponse(@NonNull Call<DateTimediemdanh> call, @NonNull Response<DateTimediemdanh> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DateTimediemdanh> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("updateAttendanceTime",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceInfor
    public MutableLiveData<List<DateTimediemdanh>> getTimeVesom(int iduser,int idsetupdd){
        MutableLiveData<List<DateTimediemdanh>> data = new MutableLiveData<>();
        apiDiemdanh.getTimeVesom(iduser,idsetupdd).enqueue(new Callback<List<DateTimediemdanh>>() {
            @Override
            public void onResponse(@NonNull Call<List<DateTimediemdanh>> call, @NonNull Response<List<DateTimediemdanh>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<DateTimediemdanh>> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("getTimeVesom",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceInfor
    public MutableLiveData<DateTimediemdanh> insertTimeVesom(int iduser,int idsetupdd,String timevesom){
        MutableLiveData<DateTimediemdanh> data = new MutableLiveData<>();
        Call<DateTimediemdanh> call = apiDiemdanh.insertTimeVesom(iduser,idsetupdd,timevesom);
        call.enqueue(new Callback<DateTimediemdanh>() {
            @Override
            public void onResponse(@NonNull Call<DateTimediemdanh> call, @NonNull Response<DateTimediemdanh> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DateTimediemdanh> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("insertTimeVesom",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceInfor
    public MutableLiveData<DateTimediemdanh> deteleTimeVesom(int iduser,int idsetupdd){
        MutableLiveData<DateTimediemdanh> data = new MutableLiveData<>();
        Call<DateTimediemdanh> call = apiDiemdanh.deleteTimeVesom(iduser,idsetupdd);
        call.enqueue(new Callback<DateTimediemdanh>() {
            @Override
            public void onResponse(@NonNull Call<DateTimediemdanh> call, @NonNull Response<DateTimediemdanh> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DateTimediemdanh> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("deteleTimeVesom",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceInfor
    public MutableLiveData<DateTimediemdanh> deleteAttendanceTime(int idsetupdd){
        MutableLiveData<DateTimediemdanh> data = new MutableLiveData<>();
        Call<DateTimediemdanh> call = apiDiemdanh.deleteTimeDiemdanh(idsetupdd);
        call.enqueue(new Callback<DateTimediemdanh>() {
            @Override
            public void onResponse(@NonNull Call<DateTimediemdanh> call, @NonNull Response<DateTimediemdanh> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DateTimediemdanh> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("deteleTimeVesom",t.getMessage());
            }
        });
        return data;
    }

    //fragment
    public MutableLiveData<List<DateTimediemdanh>> getTimeDiemdanh(int iduser,String date){
        MutableLiveData<List<DateTimediemdanh>> data = new MutableLiveData<>();
        apiDiemdanh.getTimeDiemdanh(iduser,date).enqueue(new Callback<List<DateTimediemdanh>>() {
            @Override
            public void onResponse(@NonNull Call<List<DateTimediemdanh>> call, @NonNull Response<List<DateTimediemdanh>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<DateTimediemdanh>> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("getTimeDiemdanh",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceDate
    public MutableLiveData<List<DateTimediemdanh>> getNgayDiemdanh(int iduser,String date){
        MutableLiveData<List<DateTimediemdanh>> data = new MutableLiveData<>();
        Call<List<DateTimediemdanh>> call = apiDiemdanh.getNgayDiemdanh(iduser,date);
        call.enqueue(new Callback<List<DateTimediemdanh>>() {
            @Override
            public void onResponse(@NonNull Call<List<DateTimediemdanh>> call, @NonNull Response<List<DateTimediemdanh>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<DateTimediemdanh>> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("getNgayDiemdanh",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceDate
    public MutableLiveData<DateTimediemdanh> deleteAttendanceDate(int idngaydd,int idsetupdd){
        MutableLiveData<DateTimediemdanh> data = new MutableLiveData<>();
        Call<DateTimediemdanh> call = apiDiemdanh.deleteNgayDiemdanh(idngaydd,idsetupdd);
        call.enqueue(new Callback<DateTimediemdanh>() {
            @Override
            public void onResponse(@NonNull Call<DateTimediemdanh> call, @NonNull Response<DateTimediemdanh> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DateTimediemdanh> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("deleteAttendanceDate",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceList
    public MutableLiveData<Class> getThongkeTheoBuoi(int iduser, int idsetupdd){
        MutableLiveData<Class> data = new MutableLiveData<>();
        Call<Class> call = apiDiemdanh.getThongkeDiemdanh(iduser,idsetupdd);
        call.enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                data.setValue(null);
                Log.e("getThongkeTheoBuoi",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceList
    public MutableLiveData<List<Attendance>> getDiemdanhvao(int iduser,int idngaydd,int idsetupdd){
        MutableLiveData<List<Attendance>> data = new MutableLiveData<>();
        Call<List<Attendance>> call = apiDiemdanh.getDiemdanhvao(iduser,idngaydd,idsetupdd);
        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(@NonNull Call<List<Attendance>> call, @NonNull Response<List<Attendance>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Attendance>> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("getDiemdanhvao",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceList
    public MutableLiveData<List<Attendance>> getDiemdanhra(int iduser,int idngaydd,int idsetupdd){
        MutableLiveData<List<Attendance>> data = new MutableLiveData<>();
        Call<List<Attendance>> call = apiDiemdanh.getDiemdanhra(iduser,idngaydd,idsetupdd);
        call.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(@NonNull Call<List<Attendance>> call, @NonNull Response<List<Attendance>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Attendance>> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("getDiemdanhra",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceList
    public MutableLiveData<Attendance> updateLydo(int idngaydd,String lydo){
        MutableLiveData<Attendance> data = new MutableLiveData<>();
        Call<Attendance> call = apiDiemdanh.updateLydo(idngaydd,lydo);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(@NonNull Call<Attendance> call, @NonNull Response<Attendance> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Attendance> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("updateLydo",t.getMessage());
            }
        });
        return data;
    }

    //activity AttendanceList
    public MutableLiveData<Attendance> deleteDiemdanh(int idsetupdd){
        MutableLiveData<Attendance> data = new MutableLiveData<>();
        Call<Attendance> call = apiDiemdanh.deleteDiemdanh(idsetupdd);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(@NonNull Call<Attendance> call, @NonNull Response<Attendance> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Attendance> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("deleteDiemdanh",t.getMessage());
            }
        });
        return data;
    }

    //activity ChooseWeek
    public MutableLiveData<List<StatisWeek>> getChooseWeek(int iduser,String week,String year){
        MutableLiveData<List<StatisWeek>> data = new MutableLiveData<>();
        Call<List<StatisWeek>> call = apiDiemdanh.getChooseWeek(iduser,week,year);
        call.enqueue(new Callback<List<StatisWeek>>() {
            @Override
            public void onResponse(Call<List<StatisWeek>> call, Response<List<StatisWeek>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<StatisWeek>> call, Throwable t) {
                data.setValue(null);
                Log.e("getChooseWeek",t.getMessage());
            }
        });
        return data;
    }

    //activity StatisticsWeek
    public MutableLiveData<StatisWeek> getSolandd(int iduser,String week,String year){
        MutableLiveData<StatisWeek> data = new MutableLiveData<>();
        Call<StatisWeek> call = apiDiemdanh.getSolandd(iduser,week,year);
        call.enqueue(new Callback<StatisWeek>() {
            @Override
            public void onResponse(@NonNull Call<StatisWeek> call, @NonNull Response<StatisWeek> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<StatisWeek> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e("getSolandd",t.getMessage());
            }
        });
        return data;
    }

    //activity StatisticsWeek
    public MutableLiveData<List<StatisWeek>> getThongkeTuan(int iduser,String week,String year){
        MutableLiveData<List<StatisWeek>> data = new MutableLiveData<>();
        Call<List<StatisWeek>> call = apiDiemdanh.getThongkeTuan(iduser,week,year);
        call.enqueue(new Callback<List<StatisWeek>>() {
            @Override
            public void onResponse(Call<List<StatisWeek>> call, Response<List<StatisWeek>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<StatisWeek>> call, Throwable t) {
                data.setValue(null);
                Log.e("getThongkeTuan",t.getMessage());
            }
        });
        return data;
    }

    //activity StatisticsWeekOfEachStudent
    public MutableLiveData<List<StatisWeek>> getThongkeHocsinh(int iduser,int idweek,int idhs,String trangthai){
        MutableLiveData<List<StatisWeek>> data = new MutableLiveData<>();
        Call<List<StatisWeek>> call = apiDiemdanh.getThongkeHocsinh(iduser,idweek,idhs,trangthai);
        call.enqueue(new Callback<List<StatisWeek>>() {
            @Override
            public void onResponse(Call<List<StatisWeek>> call, Response<List<StatisWeek>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<StatisWeek>> call, Throwable t) {
                data.setValue(null);
                Log.e("getThongkeHocsinh",t.getMessage());
            }
        });
        return data;
    }


}
