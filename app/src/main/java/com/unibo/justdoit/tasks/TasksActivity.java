/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.unibo.justdoit.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.unibo.justdoit.Injection;
import com.unibo.justdoit.R;
import com.unibo.justdoit.calendar.CalendarViewActivity;
import com.unibo.justdoit.statistics.StatisticsActivity;
import com.unibo.justdoit.util.ActivityUtils;
import com.unibo.justdoit.util.EspressoIdlingResource;

/**
 * In questa classe troviamo il codice che definisce la logica dell'activity principale della nostra
 * applicazione. Il layout per questa activity è definito in 'res/layout/task_act.xml'. L'activity
 * presenta una toolbar e un layout Drawer, che mostra un menu da cui è possibile scegliere se
 * visualizzare la schermata con tutte le task (quella attuale), la schermata delle statistiche
 * oppure la schermata con il calendario contenente le date di scadenza di tutte le task. Le azioni
 * per il Drawer sono definite in 'res/menu/drawer_actions.xml'.
 * Notiamo come per la visualizzazione delle task venga utilizzato un Fragment, in particolare
 * un istanza della classe TasksFragment (definita in 'tasks').
 *
 * L'activity estende la classe AppCompatActivity, necessaria per usare la Toolbar della libreria di
 * supporto v7 appcompat
 */
public class TasksActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    private TasksPresenter mTasksPresenter;

    /**
     * Lifecycle callback, chiamato quando l'activity viene creata. Associa il layout 'task_act' a
     * questa activity, setta la Toolbar, setta il layout per il Drawer, setta il fragment per
     * mostrare tutte le task, crea il presenter (WHAT IS IS?), e infine ripristina, se presente, lo
     * stato dell'istanza precedente.
     * @param savedInstanceState stato salvato in precedenza
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // associa l'activity al layout
        setContentView(R.layout.tasks_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set a Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        // recupera l'istanza di ActionBar
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        TasksFragment tasksFragment =
                (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = TasksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }

        // Create the presenter
        mTasksPresenter = new TasksPresenter(
                Injection.provideTasksRepository(getApplicationContext()), tasksFragment);

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            TasksFilterType currentFiltering =
                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTasksPresenter.setFiltering(currentFiltering);
        }
    }

    /**
     * Chiamato quando è presente uno stato di un'istanza precedente dell'activity.
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Implementa il comportamento del Drawer quando l'utente clicca su uno degli elementi
     * @param navigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.statistics_navigation_menu_item:
                                Intent statIntent =
                                        new Intent(TasksActivity.this, StatisticsActivity.class);
                                startActivity(statIntent);
                                break;
                            case R.id.calendar_view_navigation_menu_item:
                                // Apre activity per calendar view
                                Intent calIntent =
                                        new Intent(TasksActivity.this, CalendarViewActivity.class);
                                startActivity(calIntent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
