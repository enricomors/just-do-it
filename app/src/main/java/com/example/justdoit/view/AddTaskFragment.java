package com.example.justdoit.view;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.justdoit.R;
import com.example.justdoit.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment {

    @BindView(R.id.fabDone)
    FloatingActionButton fabDone;

    @BindView(R.id.button_class)
    Button buttonClass;

    @BindView(R.id.button_date)
    Button buttonDate;

    @BindView(R.id.button_priority)
    Button buttonPriority;

    @BindView(R.id.button_time)
    Button buttonTime;

    @BindView(R.id.edit_text_description)
    EditText textDescription;

    @BindView(R.id.edit_text_title)
    EditText textTitle;

    private TaskViewModel taskViewModel;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabDone.setOnClickListener(v -> onTaskSave());
        buttonTime.setOnClickListener(v -> {
            // open date picker
            DialogFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getFragmentManager(), "timePicker");
        });
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open time picker
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    private void onTaskSave() {
        NavDirections action = AddTaskFragmentDirections.actionSaveTask();
        Navigation.findNavController(fabDone).navigate(action);

        // Save newly created task to database
        // Task newTask = new Task(...data from fields...)
        // taskViewModel.insertTask(newTask);
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

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
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            Toast.makeText(getContext(), "time set", Toast.LENGTH_SHORT).show();
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

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
        }
    }
}
