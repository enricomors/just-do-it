package com.example.justdoit.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long taskId;
    private String title;
    private String description;
    private String date;
    private String time;
    private int priority;
    private String taskClass;
    private boolean completed;
    private boolean ongoing;

    public Task(String title, String description, String date, String time, int priority,
                String taskClass, boolean completed, boolean ongoing) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.priority = priority;
        this.taskClass = taskClass;
        this.completed = completed;
        this.ongoing = ongoing;
    }

    public long getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getPriority() {
        return priority;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }
}
