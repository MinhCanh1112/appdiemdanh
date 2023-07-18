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

import com.example.appdiemdanh.R;
import com.example.appdiemdanh.adapter.FragmentAdapter;
import com.example.appdiemdanh.databinding.FragmentFridayBinding;
import com.example.appdiemdanh.util.Utils;
import com.example.appdiemdanh.viewmodel.AttendanceViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;



public class FridayFragment extends Fragment {

    Context mCcontext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCcontext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCcontext = null;
    }

    private FragmentAdapter mAdapter;
    FragmentFridayBinding binding;
    AttendanceViewModel attendanceViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_friday,container,false);
        View rootView = binding.getRoot();
        attendanceViewModel = new ViewModelProvider(getActivity()).get(AttendanceViewModel.class);



        getTimeDiemdanh();
        return rootView;
    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    @Override
    public void onStart() {
        super.onStart();
        getTimeDiemdanh();
    }

    private void getTimeDiemdanh() {
        String date = getCurrentDate();
        int iduser = Utils.getUserId(mCcontext);
        attendanceViewModel.getTimeDiemdanh(iduser,date).observe(getActivity(),dateTimediemdanhs -> {
            binding.recycFriday.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new FragmentAdapter();
            binding.recycFriday.setAdapter(mAdapter);
            mAdapter.setData(dateTimediemdanhs,mCcontext);
        });

    }
}