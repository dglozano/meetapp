package com.example.dglozano.meetapp.actividades;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.dao.Dao;
import com.example.dglozano.meetapp.dao.MockDaoEvento;
import com.example.dglozano.meetapp.fragments.DatePickerFragment;
import com.example.dglozano.meetapp.modelo.Evento;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.Locale;

public class EventoForm extends AppCompatActivity {

    public static final String ID_KEY = "id";
    private static final int PLACE_PICKER_REQUEST = 1;

    private Intent intentOrigen;
    private Boolean flagNuevoEvento;
    private Evento evento;
    private Dao<Evento> dao;

    private EditText et_nombre;
    private EditText et_lugar;
    private Place place;
    private EditText et_fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_form);

        Toolbar myToolbar = findViewById(R.id.toolbar_evento_form);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        // TODO cambiar a daosqlite
        dao = MockDaoEvento.getInstance();

        getViews();
        setListeners();

        intentOrigen = getIntent();
        Bundle extras = intentOrigen.getExtras();
        final Integer id = (extras != null) ? extras.getInt(ID_KEY) : null;
        flagNuevoEvento = id == null;

        if(!flagNuevoEvento) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    evento = dao.getById(id);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarDatosEvento();
                        }
                    });
                }
            };
            Thread t = new Thread(r);
            t.start();
        }

    }

    private void getViews() {
        et_nombre = findViewById(R.id.editText_nombre);
        et_lugar = findViewById(R.id.editText_lugar);
        et_fecha = findViewById(R.id.editText_fecha);
    }

    private void setListeners() {
        et_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        et_lugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showPlacePicker();
                } catch(GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void mostrarDatosEvento() {
        et_nombre.setText(evento.getNombre());
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if(evento.getLugar() != null) {
            try {
                String l = geocoder.getFromLocation(evento.getLugar().latitude, evento.getLugar().longitude, 1).get(0).getAddressLine(0);
                et_lugar.setText(l);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        et_fecha.setText(evento.getFecha());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_Ok:
                guardar();
                setResult(RESULT_OK, intentOrigen);
                finish();
                return true;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void guardar() {
        String nombre = et_nombre.getText().toString();
        String fecha = et_fecha.getText().toString();

        if(flagNuevoEvento) {
            evento = new Evento();
        }

        evento.setNombre(nombre);
        if(place != null) {
            evento.setLugar(place.getLatLng());
        }
        evento.setFecha(fecha);

        dao.save(evento);
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // month + 1 porque los meses van del 0 al 11
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                et_fecha.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showPlacePicker() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                // TODO Ver si se muestra el nombre o la dirección
                et_lugar.setText(place.getAddress());
            }
        }
    }
}
