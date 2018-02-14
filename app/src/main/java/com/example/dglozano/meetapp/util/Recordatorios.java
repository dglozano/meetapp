package com.example.dglozano.meetapp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.dglozano.meetapp.NotifyService;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class Recordatorios {

    public Recordatorios() {
    }

    public void recordatorioEvento(Context context, int idEvento, Date fecha) {
        Context ctx = context.getApplicationContext();
        Intent myIntent = new Intent(ctx.getApplicationContext(), NotifyService.class);
        myIntent.putExtra("id", idEvento);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(ctx, idEvento, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // se obtiene el dia anterior (si se pone fecha 0 en el constructor se obtiene el último día del mes anterior)
        fecha = new Date(fecha.getYear(), fecha.getMonth(), fecha.getDate() - 1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 38);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.DAY_OF_MONTH, fecha.getDate());
        calendar.set(Calendar.MONTH, fecha.getMonth());
        calendar.set(Calendar.YEAR, fecha.getYear() + 1900);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void eliminarRecordatorio(Context context, int idEvento) {
        Context ctx = context.getApplicationContext();
        Intent myIntent = new Intent(ctx.getApplicationContext(), NotifyService.class);
        myIntent.putExtra("id", idEvento);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(ctx, idEvento, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
