package com.example.justdoit.view;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justdoit.R;
import com.example.justdoit.model.Task;
import com.example.justdoit.model.TaskClass;
import com.example.justdoit.viewmodel.TaskClassViewModel;
import com.example.justdoit.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

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

    // date and time variables
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private String title;
    private String desc;
    private String date;
    private String time;
    private String taskClass;
    private String pNumber;

    private Task taskToUpdate;
    private long taskID;

    private long selectedTime;

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

        fabDone.setOnClickListener(v -> checkFields());

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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.choose_class);
            CharSequence[] classes = classNames.toArray(new CharSequence[classNames.size()]);
            builder.setItems(classes, (dialogInterface, i) -> textViewClass.setText(classes[i].toString()));
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        textViewPriority.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.choose_priority);
            builder.setItems(R.array.priority_array, (dialogInterface, i) -> textViewPriority.setText(String.valueOf(i)));
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void checkFields() {

        String err = "";

        title = textTitle.getText().toString().trim();
        desc = textDescription.getText().toString().trim();

        date = buttonDate.getText().toString();
        time = buttonTime.getText().toString();

        taskClass = textViewClass.getText().toString();
        pNumber = textViewPriority.getText().toString();

        if (title.length() == 0) {
            err += getString(R.string.title_err) + "\n";
         }

        if (desc.length() == 0) {
            desc = getString(R.string.no_desc);
        }

        if (date.length() == 0 || time.length() == 0) {
            err += getString(R.string.no_date_time) + "\n";
        } else {
            Calendar c = getCalendar();

            if (c.before(Calendar.getInstance())) {
                err += getString(R.string.date_time_err) + "\n";
            }
        }

        if (taskClass.length() == 0) {
            taskClass = "No class";
        }

        if (pNumber.length() == 0) {
            pNumber = "0";
        }

        if (err.length() == 0) {
            if (taskID != 0) {
                onTaskUpdate();
            } else {
                onTaskSave();
            }
        } else {
            openDialogError(err);
        }

    }

    private void openDialogError(String errorString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(errorString)
                .setTitle(getString(R.string.dialog_err));
        builder.setPositiveButton(R.string.dialog_ok, (dialogInterface, i) -> {});
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onTaskSave() {
        int priority = Integer.valueOf(pNumber);

        //TODO: da gestire meglio

        Calendar c = getCalendar();
        long deadline = c.getTimeInMillis();

        Task newTask = new Task(title, desc, date, time, priority, taskClass,
                false, false, deadline);
        // startAlarm(c, newTask.getTaskId());

        // pass the new task along w/ calendar representing deadline
        taskViewModel.insertTask(newTask, c, title);

        Toast.makeText(getContext(), "Task saved", Toast.LENGTH_SHORT).show();
        NavDirections action = AddEditTaskFragmentDirections.actionSaveTask();
        Navigation.findNavController(fabDone).navigate(action);
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

        Calendar c = getCalendar();
        long deadline = c.getTimeInMillis();

        taskToUpdate.setDeadlineMillies(deadline);

        // startAlarm(c, taskToUpdate.getTaskId());

        taskViewModel.updateTask(taskToUpdate, c, taskToUpdate.getTitle(),
                taskToUpdate.getTaskId());

        Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        String formattedDate = day + "-" + month + "-" + year;
        buttonDate.setText(formattedDate);
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        String formattedTime = hour + ":" + minute;
        buttonTime.setText(formattedTime);
    }

    private Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        return c;
    }
}
