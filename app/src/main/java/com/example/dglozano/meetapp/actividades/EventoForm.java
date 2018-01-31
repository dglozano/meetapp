package com.example.dglozano.meetapp.actividades;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.modelo.Evento;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class EventoForm extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;

    private Intent intentOrigen;
    private Boolean flagNuevoEvento;
    private Evento evento;

    private EditText et_nombre;
    private EditText et_lugar;
    private Place place;
    private EditText et_fecha;
    private Button btnGuardar;
    private Button btnCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_form);

        getViews();
        setListeners();

        intentOrigen = getIntent();
        Bundle extras = intentOrigen.getExtras();
        // TODO ver como se obtiene el evento

        // FIXME asignar el flag en base al evento obtenido
        flagNuevoEvento = true;

    }

    private void getViews() {
        et_nombre = findViewById(R.id.editText_nombre);
        et_lugar = findViewById(R.id.editText_lugar);
        et_fecha = findViewById(R.id.editText_fecha);
        btnGuardar = findViewById(R.id.eventoFormGuardar);
        btnCancelar = findViewById(R.id.eventoFormCancelar);
    }

    private void setListeners() {
        btnGuardar.setOnClickListener(new GuardarListener());
        btnCancelar.setOnClickListener(new CancelarListener());
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
                // TODO hacer esto
                et_lugar.setText(place.getAddress());
            }
        }
    }

    private class GuardarListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String nombre = et_nombre.getText().toString();
            String fecha = et_fecha.getText().toString();

            if(flagNuevoEvento) {
                evento = new Evento();
            }
            evento.setNombre(nombre);
            evento.setLugar(place.getLatLng());
            evento.setFecha(fecha);

            // TODO mandar a guardar

            setResult(RESULT_OK, intentOrigen);
        }
    }

    private class CancelarListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            setResult(RESULT_CANCELED, intentOrigen);
            finish();
        }
    }
}
