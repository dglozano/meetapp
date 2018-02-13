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
import com.example.dglozano.meetapp.dao.DaoEvento;
import com.example.dglozano.meetapp.dao.DaoEventoMember;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.example.dglozano.meetapp.dao.SQLiteDaoTarea;
import com.example.dglozano.meetapp.dao.mock.MockDaoTarea;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.util.ArrayList;
import java.util.List;

public class TareaForm extends AppCompatActivity {

    public static final String KEY_TAREA_ID = "idTarea";
    public static final String KEY_EVENTO_ID = "idEvento";
    public static final String KEY_TAREA_NUEVA_FLAG = "tareaNuevaFlag";

    private Intent intentOrigen;
    private Boolean flagNuevaTarea;
    private Tarea tarea;
    private DaoEvento daoEvento;
    private DaoEventoMember<Tarea> daoTarea;
    private DaoEventoMember<Participante> daoParticipante;

    private EditText et_titulo;
    private Spinner spinner_encargado;
    private EditText et_descripcion;

    private List<Participante> listaParticipantes;
    private ArrayAdapter<Participante> adapterParticipantes;

    private Evento evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_form);

        Toolbar myToolbar = findViewById(R.id.toolbar_tarea_form);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        daoTarea = new SQLiteDaoTarea(this);
        daoParticipante = new SQLiteDaoParticipante(this);
        daoEvento = new SQLiteDaoEvento(this);

        intentOrigen = getIntent();
        Bundle extras = intentOrigen.getExtras();
        flagNuevaTarea = extras.getBoolean(KEY_TAREA_NUEVA_FLAG);
        final Integer idTarea = extras.getInt(KEY_TAREA_ID);
        evento = daoEvento.getById(extras.getInt(KEY_EVENTO_ID));


        getViews();
        inicializarSpinner(0); //0 para que no seleccione nada

        if(!flagNuevaTarea) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    tarea = daoTarea.getById(idTarea);
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
                List<Participante> lista = daoParticipante.getAllDelEvento(evento.getId());
                listaParticipantes.clear();
                listaParticipantes.addAll(lista);
                listaParticipantes.add(0, Participante.getParticipanteSinAsignar());
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
        if(tarea.getEstadoTarea() != EstadoTarea.SIN_ASIGNAR) {
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
        Participante encargado = adapterParticipantes.getItem(spinner_encargado.getSelectedItemPosition());
        //EstadoTarea estado = EstadoTarea.FINALIZADA;
        // TODO VER LO DEL ESTADO Y EL SEGUNDO IF QUE ES MEDIO RARO
        if(flagNuevaTarea) {
            tarea = new Tarea();
        }
        /*if(flagNuevaTarea || !flagNuevaTarea && tarea.getEstadoTarea() != EstadoTarea.FINALIZADA) {
            tarea.setPersonaAsignada(encargado);
            estado = EstadoTarea.EN_PROGRESO;
        }*/
        tarea.setTitulo(titulo);
        if(flagNuevaTarea){
            if(encargado.esSinAsignar()){
                tarea.setEstadoTarea(EstadoTarea.SIN_ASIGNAR);
            } else {
                tarea.setEstadoTarea(EstadoTarea.EN_PROGRESO);
            }
        }
        tarea.setDescripcion(descripcion);
        tarea.setPersonaAsignada(encargado);
        //TODO UPDATE EN DB SI NO ES NUEVA TAREA
        daoTarea.save(tarea, evento.getId());
    }
}
