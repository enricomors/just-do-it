package com.unibo.justdoit.calendar;

import android.support.v4.app.Fragment;
import android.widget.TextView;

/**
 * Implements the UI for the Calendar View screen
 */
public class CalendarViewFragment extends Fragment {

    private TextView mDateView;

    private CalendarViewContract.Presenter mPresenter;

    public static CalendarViewFragment newInstance() {
        return new CalendarViewFragment();
    }



}
