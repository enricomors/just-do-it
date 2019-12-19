package com.example.justdoit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.justdoit.model.TaskClass;
import com.example.justdoit.model.TaskClassRepository;

import java.util.List;

public class TaskClassViewModel extends AndroidViewModel {

    private TaskClassRepository repository;
    private LiveData<List<TaskClass>> allClasses;

    public TaskClassViewModel(@NonNull Application application) {
        super(application);

        repository = new TaskClassRepository(application);
        allClasses = repository.getAllClasses();
    }

    public void insertClass(TaskClass taskClass) {
        repository.insertClass(taskClass);
    }

    public LiveData<List<TaskClass>> getAllClasses() {
        return allClasses;
    }
}
