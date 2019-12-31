package com.example.justdoit.view;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.justdoit.R;
import com.example.justdoit.model.Task;
import com.example.justdoit.viewmodel.TaskViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    @BindView(R.id.text_view_active)
    TextView textViewActive;

    @BindView(R.id.text_view_completed)
    TextView textViewCompleted;

    @BindView(R.id.active_number)
    TextView textViewActiveNumber;

    @BindView(R.id.completed_number)
    TextView textViewCompletedNumber;

    @BindView(R.id.text_view_all)
    TextView textViewAll;

    @BindView(R.id.text_view_all_number)
    TextView textViewAllNumber;

    private TaskViewModel taskViewModel;
    private int activeNum;
    private int completeNum;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            textViewAllNumber.setText(String.valueOf(tasks.size()));
        });
        taskViewModel.getActiveTasks().observe(this, tasks -> {
            textViewActiveNumber.setText(String.valueOf(tasks.size()));
            activeNum = tasks.size();
        });
        taskViewModel.getCompletedTasks().observe(this, tasks -> {
            textViewCompletedNumber.setText(String.valueOf(tasks.size()));
            completeNum = tasks.size();
        });

        return view;
    }
}
