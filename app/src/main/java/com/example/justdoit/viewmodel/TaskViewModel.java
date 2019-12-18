package com.example.justdoit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.justdoit.model.Task;
import com.example.justdoit.model.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<Task>> allTask;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        repository = new TaskRepository(application);
        allTask = repository.getAllTasks();
    }

    public void insertTask(Task task) {
        repository.insertTask(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTask;
    }
}
