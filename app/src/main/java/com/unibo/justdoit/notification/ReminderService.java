package com.unibo.justdoit.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.unibo.justdoit.Injection;
import com.unibo.justdoit.data.Task;
import com.unibo.justdoit.data.source.TasksDataSource;
import com.unibo.justdoit.data.source.TasksRepository;
import com.unibo.justdoit.util.Helper;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReminderService extends Service {

    // serve modo per recuperare le task dal db

    private static final String TAG = ReminderService.class.getSimpleName();

    public static final String ALARM_TRIGGERED = "ALARM_TRIGGERED";

    private boolean alreadyRunning = false;
    private final IBinder mBinder = new ReminderServiceBinder();

    private final TasksRepository mTasksRepository =
            Injection.provideTasksRepository(getApplicationContext());

    private NotificationManager mNotificationManager;
    private AlarmManager alarmManager;
    private NotificationChannel mChannel;
    private NotificationHelper helper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Bundle extras = intent.getExtras();
        // messaggi dall'activity
        if (extras != null) {
            Log.i(TAG, "onBind() with extra message");
        } else {
            Log.i(TAG, "onBind()");
        }
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // qui si scollega dall'oggetto dbHelper
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");

        // viene inizializzato dbHelper con istanza di DatabaseHelper
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        alarmManager = AlarmManagerHolder.getAlarmManager(this);
        helper = new NotificationHelper(this);

        boolean alarmTriggered = false;
        Bundle extras = intent.getExtras();
        if (extras != null)
            alarmTriggered = intent.getExtras().getBoolean(ALARM_TRIGGERED);
        if (alarmTriggered) {
            // crea nuova istanza di task e chiama getParcealable
            // chiamata a handleAlarm passando la nuova task creata come argomento
            // richiama metodo per recuperare la prossima task in scadenza
            // if (Il db restituisce una task)
                // chiama metodo setAlarmForTask() per settare alarm per questa task
        } else {
            // se il servizio è stato avviato per la prima volta
            if (!alreadyRunning) {
                reloadAlarmsFromDB();
                alreadyRunning = true;
                Log.i(TAG, "Service was started the first time.");
            } else {
                Log.i(TAG, "Service was already running.");
            }
        }
        return START_NOT_STICKY; // do not recreate service if the phone runs out of memory
    }

    private void handleAlarm(Task task) {
        String title = task.getTitle();
        // crea la notifica
        NotificationCompat.Builder nb = helper.getNotification(title,
                "Deadline is approaching",
                task);
        // richiama il notification manager
        helper.getManager().notify(Integer.parseInt(task.getId()), nb.build());
    }

    public void reloadAlarmsFromDB() {
        mNotificationManager.cancelAll(); // cancella tutti gli allarmi

        // qui l'altra app recupera dal database quelle task per cui settare un'alarm, che sono:
        // quelle non completate e per cui il "reminder time" è settato prima della data/ora attuale
        // la prossima task in scadenza

        // qui restituiamo soltanto le task non completate, non c'è un controllo sulla data di
        // scadenza.
        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                // lista delle task per cui settare il reminder
                List<Task> tasksToRemind = new ArrayList<Task>();
                // riempie la lista e setta l'allarme per le task attive
                for (Task task : tasks) {
                    if (task.isActive()) {
                        tasksToRemind.add(task);
                        setAlarmForTask(task);
                    }
                }
                if (tasksToRemind.size() == 0) {
                    Log.i(TAG, "No alarms set");
                }
            }

            @Override
            public void onDataNotAvailable() {
                // come gestire questo callback?
            }
        });

    }

    private void setAlarmForTask(Task task) {
        // crea nuovo intent
        Intent alarmIntent = new Intent(this, ReminderService.class);
        // alarmIntent.putExtra() --> PARCEALABLE_KEY
        alarmIntent.putExtra(ALARM_TRIGGERED, true);

        // id della task per cui settiamo l'allarme (prendiamo id della task)
        int alarmId = Integer.parseInt(task.getId());
        PendingIntent pendingAlarmIntent = PendingIntent.getService(
                this,
                alarmId,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();

        // recupera l'ora di scadenza della task (valore long)
        // il problema è che l'altro progetto usa un valore in millisecondi (timestamp) per settare
        // le notifiche, mentre noi abbiamo la stringa della data. Bisogna cambiare il modo facendo
        // tutto basandosi sulla data.
        try {

            long reminderTime = task.getTaskDeadline();

            if (reminderTime != 1 && reminderTime <= Helper.getCurrentTimestamp()) {
                Date date = new Date(TimeUnit.SECONDS.toMillis(Helper.getCurrentTimestamp()));
                calendar.setTime(date);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarmIntent);

                Log.i(TAG, "Alarm set for" + task.getTitle() + " at " + Helper.getDateTime(calendar.getTimeInMillis() / 1000) + " (alarm id: " + alarmId + ")");
            } else if (reminderTime != -1) {
                Date date = new Date(TimeUnit.SECONDS.toMillis(reminderTime));
                calendar.setTime(date);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingAlarmIntent);

                Log.i(TAG, "Alarm set for " + task.getTitle() + " at " + Helper.getDateTime(calendar.getTimeInMillis() / 1000) + " (alarm id: " + alarmId + ")");
            }

        } catch (ParseException ex) {
            Log.i("Exception", ex.getLocalizedMessage());
        }
    }

    public void processTask(Task changedTask) {

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,
                Integer.parseInt(changedTask.getId()),
                new Intent(this, ReminderService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // controlla che l'allarme sia stato settato per questa task
        if (alarmIntent != null) {
            // 1. Cancella il vecchio allarme
            alarmManager.cancel(alarmIntent);
            Log.i(TAG, "Alarm of task " + changedTask.getTitle() + " cancelled. (id =" +changedTask.getId());

            // 2. Cancella la vecchia notifica se esistente
            mNotificationManager.cancel(Integer.parseInt(changedTask.getId()));
            Log.i(TAG, "Notification of task " + changedTask.getTitle() + " deleted (if existed). (id="+changedTask.getId()+")");
        } else {
            Log.i(TAG, "No alarm found for " + changedTask.getTitle() + " (alarm id: " + changedTask.getId() + ")");
        }

        setAlarmForTask(changedTask);
    }

    public class ReminderServiceBinder extends Binder {
        public ReminderService getService() {
            return ReminderService.this;
        }
    }
}
