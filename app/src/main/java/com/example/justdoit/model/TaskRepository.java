package com.example.justdoit.model;

import android.app.Application;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.justdoit.view.DeleteTaskFragment;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<Task> task;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> activeTasks;
    private LiveData<List<Task>> completedTasks;
    private LiveData<List<Task>> ongoingTasks;
    private LiveData<List<ClassWithTask>> classesWithTasks;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        this.taskDao = database.taskDao();
        this.allTasks = taskDao.getAllTasks();
        this.activeTasks = taskDao.getAllActiveTasks();
        this.ongoingTasks = taskDao.getOngoingTasks();
        this.completedTasks = taskDao.getCompletedTasks();
        this.classesWithTasks = taskDao.getClassesWithTasks();
    }

    public void insertTask(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteTask(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void updateTask(Task task) {
        new UpdateTaskAsyncTask(taskDao).execute(task);
    }

    public void updateComplete(int taskID, boolean completed) {
        new UpdateCompletedAsyncTask(taskDao, taskID, completed).execute();
    }

    public void updateOngoing(int taskID, boolean ongoing) {
        new UpdateOngoingAsyncTask(taskDao, taskID, ongoing).execute();
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

    public LiveData<Task> getTask(int taskID) {
        return taskDao.getTask(taskID);
    }

    public LiveData<List<Task>> getActiveTasks() { return activeTasks; }

    public LiveData<List<Task>> getCompletedTasks() {
        return completedTasks;
    }

    public LiveData<List<Task>> getOngoingTasks() { return ongoingTasks; }

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

    public static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTask(tasks[0]);
            return null;
        }
    }

    public static class UpdateCompletedAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;
        private boolean completed;
        private int taskID;

        private UpdateCompletedAsyncTask(TaskDao taskDao, int taskID, boolean completed) {
            this.taskDao = taskDao;
            this.completed = completed;
            this.taskID = taskID;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.updateComplete(taskID, completed);
            return null;
        }
    }

    public static class UpdateOngoingAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;
        private boolean ongoing;
        private int taskID;

        private UpdateOngoingAsyncTask(TaskDao taskDao, int taskID, boolean ongoing) {
            this.taskDao = taskDao;
            this.ongoing = ongoing;
            this.taskID = taskID;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.updateOngoing(taskID, ongoing);
            return null;
        }
    }
}
