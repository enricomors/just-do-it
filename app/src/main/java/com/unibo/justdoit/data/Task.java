/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unibo.justdoit.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Immutable model class for a Task.
 */
@Entity(tableName = "tasks")
public final class Task {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String mId;

    @Nullable
    @ColumnInfo(name = "title")
    private final String mTitle;

    @Nullable
    @ColumnInfo(name = "description")
    private final String mDescription;

    @Nullable
    @ColumnInfo(name = "date")
    private final String mDate;

    @Nullable
    @ColumnInfo(name = "time")
    private final String mTime;

    @ColumnInfo(name = "completed")
    private final boolean mCompleted;

    /**
     * Use this constructor to create a new active Task.
     *
     * @param title       title of the task
     * @param description description of the task
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, @Nullable String date, @Nullable String time) {
        this(title, description, date, time, UUID.randomUUID().toString(), false);
    }

    /**
     * Use this constructor to create an active Task if the Task already has an id (copy of another
     * Task).
     *
     * @param title       title of the task
     * @param description description of the task
     * @param id          id of the task
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, @Nullable String date, @Nullable String time, @NonNull String id) {
        this(title, description, date, time, id, false);
    }

    /**
     * Use this constructor to create a new completed Task.
     *
     * @param title       title of the task
     * @param description description of the task
     * @param completed   true if the task is completed, false if it's active
     */
    @Ignore
    public Task(@Nullable String title, @Nullable String description, @Nullable String date, @Nullable String time, boolean completed) {
        this(title, description, date, time, UUID.randomUUID().toString(), completed);
    }

    /**
     * Use this constructor to specify a completed Task if the Task already has an id (copy of
     * another Task).
     *
     * @param title       title of the task
     * @param description description of the task
     * @param id          id of the task
     * @param completed   true if the task is completed, false if it's active
     */
    public Task(@Nullable String title, @Nullable String description, @NonNull String id,
                @Nullable String date, @Nullable String time, boolean completed) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mDate = date;
        mTime = time;
        mCompleted = completed;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    @Nullable
    public String getDateForList() {
        if (!Strings.isNullOrEmpty(mDate)) {
            return mDate;
        } else {
            return "No date specified";
        }
    }

    @Nullable
    public String getTimeForList() {
        if (!Strings.isNullOrEmpty(mTime)) {
            return mTime;
        } else {
            return "No time specified";
        }
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @Nullable
    public String getDate() {
        return mDate;
    }

    @Nullable
    public String getTime() {
        return mTime;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mTitle) &&
                Strings.isNullOrEmpty(mDescription) &&
                Strings.isNullOrEmpty(mDate) &&
                Strings.isNullOrEmpty(mTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equal(mId, task.mId) &&
                Objects.equal(mTitle, task.mTitle) &&
                Objects.equal(mDescription, task.mDescription) &&
                Objects.equal(mDate, task.mDate) &&
                Objects.equal(mTime, task.mTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription, mDate, mTime);
    }

    @Override
    public String toString() {
        return "Task with title " + mTitle;
    }
}
