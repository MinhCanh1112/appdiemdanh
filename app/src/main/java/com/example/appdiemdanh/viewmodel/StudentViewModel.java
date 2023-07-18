package com.example.appdiemdanh.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appdiemdanh.data.model.Student;
import com.example.appdiemdanh.data.model.Class;
import com.example.appdiemdanh.data.repository.StudentRepository;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StudentViewModel extends ViewModel {
    private StudentRepository studentRepository;

    public StudentViewModel() {
       studentRepository = new StudentRepository();
    }

    public MutableLiveData<List<Student>> liststudent(int iduser){
        return studentRepository.getDataStudent(iduser);
    }

    public MutableLiveData<Student> deleteStudent(int id,int iduser){
        return studentRepository.deleteStudent(id,iduser);
    }

    public MutableLiveData<Class> lopsiso(int iduser){
        return studentRepository.getLopSiso(iduser);
    }

    public MutableLiveData<Class> tenlop(int iduser, String lop){
        return studentRepository.updateLop(iduser,lop);
    }

    public MutableLiveData<Student> addStudent(RequestBody iduser, RequestBody name,
                                                              RequestBody uid, RequestBody mshs, RequestBody gender,
                                                              RequestBody mobile, RequestBody week, RequestBody year,
                                                              MultipartBody.Part avatar){
        return studentRepository.addStudent(iduser,name, uid, mshs, gender, mobile,week,year,avatar);
    }

    public MutableLiveData<List<Student>> showUID(){
        return studentRepository.showUID();
    }

    public MutableLiveData<Student> updateStudent(RequestBody id,RequestBody iduser, RequestBody name,
                                                  RequestBody uid, RequestBody mshs, RequestBody gender,
                                                  RequestBody mobile, RequestBody week, RequestBody year,
                                                  MultipartBody.Part avatar){
        return studentRepository.updateStudent(id,iduser,name,uid,mshs,gender,mobile,week,year,avatar);
    }
}
