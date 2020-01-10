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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {


    private TaskViewModel taskViewModel;

    private int activeNum;
    private int completeNum;
    private int ongoingNum;

    private List<PieEntry> pieEntries = new ArrayList<>();

    private List<Task> active = new ArrayList<>();
    private List<Task> completed = new ArrayList<>();
    private List<Task> ongoing = new ArrayList<>();

    @BindView(R.id.tw_all_value)
    TextView textViewAllNumber;

    @BindView(R.id.tv_active)
    TextView textViewActive;

    @BindView(R.id.tv_completed)
    TextView textViewCompleted;

    @BindView(R.id.tv_ongoing)
    TextView textViewOngoing;

    @BindView(R.id.pie_chart)
    PieChart chart;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PieDataSet dataSet = new PieDataSet(pieEntries, "Tasks data");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        chart.setData(data);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            textViewAllNumber.setText(String.valueOf(tasks.size()));
        });

        taskViewModel.getOngoingTasks().observe(this, tasks -> {
            pieEntries.add(new PieEntry((float) tasks.size(), "Ongoing"));
            chart.notifyDataSetChanged();
            chart.invalidate();
        });

        taskViewModel.getActiveTasks().observe(this, tasks -> {
            pieEntries.add(new PieEntry((float) tasks.size(), "Active"));
            chart.notifyDataSetChanged();
            chart.invalidate();
        });

        taskViewModel.getCompletedTasks().observe(this, tasks -> {
            pieEntries.add(new PieEntry((float) tasks.size(), "Complete"));
            completeNum = tasks.size();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
    }
}
