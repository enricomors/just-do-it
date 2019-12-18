package com.example.justdoit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE taskId = :taskId")
    Task getTask(int taskId);

    @Transaction
    @Query("SELECT * FROM task_class_table")
    List<TaskWithClass> getTasksWithClass();
}
