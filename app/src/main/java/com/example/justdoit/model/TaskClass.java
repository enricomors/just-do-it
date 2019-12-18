package com.example.justdoit.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_class_table")
public class TaskClass {

    @PrimaryKey(autoGenerate = true)
    private int classId;

    private String name;

    public int getClassId() {
        return classId;
    }

    public String getName() {
        return name;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
