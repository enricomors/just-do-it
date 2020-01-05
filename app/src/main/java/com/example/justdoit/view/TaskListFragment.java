package com.example.justdoit.view;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.justdoit.R;
import com.example.justdoit.model.Task;
import com.example.justdoit.viewmodel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class  TaskListFragment extends Fragment implements TaskListAdapter.OnTaskClickListener,
        TaskListAdapter.OnTaskCompleteListener {

    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    @BindView(R.id.task_list)
    RecyclerView tasksList;

    private TaskViewModel viewModel;

    private TaskListAdapter adapter = new TaskListAdapter(this, this);

    private int filterType;

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabAdd.setOnClickListener(v -> onAddTask());

        filterType = TaskListFragmentArgs.fromBundle(getArguments()).getFilerType();

        // instantiate the view model
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        if (filterType == 0) {
            viewModel.getActiveTasks().observe(this, tasks -> adapter.setTasksList(tasks));
        }

        if (filterType == 1) {
            viewModel.getAllTasks().observe(this, tasks -> adapter.setTasksList(tasks));
        }

        if (filterType == 2) {
            viewModel.getCompletedTasks().observe(this, tasks -> adapter.setTasksList(tasks));
        }

        // viewModel.getAllTasks().observe(this, tasks -> adapter.setTasksList(tasks));

        // set adapter
        tasksList.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksList.setAdapter(adapter);

        // helper to make the view swipable
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Task taskToDelete = adapter.getItem(viewHolder.getAdapterPosition());
                DeleteTaskFragment fragment = new DeleteTaskFragment(taskToDelete);
                fragment.show(getFragmentManager(), "Delete Task");
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(tasksList);
    }

    private void onAddTask() {
        NavDirections action = TaskListFragmentDirections.actionAddTask();
        Navigation.findNavController(fabAdd).navigate(action);
    }

    @Override
    public void onItemClick(int position) {
        Task clickedTask = adapter.getItem(position);
        TaskListFragmentDirections.ActionAddTask action =
                TaskListFragmentDirections.actionAddTask();
        action.setTaskID(clickedTask.getTaskId());
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onTaskComplete(int position, boolean complete) {
        Task completedTask = adapter.getItem(position);
        viewModel.updateComplete(completedTask.getTaskId(), complete);
        Toast.makeText(getContext(), R.string.task_completed, Toast.LENGTH_SHORT).show();
    }

    public void changeTaskList(int type) {

    }
}
