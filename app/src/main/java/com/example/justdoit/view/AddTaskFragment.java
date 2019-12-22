package com.example.justdoit.view;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.justdoit.R;
import com.example.justdoit.model.Task;
import com.example.justdoit.model.TaskClass;
import com.example.justdoit.viewmodel.TaskClassViewModel;
import com.example.justdoit.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment
        implements DatePickerFragment.DatePickerFragmentEvents,
        TimePickerFragment.TimePickerFragmentEvents {

    @BindView(R.id.fabDone)
    FloatingActionButton fabDone;

    @BindView(R.id.spinner_class)
    Spinner spinnerClass;

    @BindView(R.id.button_date)
    Button buttonDate;

    @BindView(R.id.spinner_priority)
    Spinner spinnerPriority;

    @BindView(R.id.button_time)
    Button buttonTime;

    @BindView(R.id.edit_text_description)
    EditText textDescription;

    @BindView(R.id.edit_text_title)
    EditText textTitle;

    private TaskViewModel taskViewModel;
    private TaskClassViewModel taskClassViewModel;
    private ArrayAdapter<TaskClass> classAdapter;
    private ArrayAdapter<CharSequence> priorityAdapter;
    private TaskClass classSelected;
    private int prioritySelected;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        ButterKnife.bind(this, view);

        classAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        priorityAdapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_array,
                android.R.layout.simple_spinner_dropdown_item);

        // populate the adapter for the class spinner w/ data coming from the ViewModel
        taskClassViewModel = ViewModelProviders.of(this).get(TaskClassViewModel.class);
        taskClassViewModel.getAllClasses().observe(this, taskClasses -> {
            for (TaskClass taskClass : taskClasses) {
                classAdapter.add(taskClass);
            }
        });

        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPriority.setAdapter(priorityAdapter);
        spinnerClass.setAdapter(classAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabDone.setOnClickListener(v -> onTaskSave());
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                classSelected = (TaskClass) parent.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                prioritySelected = (int) parent.getItemIdAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonTime.setOnClickListener(v -> {
            // open date picker
            DialogFragment timePickerFragment = new TimePickerFragment();
            ((TimePickerFragment) timePickerFragment).setTimePickerFragmentEvents(AddTaskFragment.this);
            timePickerFragment.show(getFragmentManager(), "timePicker");
        });
        buttonDate.setOnClickListener(v -> {
            // open time picker
            DialogFragment datePickerFragment = new DatePickerFragment();
            ((DatePickerFragment) datePickerFragment).setDatePickerFragmentEvents(AddTaskFragment.this);
            datePickerFragment.show(getFragmentManager(), "datePicker");
        });
    }

    private void onTaskSave() {
        NavDirections action = AddTaskFragmentDirections.actionSaveTask();
        Navigation.findNavController(fabDone).navigate(action);

        String title = textTitle.getText().toString();
        String desc = textDescription.getText().toString();
        String date = buttonDate.getText().toString();
        String time = buttonTime.getText().toString();

        int classTaskId = classSelected.getClassId();
        int priority = prioritySelected;

        Task newTask = new Task(title, desc, date, time, priority, classTaskId);

        // Save newly created task to database
        taskViewModel.insertTask(newTask);
    }

    @Override
    public void onDateSelected(String date) {
        buttonDate.setText(date);
    }

    @Override
    public void onTimeSelected(String time) {
        buttonTime.setText(time);
    }
}
