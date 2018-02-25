package com.retigalo.dglozano.meetapp.actividades;

/**
 * Created by augusto on 10/02/2018.
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.retigalo.dglozano.meetapp.R;
import com.retigalo.dglozano.meetapp.adapters.ContactoItemAdapter;
import com.retigalo.dglozano.meetapp.dao.DaoEventoMember;
import com.retigalo.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.retigalo.dglozano.meetapp.modelo.Contacto;
import com.retigalo.dglozano.meetapp.modelo.Participante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactosActivity extends AppCompatActivity {

    private Intent intentOrigen;
    private ContactoItemAdapter mContactoItemAdapter;
    private List<Contacto> todosLosContactos = new ArrayList<>();
    private List<Contacto> contactosDisplayed = new ArrayList<>();

    private DaoEventoMember<Participante> daoParticipante;
    private Integer eventoId;

    public static final String KEY_EVENTO_ID = "idEvento";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contactos_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_contactos);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        daoParticipante = new SQLiteDaoParticipante(this);
        intentOrigen = getIntent();
        Bundle extras = intentOrigen.getExtras();
        eventoId = extras.getInt(KEY_EVENTO_ID);

        LoadContactsAsycn lca = new LoadContactsAsycn();
        lca.execute();

        RecyclerView mContactosRecyclerView = findViewById(R.id.recvw_contactos_list);
        mContactoItemAdapter = new ContactoItemAdapter(contactosDisplayed, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                this.getApplicationContext());
        mContactosRecyclerView.setLayoutManager(mLayoutManager);
        mContactosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mContactosRecyclerView.addItemDecoration(new DividerItemDecoration(
                this.getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mContactosRecyclerView.setAdapter(mContactoItemAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search_contactos:
                return true;
            case R.id.menu_item_Ok:
                ArrayList<Contacto> contactosCheckeados = getChecked();
                guardar(contactosCheckeados);
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

    private ArrayList<Contacto> getChecked() {
        ArrayList<Contacto> contactosCheckeados = new ArrayList<>();
        for (Contacto c : todosLosContactos) {
            if (c.isChecked()) {
                contactosCheckeados.add(c);
            }
        }
        return contactosCheckeados;
    }

    private void search(String query) {
        List<Contacto> result = new ArrayList<>();
        for (Contacto c : todosLosContactos) {
            if (c.getNombre().toUpperCase().contains(query.toUpperCase())) {
                result.add(c);
            }
        }
        contactosDisplayed.clear();
        contactosDisplayed.addAll(result);
        mContactoItemAdapter.notifyDataSetChanged();
    }

    private void restoreOriginalContactosList() {
        contactosDisplayed.clear();
        contactosDisplayed.addAll(todosLosContactos);
        mContactoItemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contactos, menu);
        final MenuItem searchItem = menu.findItem(R.id.toolbar_search_contactos);
        final SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new ContactosActivity.MyOnQueryTextListener());
        searchView.setOnCloseListener(new ContactosActivity.MyOnCloseListener());
        return super.onCreateOptionsMenu(menu);
    }

    private class MyOnQueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            search(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            search(query);
            if (query.trim().isEmpty()) {
                restoreOriginalContactosList();
            }
            return false;
        }
    }

    private class MyOnCloseListener implements SearchView.OnCloseListener {
        @Override
        public boolean onClose() {
            restoreOriginalContactosList();
            return false;
        }
    }

    class LoadContactsAsycn extends AsyncTask<Void, Void, ArrayList<Contacto>> {

        @Override
        protected ArrayList<Contacto> doInBackground(Void... params) {

            ArrayList<Contacto> contacts = new ArrayList<>();

            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            while (c.moveToNext()) {

                String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if (contacts.size() == 0) {
                    contacts.add(new Contacto(contactName, phNumber));
                } else {
                    boolean flag = false;
                    for (Contacto cn : contacts) {
                        if (cn.getNombre().equals(contactName)) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        contacts.add(new Contacto(contactName, phNumber));
                    }
                }
            }
            c.close();
            Collections.sort(contacts);
            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<Contacto> contacts) {

            super.onPostExecute(contacts);
            todosLosContactos.addAll(contacts);
            contactosDisplayed.addAll(contacts);

            contactosDisplayed.clear();
            contactosDisplayed.addAll(todosLosContactos);
            mContactoItemAdapter.notifyDataSetChanged();
        }
    }

    private void guardar(ArrayList<Contacto> contactosChecked) {
        for (Contacto c : contactosChecked) {
            String nombre = c.getNombre();
            String numero = c.getNumero();

            Participante participante = new Participante(nombre, numero);

            daoParticipante.save(participante, eventoId);
        }
    }
}
