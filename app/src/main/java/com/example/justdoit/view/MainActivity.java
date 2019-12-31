package com.example.justdoit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.justdoit.R;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up the toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // set up the navigation controller
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
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
}
