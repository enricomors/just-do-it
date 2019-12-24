package com.example.justdoit.model;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.justdoit.view.DeleteTaskFragment;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<ClassWithTask>> classesWithTasks;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        this.taskDao = database.taskDao();
        this.allTasks = taskDao.getAllTasks();
        this.classesWithTasks = taskDao.getClassesWithTasks();
    }

    public void insertTask(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteTask(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }
    //TODO: implement all other methods

    /** Room will automatically execute the database operations that returns
     LiveData on the background thread, so we don'd need to handle this. */

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<ClassWithTask>> getClassesWithTasks() {
        return classesWithTasks;
    }

    /*
    public LiveData<List<ClassWithTask>> getClassesWithTasks() {
        return classesWithTasks;
    }*/

    /** ASYNC TASKS: for other database operations, we need to execute the call
     * on the background thread ourselves, because database operations aren't
     * allowed by Room, since this will cause a crash in our application.
     */

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

    public static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteTask(tasks[0]);
            return null;
        }
    }
}
