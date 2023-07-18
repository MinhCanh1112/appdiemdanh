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
import com.example.appdiemdanh.databinding.FragmentSaturdayBinding;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaturdayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaturdayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SaturdayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaturdayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaturdayFragment newInstance(String param1, String param2) {
        SaturdayFragment fragment = new SaturdayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
    AttendanceViewModel attendanceViewModel;
    FragmentSaturdayBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_saturday,container,false);
        View rootView = binding.getRoot();
        attendanceViewModel = new ViewModelProvider(getActivity()).get(AttendanceViewModel.class);

        getTimeDiemdanh();
        return rootView;
    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
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
            binding.recycSaturday.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new FragmentAdapter();
            binding.recycSaturday.setAdapter(mAdapter);
            mAdapter.setData(dateTimediemdanhs,mCcontext);
        });

    }
}