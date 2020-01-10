package com.example.justdoit.view;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Collections;
import java.util.List;

import javax.crypto.AEADBadTagException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class  TaskListFragment extends Fragment implements TaskListAdapter.OnTaskClickListener,
        TaskListAdapter.OnTaskCompleteListener {

    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    @BindView(R.id.task_list)
    RecyclerView tasksList;

    @BindView(R.id.spinner_class)
    Spinner spinnerClass;

    @BindView(R.id.spinner_priority)
    Spinner spinnerPriority;

    @BindView(R.id.btn_filter)
    Button btnFilter;

    private TaskViewModel viewModel;
    private TaskClassViewModel viewModelClass;

    private TaskListAdapter adapter = new TaskListAdapter(this, this);

    private ArrayList<String> classNames = new ArrayList<>();
    private ArrayList<Task> myTasks = new ArrayList<>();

    private int filterType;
    private String classFilter;
    private String priorityFilter;

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

        classNames.clear();

        fabAdd.setOnClickListener(v -> onAddTask());

        btnFilter.setOnClickListener(v -> filterTasks(classFilter, priorityFilter));

        filterType = TaskListFragmentArgs.fromBundle(getArguments()).getFilerType();

        // instantiate the view model
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        viewModelClass = ViewModelProviders.of(this).get(TaskClassViewModel.class);

        adapter.setTasksList(myTasks);

        if (filterType == 0) {
            viewModel.getActiveTasks().observe(this, tasks -> {
                myTasks.clear();
                Collections.sort(tasks, Task.BY_DEADLINE);
                for (Task task : tasks) {
                    myTasks.add(task);
                }
                adapter.notifyDataSetChanged();
            });
        }

        if (filterType == 1) {
            viewModel.getAllTasks().observe(this, tasks -> {
                myTasks.clear();
                Collections.sort(tasks, Task.BY_DEADLINE);
                for (Task task : tasks) {
                    myTasks.add(task);
                }
                adapter.notifyDataSetChanged();
            });
        }

        if (filterType == 2) {
            viewModel.getCompletedTasks().observe(this, tasks -> {
                myTasks.clear();
                Collections.sort(tasks, Task.BY_DEADLINE);
                for (Task task : tasks) {
                    myTasks.add(task);
                }
                adapter.notifyDataSetChanged();
            });
        }

        if (filterType == 3) {
            viewModel.getOngoingTasks().observe(this, tasks -> {
                myTasks.clear();
                Collections.sort(tasks, Task.BY_DEADLINE);
                for (Task task : tasks) {
                    myTasks.add(task);
                }
                adapter.notifyDataSetChanged();
            });
        }

        // viewModel.getAllTasks().observe(this, tasks -> adapter.setTasksList(tasks));
        // setup spinner priority
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        // setup spinner class
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, classNames);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        viewModelClass.getAllClasses().observe(this, taskClasses -> {
            for (TaskClass taskClass : taskClasses) {
                classNames.add(taskClass.getName());
            }
            classAdapter.notifyDataSetChanged();
        });

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classFilter = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priorityFilter = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        // set adapter
        //adapter.setTasksList(mTasks);
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

    private void filterTasks(String classFilter, String priorityFilter) {
        ArrayList<Task> filteredTasks = new ArrayList<>();
        adapter.setTasksList(filteredTasks);
        int priority = Integer.valueOf(priorityFilter);
        for (Task task : myTasks) {
            if ((task.getPriority() == priority) && (task.getTaskClass().equals(classFilter))) {
                filteredTasks.add(task);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void onAddTask() {
        NavDirections action = TaskListFragmentDirections.actionAddTask();
        Navigation.findNavController(fabAdd).navigate(action);
    }

    @Override
    public void onItemClick(int position) {
        long clickedTaskID = adapter.getItem(position).getTaskId();
        TaskListFragmentDirections.ActionTaskDetail action =
                TaskListFragmentDirections.actionTaskDetail();
        action.setTaskID(clickedTaskID);
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onTaskComplete(int position, boolean complete) {
        Task completedTask = adapter.getItem(position);
        viewModel.updateComplete(completedTask.getTaskId(), complete);
        Toast.makeText(getContext(), R.string.task_completed, Toast.LENGTH_SHORT).show();
    }
}
