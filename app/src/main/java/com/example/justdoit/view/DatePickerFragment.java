package com.example.justdoit.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    //Interface created for communicating this dialog fragment events to called fragment
    public interface DatePickerFragmentEvents {
        void onDateSelected(String date);
    }

    DatePickerFragmentEvents datePickerFragmentEvents;

    public void setDatePickerFragmentEvents(DatePickerFragmentEvents events) {
        datePickerFragmentEvents = events;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Toast.makeText(getContext(), "date set", Toast.LENGTH_SHORT).show();

        String formattedDate = day + "-" + month + "-" + year;
        datePickerFragmentEvents.onDateSelected(formattedDate);
    }
}