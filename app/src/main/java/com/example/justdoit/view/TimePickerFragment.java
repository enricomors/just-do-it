package com.example.justdoit.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface TimePickerFragmentEvents {
        void onTimeSelected(String time);
    }

    TimePickerFragmentEvents timePickerFragmentEvents;

    public void setTimePickerFragmentEvents(TimePickerFragmentEvents events) {
        timePickerFragmentEvents = events;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Toast.makeText(getContext(), "time set", Toast.LENGTH_SHORT).show();

        String formattedTime = hour + ":" + minute;
        timePickerFragmentEvents.onTimeSelected(formattedTime);
    }
}