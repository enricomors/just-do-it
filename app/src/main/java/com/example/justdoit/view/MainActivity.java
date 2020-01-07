package com.example.justdoit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.justdoit.AddClassFragmentDirections;
import com.example.justdoit.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavController navController;

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private static String CHANNEL_ID = "Tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            case R.id.action_statistics:
                // statistiche
                statistics();
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
        TaskListFragmentDirections.ActionFilterTask action =
                TaskListFragmentDirections.actionFilterTask();
        action.setFilerType(filterType);
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
