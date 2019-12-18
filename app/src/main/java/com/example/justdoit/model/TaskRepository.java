package com.example.justdoit.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        this.taskDao = database.taskDao();
        this.allTasks = taskDao.getAllTasks();
    }

    public void insertTask(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks[0]);
            return null;
        }
    }
}
