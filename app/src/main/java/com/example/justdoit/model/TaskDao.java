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
    long insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("UPDATE task_table SET completed = :completed WHERE taskId = :taskId")
    void updateComplete(long taskId, boolean completed);

    @Query("UPDATE task_table SET ongoing = :ongoing WHERE taskId = :taskId")
    void updateOngoing(long taskId, boolean ongoing);

    @Query("DELETE FROM task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE completed = 0 AND ongoing = 0")
    LiveData<List<Task>> getAllActiveTasks();

    @Query("SELECT * FROM task_table WHERE completed = 1")
    LiveData<List<Task>> getCompletedTasks();

    @Query("SELECT * FROM task_table WHERE ongoing = 1 AND completed = 0")
    LiveData<List<Task>> getOngoingTasks();

    @Query("SELECT * FROM task_table WHERE taskId = :taskId")
    LiveData<Task> getTask(long taskId);

    @Transaction
    @Query("SELECT * FROM task_class_table WHERE classId IN (SELECT DISTINCT(taskClass) FROM task_table)")
    LiveData<List<ClassWithTask>> getClassesWithTasks();
}
