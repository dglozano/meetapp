package com.example.dglozano.meetapp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.dglozano.meetapp.NotifyService;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class Recordatorios {

    public Recordatorios() {
    }

    public void crearRecordatorioEvento(Context context, Integer idEvento, Integer año, Integer mes, Integer dia) {
        this.recordatorioEvento(false, context, idEvento, año, mes, dia);
    }

    public void actualizarRecordatorioEvento(Context context, Integer idEvento, Integer año, Integer mes, Integer dia) {
        this.recordatorioEvento(true, context, idEvento, año, mes, dia);
    }

    private void recordatorioEvento(boolean cancelar, Context context, Integer idEvento, Integer año, Integer mes, Integer dia) {
        Context ctx = context.getApplicationContext();
        Intent myIntent = new Intent(ctx.getApplicationContext(), NotifyService.class);
        myIntent.putExtra("id", idEvento);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(ctx, idEvento, myIntent, 0);

        if(cancelar) {
            alarmManager.cancel(pendingIntent);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 8);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.MONTH, mes - 1);
        calendar.set(Calendar.YEAR, año);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
