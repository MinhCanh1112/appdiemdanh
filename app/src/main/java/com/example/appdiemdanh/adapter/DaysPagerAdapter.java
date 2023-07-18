package com.example.appdiemdanh.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.appdiemdanh.view.fragment.FridayFragment;
import com.example.appdiemdanh.view.fragment.MondayFragment;
import com.example.appdiemdanh.view.fragment.SaturdayFragment;
import com.example.appdiemdanh.view.fragment.SundayFragment;
import com.example.appdiemdanh.view.fragment.ThursdayFragment;
import com.example.appdiemdanh.view.fragment.TuesdayFragment;
import com.example.appdiemdanh.view.fragment.WednesdayFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DaysPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 7;

    public DaysPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MondayFragment();
            case 1:
                return new TuesdayFragment();
            case 2:
                return new WednesdayFragment();
            case 3:
                return new ThursdayFragment();
            case 4:
                return new FridayFragment();
            case 5:
                return new SaturdayFragment();
            case 6:
                return new SundayFragment();
            default:
                throw new IllegalArgumentException("Invalid page position: " + position);
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE - dd/MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, position + 2); // position 0 = Monday
        String pageTitle = sdf.format(calendar.getTime());
        if (position == getTodayTabIndex()) {
            pageTitle += " (Today)";
        }
        return pageTitle;
    }

    private int getTodayTabIndex() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK); // 1 = Sunday, 2 = Monday, ..., 7 = Saturday
        return dayOfWeek - 2; // Monday is the first tab
    }


}
