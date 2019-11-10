package com.unibo.justdoit.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import android.widget.CalendarView;
import android.widget.TextView;


import com.unibo.justdoit.Injection;
import com.unibo.justdoit.R;
import com.unibo.justdoit.util.ActivityUtils;

import java.util.Calendar;

/**
 * Activity per visualizzare la vista del Calendario con tutte le icone rappresentanti le task.
 */
public class CalendarViewActivity extends AppCompatActivity {

    //drawer
    private DrawerLayout mDrawerLayout;

    private CalendarView calendar;

    private TextView dateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up the view for the activity
        setContentView(R.layout.calendarview_act);
        calendar = (CalendarView) findViewById(R.id.calendar);
        dateView = (TextView) findViewById(R.id.date_view);

        // set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.calendar_title);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // set up the navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // set up the fragment
        CalendarViewFragment calendarViewFragment = (CalendarViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (calendarViewFragment == null) {
            calendarViewFragment = CalendarViewFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    calendarViewFragment, R.id.contentFrame);
        }

        // set up the presenter
        // new CalendarViewPresenter(
         //       Injection.provideTasksRepository(getApplicationContext()), calendarViewFragment);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.calendar_view_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.statistics_navigation_menu_item:
                                // Navigate to Statistics activity
                                break;
                            case R.id.list_navigation_menu_item:
                                NavUtils.navigateUpFromSameTask(CalendarViewActivity.this);
                                break;
                            default:
                                break;
                        }
                        // Close navigation drawer when the item is selected
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

}
