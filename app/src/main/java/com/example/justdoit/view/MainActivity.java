package com.example.justdoit.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.justdoit.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavController navController;

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get task id from intent
        long taskID = getIntent().getLongExtra("TaskID", 0L);

        // set up the toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // set up the navigation controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupWithNavController(myToolbar, navController, appBarConfiguration);
        navigationView.setNavigationItemSelectedListener(this);

        // if task id is not 0, navigate to TaskDetailFragment
        if (taskID != 0L) {
            taskDetail(taskID);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (DrawerLayout) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_active_tasks:
                filterTasks(0);
                return true;
            case R.id.action_all_tasks:
                // schermata con tutte le task, sia attive che completate
                filterTasks(1);
                return true;
            case R.id.action_completed_tasks:
                filterTasks(2);
                // schermata con task completate
                return true;
            case R.id.action_ongoing:
                filterTasks(3);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_task_list:
                filterTasks(0);
                break;
            case R.id.nav_stats:
                statistics();
                break;
            case R.id.nav_new_class:
                addClass();
                break;
        }
        return true;
    }

    private void filterTasks(int filterType) {
        int destID = navController.getCurrentDestination().getId();
        switch (destID) {
            case R.id.taskListFragment:
                TaskListFragmentDirections.ActionFilterTask action =
                        TaskListFragmentDirections.actionFilterTask();
                action.setFilerType(filterType);
                navController.navigate(action);
                break;
            case R.id.addTaskFragment:
                AddEditTaskFragmentDirections.ActionSaveTask action1 =
                        AddEditTaskFragmentDirections.actionSaveTask();
                action1.setFilerType(filterType);
                navController.navigate(action1);
                break;
            case R.id.addClassFragment:
                AddClassFragmentDirections.ActionClassAdded action2 =
                        AddClassFragmentDirections.actionClassAdded();
                action2.setFilerType(filterType);
                navController.navigate(action2);
                break;
            case R.id.taskDetailFragment:
                TaskDetailFragmentDirections.ActionBackToList action3 =
                        TaskDetailFragmentDirections.actionBackToList();
                action3.setFilerType(filterType);
                navController.navigate(action3);
                break;
            case R.id.statisticsFragment:

        }

    }

    private void taskDetail(long taskID) {
        TaskListFragmentDirections.ActionTaskDetail action =
                TaskListFragmentDirections.actionTaskDetail();
        action.setTaskID(taskID);
        navController.navigate(action);
    }

    private void statistics() {
        NavDirections action = TaskListFragmentDirections.actionStatistics();
        navController.navigate(action);
    }

    private void addClass() {
        NavDirections action = TaskListFragmentDirections.actionAddClass();
        navController.navigate(action);
    }
}
