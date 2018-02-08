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
import com.example.dglozano.meetapp.dao.Dao;
import com.example.dglozano.meetapp.dao.MockDaoParticipante;
import com.example.dglozano.meetapp.dao.MockDaoTarea;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

public class TareaForm extends AppCompatActivity {

    public static final String ID_KEY = "id";

    private Intent intentOrigen;
    private Boolean flagNuevaTarea;
    private Tarea tarea;
    private Dao<Tarea> dao;
    private Dao<Participante> daoParticipante;

    private EditText et_titulo;
    private Spinner spinner_encargado;
    private EditText et_descripcion;

    private List<Participante> listaParticipantes;
    private ArrayAdapter<Participante> adapterParticipantes;

    private final String SELECCIONAR = "Sin Asignar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_form);

        Toolbar myToolbar = findViewById(R.id.toolbar_tarea_form);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // TODO reemplazar por daosqlite
        dao = MockDaoTarea.getInstance();
        daoParticipante = MockDaoParticipante.getInstance();

        getViews();
        inicializarSpinner(0); //0 para que no seleccione nada

        intentOrigen = getIntent();
        Bundle extras = intentOrigen.getExtras();
        // TODO ver que la clave coincida
        final Integer id = (extras != null) ? extras.getInt(ID_KEY) : null;
        flagNuevaTarea = id == null;

        if(!flagNuevaTarea) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    tarea = dao.getById(id);
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
                List<Participante> lista = daoParticipante.getAll();
                listaParticipantes.clear();
                listaParticipantes.addAll(lista);
                listaParticipantes.add(0, new Participante(-1, SELECCIONAR, 1));
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
        if(tarea.getPersonaAsignada().getId() == null) {
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
        String titulo = et_titulo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        Participante encargado = (Participante) spinner_encargado.getSelectedItem();
        EstadoTarea estado = EstadoTarea.FINALIZADA;
        if(flagNuevaTarea) {
            tarea = new Tarea();
        }
        if(flagNuevaTarea || !flagNuevaTarea && tarea.getEstadoTarea() != EstadoTarea.FINALIZADA) {
            if(SELECCIONAR.equals(encargado.getNombreApellido())) {
                tarea.setPersonaAsignada(Participante.getParticipanteSinAsignar());
                estado = EstadoTarea.SIN_ASIGNAR;
            } else {
                tarea.setPersonaAsignada(encargado);
                estado = EstadoTarea.EN_PROGRESO;
            }
        }
        tarea.setTitulo(titulo);
        tarea.setEstadoTarea(estado);
        tarea.setDescripcion(descripcion);

        dao.save(tarea);
    }
}
