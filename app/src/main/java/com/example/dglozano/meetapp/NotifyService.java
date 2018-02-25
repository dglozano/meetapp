package com.example.dglozano.meetapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Geocoder;

import com.example.dglozano.meetapp.actividades.EventoActivity;
import com.example.dglozano.meetapp.adapters.EventoItemAdapter;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.util.AddressFormater;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class NotifyService extends IntentService {
    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SQLiteDaoEvento dao = new SQLiteDaoEvento(getApplicationContext());
        Integer idEvento = intent.getExtras().getInt("id");
        if (idEvento != null) {
            Evento evento = dao.getById(idEvento);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent newIntent = new Intent(this.getApplicationContext(), EventoActivity.class);
            newIntent.putExtra(EventoItemAdapter.EXTRA_EVENTO_ID, idEvento);
            PendingIntent pi = PendingIntent.getActivity(this, 0, newIntent, 0);
            AddressFormater af = new AddressFormater(new Geocoder(this, Locale.getDefault()));

            int icon = R.drawable.ic_cake_black_24dp;
            Notification not = new Notification.Builder(this)
                    .setSmallIcon(icon)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setContentTitle(getResources().getString(R.string.title_notif))
                    .setContentText(String.format(getResources().getString(R.string.body_msg_notif),
                            evento.getNombre(),
                            af.format(evento.getLugar())))
                    .build();
            nm.notify(1, not);
        }
    }
}
