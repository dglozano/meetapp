package com.example.dglozano.meetapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

public class TareaForm extends AppCompatActivity {

    private Intent intentOrigen;
    private Boolean flagNuevaTarea;
    private Tarea tarea;
    // TODO private Dao dao;

    private EditText et_titulo;
    private Spinner spinner_encargado;
    private EditText et_descripcion;

    private List<Participante> listaParticipantes;
    private ArrayAdapter<Participante> adapterParticipantes;

    private final String SELECCIONAR = "-- seleccionar --";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_form);

        Toolbar myToolbar = findViewById(R.id.toolbar_tarea_form);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        getViews();
        inicializarSpinner(0); //0 para que no seleccione nada

        intentOrigen = getIntent();
        Bundle extras = intentOrigen.getExtras();
        // TODO ver que la clave coincida
        Integer id = (extras != null) ? extras.getInt("id") : null;
        flagNuevaTarea = id == null;

        if(!flagNuevaTarea) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    tarea = null; // TODO dao.getTarea
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarDatosTarea();
                        }
                    });
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }

    private void getViews() {
        et_titulo = findViewById(R.id.editText_titulo);
        spinner_encargado = findViewById(R.id.spinner_encargado);
        et_descripcion = findViewById(R.id.editText_descripcion);
    }

    private void inicializarSpinner(final int selectedPosition) {
        listaParticipantes = new ArrayList<>();
        adapterParticipantes = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaParticipantes);
        spinner_encargado.setAdapter(adapterParticipantes);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<Participante> lista = new ArrayList<>(); // TODO = dao.getParticipantes()
                lista.addAll(Participante.getParticipantesMock()); // TODO quitar esto
                lista.add(0, new Participante(SELECCIONAR, 1));
                listaParticipantes.clear();
                listaParticipantes.addAll(lista);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterParticipantes.notifyDataSetChanged();
                        spinner_encargado.setSelection(selectedPosition);
                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void mostrarDatosTarea() {
        et_titulo.setText(tarea.getTitulo());
        if(tarea.getPersonaAsignada() != null) {
            inicializarSpinner(adapterParticipantes.getPosition(tarea.getPersonaAsignada()));
        } else {
            inicializarSpinner(0);
        }
        et_descripcion.setText(tarea.getDescripcion());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_Ok:
                guardar();
                setResult(RESULT_OK, intentOrigen);
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
        String titulo = et_titulo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        Participante encargado = (Participante) spinner_encargado.getSelectedItem();
        EstadoTarea estado = null;
        if(flagNuevaTarea) {
            tarea = new Tarea();
            int id = 0;
            /*try {
                id = obtenerNuevoID();
                // TODO ver si lo hacemos así. Debería encargarse la capa dao
                */
            tarea.setId(id);/*
            } catch(ExecutionException e) {
                e.printStackTrace();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }*/
        }
        if(flagNuevaTarea || !flagNuevaTarea && tarea.getEstadoTarea() != EstadoTarea.FINALIZADA) {
            if(encargado != null) {
                estado = EstadoTarea.EN_PROGRESO;
            } else {
                estado = EstadoTarea.SIN_ASIGNAR;
            }
        }
        tarea.setTitulo(titulo);
        if(encargado.getNombreApellido().equals(SELECCIONAR)) {
            tarea.setPersonaAsignada(null);
        } else {
            tarea.setPersonaAsignada(encargado);
        }
        tarea.setEstadoTarea(estado);
        tarea.setDescripcion(descripcion);

        // TODO mandar a guardar
    }
}
