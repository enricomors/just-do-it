package com.example.justdoit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskClassDao {

    @Insert
    void insertClass(TaskClass taskClass);

    @Delete
    void deleteClass(TaskClass taskClass);

    @Query("SELECT * FROM task_class_table")
    LiveData<List<TaskClass>> getAllClasses();

    @Query("SELECT * FROM task_class_table WHERE classId = :classId")
    TaskClass getClass(int classId);
}
