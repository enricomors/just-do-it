package com.example.justdoit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.justdoit.model.ClassWithTask;
import com.example.justdoit.model.Task;
import com.example.justdoit.model.TaskRepository;

import java.util.Calendar;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        repository = new TaskRepository(application);
        // classesWithTasks = repository.getClassesWithTasks();
    }

    public void insertTask(Task task, Calendar deadline, String title) {
        repository.insertTask(task, deadline, title);
    }

    public void deleteTask(Task task) {
        repository.deleteTask(task);
    }

    public void updateTask(Task task, Calendar deadline, String title, long id) {
        repository.updateTask(task, deadline, title, id);
    }

    public void updateComplete(long taskID, boolean complete) {
        repository.updateComplete(taskID, complete);
    }

    public void updateOngoing(long taskID, boolean ongoing) {
        repository.updateOngoing(taskID, ongoing);
    }

    public LiveData<Task> getTask(long taskID) {
        return repository.getTask(taskID);
    }

    public LiveData<List<Task>> getAllTasks() {
        return repository.getAllTasks();
    }

    public LiveData<List<ClassWithTask>> getAllClassesWithTask() {
        return repository.getClassesWithTasks();
    }

    public LiveData<List<Task>> getActiveTasks() {
        return repository.getActiveTasks();
    }

    public LiveData<List<Task>> getCompletedTasks() {
        return repository.getCompletedTasks();
    }

    public LiveData<List<Task>> getOngoingTasks() {
        return repository.getOngoingTasks();
    }
}
