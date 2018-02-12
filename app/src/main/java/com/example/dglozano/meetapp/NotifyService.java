package com.example.dglozano.meetapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.example.dglozano.meetapp.actividades.EventoActivity;
import com.example.dglozano.meetapp.adapters.EventoItemAdapter;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.modelo.Evento;

public class NotifyService extends IntentService {
    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SQLiteDaoEvento dao = new SQLiteDaoEvento(getApplicationContext());
        Integer idEvento = intent.getExtras().getInt("id");
        if(idEvento != null) {
            Evento evento = dao.getById(idEvento);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent newIntent = new Intent(this.getApplicationContext(), EventoActivity.class);
            newIntent.putExtra(EventoItemAdapter.EXTRA_EVENTO_ID, idEvento);
            PendingIntent pi = PendingIntent.getActivity(this, 0, newIntent, 0);
            int icon = android.R.drawable.sym_def_app_icon;
            Notification not = new Notification.Builder(this)
                    .setSmallIcon(icon)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setContentTitle("Un evento mañana!")
                    .setContentText(evento.getNombre())
                    .build();
            nm.notify(1, not);
        }
    }
}
