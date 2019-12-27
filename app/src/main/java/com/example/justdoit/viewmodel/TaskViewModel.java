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

    public TaskViewModel(@NonNull Application application) {
        super(application);

        repository = new TaskRepository(application);
        // classesWithTasks = repository.getClassesWithTasks();
    }

    public void insertTask(Task task) {
        repository.insertTask(task);
    }

    public void deleteTask(Task task) {
        repository.deleteTask(task);
    }

    public void updateTask(Task task) {
        repository.updateTask(task);
    }

    public LiveData<Task> getTask(int taskID) {
        return repository.getTask(taskID);
    }

    public LiveData<List<Task>> getAllTasks() {
        return repository.getAllTasks();
    }

    public LiveData<List<ClassWithTask>> getAllClassesWithTask() {
        return repository.getClassesWithTasks();
    }
}
