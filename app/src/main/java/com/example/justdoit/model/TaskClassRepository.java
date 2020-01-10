package com.example.justdoit.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import java.util.List;

public class TaskClassRepository {

    private TaskClassDao taskClassDao;
    private LiveData<List<TaskClass>> allClasses;

    public TaskClassRepository(Application application) {
        TaskDatabase taskDatabase = TaskDatabase.getInstance(application);
        this.taskClassDao = taskDatabase.taskClassDao();
        this.allClasses = taskClassDao.getAllClasses();
    }

    public LiveData<List<TaskClass>> getAllClasses() {
        return allClasses;
    }

    public void insertClass(TaskClass taskClass) {
        new InsertClassAsyncTask(taskClassDao).execute(taskClass);
    }

    public void deleteClass(TaskClass taskClass) {
        new DeleteClassAsyncTask(taskClassDao).execute(taskClass);
    }

    //TODO: implement all other methods

    /** ASYNC TASKS */

    public static class InsertClassAsyncTask extends AsyncTask<TaskClass, Void, Void> {

        private TaskClassDao taskClassDao;

        private InsertClassAsyncTask(TaskClassDao taskClassDao) {
            this.taskClassDao = taskClassDao;
        }

        @Override
        protected Void doInBackground(TaskClass... taskClasses) {
            taskClassDao.insertClass(taskClasses[0]);
            return null;
        }
    }


    public static class DeleteClassAsyncTask extends AsyncTask<TaskClass, Void, Void> {

        private TaskClassDao taskClassDao;

        private DeleteClassAsyncTask(TaskClassDao taskClassDao) {
            this.taskClassDao = taskClassDao;
        }

        @Override
        protected Void doInBackground(TaskClass... taskClasses) {
            taskClassDao.deleteClass(taskClasses[0]);
            return null;
        }
    }

}
