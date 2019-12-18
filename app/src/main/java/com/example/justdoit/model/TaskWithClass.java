package com.example.justdoit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TaskWithClass {

    @Embedded
    public TaskClass taskClass;

    @Relation(
            parentColumn = "classId",
            entityColumn = "classTaskId"
    )
    public List<Task> allTasks;
}
