package com.example.dglozano.meetapp.actividades;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.dao.DaoEvento;
import com.example.dglozano.meetapp.dao.DaoEventoMember;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.example.dglozano.meetapp.dao.SQLiteDaoTarea;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Participante;
import com.example.dglozano.meetapp.modelo.Tarea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TareaForm extends AppCompatActivity {

    public static final String KEY_TAREA_ID = "idTarea";
    public static final String KEY_EVENTO_ID = "idEvento";
    public static final String KEY_TAREA_NUEVA_FLAG = "tareaNuevaFlag";

    private static final int PERMISSION_REQUEST_CAMERA = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 5;

    private Intent intentOrigen;
    private Boolean flagNuevaTarea;
    private Tarea tarea;
    private DaoEvento daoEvento;
    private DaoEventoMember<Tarea> daoTarea;
    private DaoEventoMember<Participante> daoParticipante;

    private EditText et_titulo;
    private Spinner spinner_encargado;
    private EditText et_descripcion;
    private Button btnCargarFoto;
    private ImageView imgFoto;

    private List<Participante> listaParticipantes;
    private ArrayAdapter<Participante> adapterParticipantes;

    private Integer eventoId;
    private Bitmap imageBitmap;

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
        eventoId = extras.getInt(KEY_EVENTO_ID);


        getViews();
        inicializarSpinner(0); //0 para que no seleccione nada

        if(!flagNuevaTarea) {
            btnCargarFoto.setEnabled(true);
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
        } else {
            btnCargarFoto.setEnabled(false);
        }
    }

    private void getViews() {
        et_titulo = findViewById(R.id.editText_titulo);
        spinner_encargado = findViewById(R.id.spinner_encargado);
        et_descripcion = findViewById(R.id.editText_descripcion);
        btnCargarFoto = findViewById(R.id.btn_cargarfoto);
        btnCargarFoto.setOnClickListener(new CargarFotoListener());
        imgFoto = findViewById(R.id.frmImgFoto);
    }

    private void inicializarSpinner(final int selectedPosition) {
        listaParticipantes = new ArrayList<>();
        adapterParticipantes = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaParticipantes);
        spinner_encargado.setAdapter(adapterParticipantes);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<Participante> lista = daoParticipante.getAllDelEvento(eventoId);
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

        // Mostrar o no la imagen
        try {
            loadImageFromStorage(eventoId, tarea.getId());
            imgFoto.setVisibility(View.VISIBLE);
        } catch(FileNotFoundException e) {
            imgFoto.setVisibility(View.GONE);
        }
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
        if(flagNuevaTarea) {
            tarea = new Tarea();
        } else {
            if(imageBitmap != null) {
                saveImageInStorage(imageBitmap);
            }
        }
        if(flagNuevaTarea || !flagNuevaTarea && !tarea.estaFinalizada()) {
            // si se crea una nueva o se edita una finalizada (ya que si se edita una finalizada no debería cambiar el estado a pesar de cambiar el encargado)
            if(encargado.esSinAsignar()) {
                tarea.setEstadoTarea(EstadoTarea.SIN_ASIGNAR);
            } else {
                tarea.setEstadoTarea(EstadoTarea.EN_PROGRESO);
            }
        }
        tarea.setTitulo(titulo);
        tarea.setDescripcion(descripcion);
        tarea.setPersonaAsignada(encargado);

        if(flagNuevaTarea) daoTarea.save(tarea, eventoId);
        else daoTarea.update(tarea);
    }

    private class CargarFotoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(hasPermission(Manifest.permission.CAMERA)) {
                dispatchTakePictureIntent();
            } else {
                askForPermission(Manifest.permission.CAMERA, TareaForm.PERMISSION_REQUEST_CAMERA,
                        getString(R.string.solicitud_permiso_foto));
            }
        }
    }

    public boolean hasPermission(final String permisoManifest) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // la versión alcanza con tenerlo declarado
            return true;
        }
        if(ContextCompat.checkSelfPermission(TareaForm.this, permisoManifest)
                == PackageManager.PERMISSION_GRANTED) {
            // El permiso ya esta dado
            return true;
        }
        return false;
    }

    public void askForPermission(final String permisoManifest, final int codigoPermiso, String rationaleMsgStr) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(TareaForm.this,
                permisoManifest)) {
            // Por lo que entiendo, esto lo pide solamente si ya intento varias veces y
            // hay que hacerle una explicacion mas detallada de por que necesitamos el permiso
            AlertDialog.Builder builder = new AlertDialog.Builder(TareaForm.this);
            builder.setTitle(R.string.titulo_dialog);
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setMessage(rationaleMsgStr);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(
                            new String[]
                                    {permisoManifest}
                            , codigoPermiso);
                }
            });
            builder.show();
        } else {
            // Abre el dialogo para pedir el permiso del a camara.
            ActivityCompat.requestPermissions(TareaForm.this,
                    new String[]
                            {permisoManifest},
                    codigoPermiso);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case TareaForm.PERMISSION_REQUEST_CAMERA: {
                // si el request es cancelado el arreglo es vacio.
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // tengo el permiso, saco la foto!!!
                    dispatchTakePictureIntent();
                }
                return;
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new
                Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) !=
                null) {
            startActivityForResult(takePictureIntent,
                    REQUEST_IMAGE_CAPTURE);
        }
    }

    private void saveImageInStorage(Bitmap imageTarea) {
        File directory = getApplicationContext().getDir("imagenes", Context.MODE_PRIVATE);
        if(!directory.exists())
            directory.mkdir();
        File mypath = new File(directory, "evento_" + eventoId + "tarea_" + tarea.getId() + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(mypath);
            imageTarea.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImageFromStorage(int idEvento, int idTarea) throws FileNotFoundException {
        File directory = getApplicationContext().getDir("imagenes", Context.MODE_PRIVATE);
        File f = new File(directory, "evento_" + idEvento + "tarea_" + idTarea + ".jpg");
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
        imgFoto.setImageBitmap(b);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imgFoto.setImageBitmap(imageBitmap);
                imgFoto.setVisibility(View.VISIBLE);
                break;
        }
    }
}
