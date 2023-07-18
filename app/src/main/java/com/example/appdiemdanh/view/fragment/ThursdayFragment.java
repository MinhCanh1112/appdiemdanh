package com.example.appdiemdanh.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.FragmentAdapter;
import com.example.appdiemdanh.data.model.DateTimediemdanh;
import com.example.appdiemdanh.data.api.ApiDiemdanh;
import com.example.appdiemdanh.data.api.RetrofitClient;
import com.example.appdiemdanh.databinding.FragmentThursdayBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.AttendanceViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThursdayFragment extends Fragment {

    Context mCcontext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCcontext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCcontext = null;
    }

    private FragmentAdapter mAdapter;
    FragmentThursdayBinding binding;
    AttendanceViewModel attendanceViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_thursday,container,false);
        View rootView = binding.getRoot();
        attendanceViewModel = new ViewModelProvider(getActivity()).get(AttendanceViewModel.class);
        getTimeDiemdanh();
        return rootView;
    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    @Override
    public void onStart() {
        super.onStart();
        getTimeDiemdanh();
    }

    private void getTimeDiemdanh(){
        String date = getCurrentDate();
        int iduser = Utils.getUserId(mCcontext);
        attendanceViewModel.getTimeDiemdanh(iduser,date).observe(getActivity(),dateTimediemdanhs -> {
            binding.recycThursday.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new FragmentAdapter();
            binding.recycThursday.setAdapter(mAdapter);
            mAdapter.setData(dateTimediemdanhs,mCcontext);
        });

    }



}