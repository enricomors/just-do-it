package com.example.justdoit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ClassWithTask {

    @Embedded
    public TaskClass taskClass;

    @Relation(
            parentColumn = "name",
            entityColumn = "taskClass"
    )
    public List<Task> allTasks;
}
