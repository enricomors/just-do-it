package com.example.justdoit.model;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.justdoit.util.AlarmReceiver;

import java.util.Calendar;
import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private Application application;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> activeTasks;
    private LiveData<List<Task>> completedTasks;
    private LiveData<List<Task>> ongoingTasks;
    private LiveData<List<ClassWithTask>> classesWithTasks;

    public TaskRepository(Application application) {
        this.application = application;
        TaskDatabase database = TaskDatabase.getInstance(application);
        this.taskDao = database.taskDao();
        this.allTasks = taskDao.getAllTasks();
        this.activeTasks = taskDao.getAllActiveTasks();
        this.ongoingTasks = taskDao.getOngoingTasks();
        this.completedTasks = taskDao.getCompletedTasks();
        this.classesWithTasks = taskDao.getClassesWithTasks();
    }

    public void insertTask(Task task, Calendar deadline, String title) {
         new InsertTaskAsyncTask(taskDao, deadline, title, this).execute(task);
    }

    public void deleteTask(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void updateTask(Task task, Calendar deadline, String title, long id) {
        new UpdateTaskAsyncTask(taskDao, deadline, title, id, this).execute(task);
    }

    public void updateComplete(long taskID, boolean completed) {
        new UpdateCompletedAsyncTask(taskDao, taskID, completed).execute();
    }

    public void updateOngoing(long taskID, boolean ongoing) {
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

    public LiveData<Task> getTask(long taskID) {
        return taskDao.getTask(taskID);
    }

    public LiveData<List<Task>> getActiveTasks() { return activeTasks; }

    public LiveData<List<Task>> getCompletedTasks() {
        return completedTasks;
    }

    public LiveData<List<Task>> getOngoingTasks() { return ongoingTasks; }

    private void setAlarm(Calendar c, long id, String title) {
        AlarmManager alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(application, AlarmReceiver.class);
        //intent.putExtra("Id", id);
        intent.putExtra("TaskID", id);
        intent.putExtra("Title", title);
        // TODO: requestCode must be unique for every pendingIntent
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    /*
    public LiveData<List<ClassWithTask>> getClassesWithTasks() {
        return classesWithTasks;
    }*/

    /** ASYNC TASKS: for other database operations, we need to execute the call
     * on the background thread ourselves, because database operations aren't
     * allowed by Room, since this will cause a crash in our application.
     */

    /**
     * AsyncTask for inserting new tasks into the database
     */
    public static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Long> {

        private TaskDao taskDao;
        private TaskRepository repository;
        private String title;
        private Calendar deadline;

        private InsertTaskAsyncTask(TaskDao taskDao, Calendar deadline, String title,
                                    TaskRepository repository) {
            this.taskDao = taskDao;
            this.deadline = deadline;
            this.title = title;
            this.repository = repository;
        }

        @Override
        protected Long doInBackground(Task... tasks) {
            long taskID = taskDao.insertTask(tasks[0]);
            return taskID;
        }

        @Override
        protected void onPostExecute(Long taskID) {
            super.onPostExecute(taskID);
            repository.setAlarm(deadline, taskID, title);
        }
    }

    /**
     * AsyncTask for deleting an existing task in the database
     */
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

    /**
     * AsyncTask for updating an existing task in the database
     */
    public static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;
        private Calendar deadline;
        private String title;
        private TaskRepository repository;
        private long id;

        private UpdateTaskAsyncTask(TaskDao taskDao, Calendar deadline, String title, long id,
                                    TaskRepository repository) {
            this.taskDao = taskDao;
            this.deadline = deadline;
            this.title = title;
            this.id = id;
            this.repository = repository;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTask(tasks[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            repository.setAlarm(deadline, id, title);
        }
    }

    /**
     * AsyncTask for setting an existing task as complete
     */
    public static class UpdateCompletedAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;
        private boolean completed;
        private long taskID;

        private UpdateCompletedAsyncTask(TaskDao taskDao, long taskID, boolean completed) {
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

    /**
     * AsyncTask for setting an existing task as ongoing
     */
    public static class UpdateOngoingAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;
        private boolean ongoing;
        private long taskID;

        private UpdateOngoingAsyncTask(TaskDao taskDao, long taskID, boolean ongoing) {
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
