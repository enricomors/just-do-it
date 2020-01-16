package com.example.justdoit.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class, TaskClass.class}, version = 4, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDao taskDao();

    public abstract TaskClassDao taskClassDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, "task_database")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    // callback to populate the database w/ the default classes
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        // called when the database is created for the first time
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskClassDao taskClassDao;

        private PopulateDBAsyncTask(TaskDatabase db) {
            taskClassDao = db.taskClassDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskClassDao.insertClass(new TaskClass("No class"));
            taskClassDao.insertClass(new TaskClass("Work"));
            taskClassDao.insertClass(new TaskClass("Homework"));
            taskClassDao.insertClass(new TaskClass("Family"));
            return null;
        }
    }
}
