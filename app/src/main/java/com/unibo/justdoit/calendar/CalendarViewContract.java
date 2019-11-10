package com.unibo.justdoit.calendar;

import com.unibo.justdoit.BasePresenter;
import com.unibo.justdoit.BaseView;
import com.unibo.justdoit.data.Task;
import com.unibo.justdoit.tasks.TasksContract;

import java.util.List;

public interface CalendarViewContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);


    }

    interface Presenter extends BasePresenter {

        void loadTasks(boolean forceUpdate);

        void openTaskDetails();
    }

}
