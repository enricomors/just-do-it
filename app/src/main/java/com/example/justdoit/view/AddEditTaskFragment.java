package com.example.justdoit.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justdoit.R;
import com.example.justdoit.model.Task;
import com.example.justdoit.model.TaskClass;
import com.example.justdoit.viewmodel.TaskClassViewModel;
import com.example.justdoit.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditTaskFragment extends Fragment
        implements DatePickerFragment.DatePickerFragmentEvents,
        TimePickerFragment.TimePickerFragmentEvents {

    @BindView(R.id.fabDone)
    FloatingActionButton fabDone;

    @BindView(R.id.button_date)
    Button buttonDate;

    @BindView(R.id.button_time)
    Button buttonTime;

    @BindView(R.id.edit_text_description)
    EditText textDescription;

    @BindView(R.id.edit_text_title)
    EditText textTitle;

    @BindView(R.id.text_view_class)
    TextView textViewClass;

    @BindView(R.id.text_view_priority)
    TextView textViewPriority;

    private TaskViewModel taskViewModel;
    private TaskClassViewModel taskClassViewModel;
    private ArrayAdapter<String> classAdapter;
    private ArrayAdapter<CharSequence> priorityAdapter;


    private Task taskToUpdate;
    private int taskID;

    private ArrayList<String> classNames = new ArrayList<>();

    public AddEditTaskFragment() {
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
            for (TaskClass c : taskClasses) {
                classNames.add(c.getName());
            }
        });

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        taskID = AddEditTaskFragmentArgs.fromBundle(getArguments()).getTaskID();

        if (taskID != 0) {
            taskViewModel.getTask(taskID).observe(this, task -> {
                textTitle.setText(task.getTitle());
                textDescription.setText(task.getDescription());
                buttonDate.setText(task.getDate());
                buttonTime.setText(task.getTime());
                //TODO: priority and class
                textViewPriority.setText(String.valueOf(task.getPriority()));
                textViewClass.setText(task.getTaskClass());
                taskToUpdate = task;
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabDone.setOnClickListener(v -> {
            if (taskID != 0) {
                onTaskUpdate();
            } else {
                onTaskSave();
            }
        });
        buttonTime.setOnClickListener(v -> {
            // open date picker
            DialogFragment timePickerFragment = new TimePickerFragment();
            ((TimePickerFragment) timePickerFragment).setTimePickerFragmentEvents(AddEditTaskFragment.this);
            timePickerFragment.show(getFragmentManager(), "timePicker");
        });
        buttonDate.setOnClickListener(v -> {
            // open time picker
            DialogFragment datePickerFragment = new DatePickerFragment();
            ((DatePickerFragment) datePickerFragment).setDatePickerFragmentEvents(AddEditTaskFragment.this);
            datePickerFragment.show(getFragmentManager(), "datePicker");
        });
        textViewClass.setOnClickListener(v -> {
            //TODO: open dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose a class");
            CharSequence[] classes = classNames.toArray(new CharSequence[classNames.size()]);
            builder.setItems(classes, (dialogInterface, i) -> textViewClass.setText(classes[i].toString()));
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        textViewPriority.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose a priority");
            builder.setItems(R.array.priority_array, (dialogInterface, i) -> textViewPriority.setText(String.valueOf(i)));
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void onTaskSave() {
        NavDirections action = AddEditTaskFragmentDirections.actionSaveTask();
        Navigation.findNavController(fabDone).navigate(action);

        String title = textTitle.getText().toString();
        String desc = textDescription.getText().toString();
        String date = buttonDate.getText().toString();
        String time = buttonTime.getText().toString();
        String taskClass = textViewClass.getText().toString();
        int priority = Integer.valueOf(textViewPriority.getText().toString());

        Task newTask = new Task(title, desc, date, time, priority, taskClass);
        taskViewModel.insertTask(newTask);

        Toast.makeText(getContext(), "Task added to database", Toast.LENGTH_SHORT).show();
    }

    private void onTaskUpdate() {
        NavDirections action = AddEditTaskFragmentDirections.actionSaveTask();
        Navigation.findNavController(fabDone).navigate(action);

        taskToUpdate.setTitle(textTitle.getText().toString());
        taskToUpdate.setDescription(textDescription.getText().toString());
        taskToUpdate.setDate(buttonDate.getText().toString());
        taskToUpdate.setTime(buttonTime.getText().toString());
        taskToUpdate.setTaskClass(textViewClass.getText().toString());
        taskToUpdate.setPriority(Integer.valueOf(textViewPriority.getText().toString()));

        taskViewModel.updateTask(taskToUpdate);

        Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();
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
