package com.example.justdoit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.justdoit.model.ClassWithTask;
import com.example.justdoit.model.Task;
import com.example.justdoit.model.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<Task>> allTask;
    private LiveData<List<ClassWithTask>> classesWithTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        repository = new TaskRepository(application);
        allTask = repository.getAllTasks();
        // classesWithTasks = repository.getClassesWithTasks();
    }

    public void insertTask(Task task) {
        repository.insertTask(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTask;
    }

    public LiveData<List<ClassWithTask>> getClassesWithTasks() {
        return classesWithTasks;
    }
}
