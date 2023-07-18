package com.example.appdiemdanh.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appdiemdanh.data.model.Attendance;
import com.example.appdiemdanh.data.model.Class;
import com.example.appdiemdanh.data.model.DateTimediemdanh;
import com.example.appdiemdanh.data.model.StatisWeek;
import com.example.appdiemdanh.data.repository.AttendanceRepository;

import java.util.List;

public class AttendanceViewModel extends ViewModel {
    private AttendanceRepository attendanceRepository;
    public AttendanceViewModel(){
        attendanceRepository = new AttendanceRepository();
    }

    //activity AddAttendance
    public MutableLiveData<DateTimediemdanh> addAttendanceTime(int iduser,String date,String timestart,String timevao,String timera){
        return attendanceRepository.addAttendanceTime(iduser,date,timestart,timevao,timera);
    }

    //activity AttendanceInfor
    public MutableLiveData<DateTimediemdanh> updateAttendanceTime(int idsetupdd,String timestart,String timevao,String timera){
        return attendanceRepository.updateAttendanceTime(idsetupdd,timestart,timevao,timera);
    }

    //activity AttendanceInfor
    public MutableLiveData<List<DateTimediemdanh>> getTimeVesom(int iduser,int idsetupdd){
        return attendanceRepository.getTimeVesom(iduser,idsetupdd);
    }

    //activity AttendanceInfor
    public MutableLiveData<DateTimediemdanh> insertTimeVesom(int iduser,int idsetupdd,String timevesom){
        return attendanceRepository.insertTimeVesom(iduser,idsetupdd,timevesom);
    }

    //activity AttendanceInfor
    public MutableLiveData<DateTimediemdanh> deteleTimeVesom(int iduser,int idsetupdd){
        return attendanceRepository.deteleTimeVesom(iduser,idsetupdd);
    }

    //activity AttendanceInfor
    public MutableLiveData<DateTimediemdanh> deteleAttendanceTime(int idsetupdd){
        return attendanceRepository.deleteAttendanceTime(idsetupdd);
    }

    //fragment
    public MutableLiveData<List<DateTimediemdanh>> getTimeDiemdanh(int iduser,String date){
        return attendanceRepository.getTimeDiemdanh(iduser,date);
    }

    //activity AttendanceDate
    public MutableLiveData<List<DateTimediemdanh>> getNgayDiemdanh(int iduser,String date){
        return attendanceRepository.getNgayDiemdanh(iduser,date);
    }

    //activity AttendanceDate
    public MutableLiveData<DateTimediemdanh> deleteAttendanceDate(int idngaydd,int idsetupdd){
        return attendanceRepository.deleteAttendanceDate(idngaydd,idsetupdd);
    }

    //activity AttendanceList
    public MutableLiveData<Class> getThongkeTheoBuoi(int iduser,int idsetupdd){
        return attendanceRepository.getThongkeTheoBuoi(iduser,idsetupdd);
    }

    //activity AttendanceList
    public MutableLiveData<List<Attendance>> getDiemdanhvao(int iduser,int idngaydd,int idsetupdd){
        return attendanceRepository.getDiemdanhvao(iduser,idngaydd,idsetupdd);
    }

    //activity AttendanceList
    public MutableLiveData<List<Attendance>> getDiemdanhra(int iduser,int idngaydd,int idsetupdd){
        return attendanceRepository.getDiemdanhra(iduser,idngaydd,idsetupdd);
    }

    //activity AttendanceList
    public MutableLiveData<Attendance> updateLydo(int iduser,String lydo){
        return attendanceRepository.updateLydo(iduser,lydo);
    }

    //activity AttendanceList
    public MutableLiveData<Attendance> deleteDiemdanh(int idsetupdd){
        return attendanceRepository.deleteDiemdanh(idsetupdd);
    }

    //activity ChooseWeek
    public MutableLiveData<List<StatisWeek>> getChooseWeek(int iduser, String week, String year){
        return attendanceRepository.getChooseWeek(iduser,week,year);
    }

    //activity StatisticsWeek
    public MutableLiveData<StatisWeek> getSolandd(int iduser,String week,String year){
        return attendanceRepository.getSolandd(iduser,week,year);
    }

    //activity StatisticsWeek
    public MutableLiveData<List<StatisWeek>> getThongkeTuan(int iduser, String week, String year){
        return attendanceRepository.getThongkeTuan(iduser,week,year);
    }

    //activity StatisticsWeekOfEachStudent
    public MutableLiveData<List<StatisWeek>> getThongkeHocsinh(int iduser,int idweek,int idhs,String trangthai){
        return attendanceRepository.getThongkeHocsinh(iduser,idweek,idhs,trangthai);
    }


}
