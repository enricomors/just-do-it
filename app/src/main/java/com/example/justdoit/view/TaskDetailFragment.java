package com.example.justdoit.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justdoit.R;
import com.example.justdoit.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

    @BindView(R.id.fab_edit_task)
    FloatingActionButton fabEdit;

    @BindView(R.id.text_view_title)
    TextView textViewTitle;

    @BindView(R.id.text_view_description)
    TextView textViewDescription;

    @BindView(R.id.text_view_date)
    TextView textViewDate;

    @BindView(R.id.text_view_time)
    TextView textViewTime;

    @BindView(R.id.text_view_priority)
    TextView textViewPriority;

    @BindView(R.id.label_class)
    TextView textViewClass;

    @BindView(R.id.check_complete)
    CheckBox checkBoxComplete;

    @BindView(R.id.check_ongoing)
    CheckBox checkBoxOngoing;

    private long taskID;

    private TaskViewModel taskViewModel;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        ButterKnife.bind(this, view);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        taskID = TaskDetailFragmentArgs.fromBundle(getArguments()).getTaskID();

        taskViewModel.getTask(taskID).observe(this, task -> {
            textViewTitle.setText(task.getTitle());
            textViewDescription.setText(task.getDescription());
            textViewDate.setText(task.getDate());
            textViewTime.setText(task.getTime());
            textViewPriority.setText(String.valueOf(task.getPriority()));
            textViewClass.setText(task.getTaskClass());
            checkBoxComplete.setChecked(task.isCompleted());
            checkBoxOngoing.setChecked(task.isOngoing());
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabEdit.setOnClickListener(v -> {
            TaskDetailFragmentDirections.ActionEditTask action =
                    TaskDetailFragmentDirections.actionEditTask();
            action.setTaskID(taskID);
            NavHostFragment.findNavController(this).navigate(action);
        });

        checkBoxComplete.setOnClickListener(v -> {
            boolean checked = ((CheckBox) v).isChecked();
            updateComplete(checked, taskID);
        });

        checkBoxOngoing.setOnClickListener(v -> {
            boolean checked = ((CheckBox) v).isChecked();
            updateOngoing(checked, taskID);
        });
    }

    private void updateComplete(boolean complete, long taskID) {
        taskViewModel.updateComplete(taskID, complete);
        backToList(getString(R.string.task_completed));
    }

    private void updateOngoing(boolean ongoing, long taskID) {
        taskViewModel.updateOngoing(taskID, ongoing);
        backToList(getString(R.string.task_started_now));
    }

    private void backToList(String message) {
        TaskDetailFragmentDirections.ActionBackToList action =
                TaskDetailFragmentDirections.actionBackToList();
        NavHostFragment.findNavController(this).navigate(action);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
