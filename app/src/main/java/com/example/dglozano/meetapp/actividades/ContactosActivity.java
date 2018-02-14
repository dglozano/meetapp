package com.example.dglozano.meetapp.actividades;

/**
 * Created by augusto on 10/02/2018.
 */

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.List;

import android.widget.ListView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.dao.DaoEvento;
import com.example.dglozano.meetapp.dao.DaoEventoMember;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Participante;

public class ContactosActivity extends AppCompatActivity {

    private ListView list;
    private Intent intentOrigen;
    private Participante participante;
    private ArrayAdapter<String> adapter;
    private List<String> todosLosContactos = new ArrayList<>();
    private List<String> contactosDisplayed = new ArrayList<>();
    private DaoEventoMember<Participante> daoParticipante;
    private DaoEvento daoEvento;
    private Evento evento;

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
        daoEvento = new SQLiteDaoEvento(this);

        intentOrigen = getIntent();
        Bundle extras = intentOrigen.getExtras();
        evento = daoEvento.getById(extras.getInt(KEY_EVENTO_ID));

        list = (ListView) findViewById(R.id.listView1);

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        LoadContactsAsycn lca = new LoadContactsAsycn();
        lca.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.toolbar_search_contactos:

                return true;
            case R.id.menu_item_Ok:
                ArrayList<String> checkeados = getChecked();
                guardar(checkeados);
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

    public ArrayList<String> getChecked() {
        int count = list.getCount();
        ArrayList<String> save = new ArrayList<>();
        SparseBooleanArray viewItems = list.getCheckedItemPositions();
        for (int i = 0; i < count; i++) {
            if (viewItems.get(i)) {
                String selectedContact = (String) list.getItemAtPosition(i);
                save.add(selectedContact);
            }
        }
        return save;
    }

    private void search(String query) {
        List<String> result = new ArrayList<>();
        for(String c: todosLosContactos) {
            if(c.toUpperCase().contains(query.toUpperCase())) {
                result.add(c);
            }
        }
        contactosDisplayed.clear();
        contactosDisplayed.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void restoreOriginalContactosList() {
        contactosDisplayed.clear();
        contactosDisplayed.addAll(todosLosContactos);
        adapter.notifyDataSetChanged();
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
            if(query.trim().isEmpty()) {
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

    class LoadContactsAsycn extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            ArrayList<String> contacts = new ArrayList<>();

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

                if (contacts.size()==0) {
                    contacts.add(contactName + System.lineSeparator() + phNumber);
                }
                else {
                    boolean flag = false;
                    for (String cn : contacts){
                        String[] partes = cn.split(System.lineSeparator());
                        if (partes[0].equals(contactName)){
                            flag = true;
                        }
                    }
                    if (!flag){
                        contacts.add(contactName + System.lineSeparator() + phNumber);
                    }
                }
            }
            c.close();
            Collections.sort(contacts);
            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> contacts) {

            super.onPostExecute(contacts);
            todosLosContactos.addAll(contacts);
            contactosDisplayed.addAll(contacts);

            adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_multiple_choice, contactosDisplayed);

            list.setAdapter(adapter);
        }
    }

    public void guardar(ArrayList<String> check){
        for (String s : check){
            String[] partes = s.split(System.lineSeparator());
            String nombre = partes[0];
            String numero = partes[1];

            participante = new Participante(nombre, numero);

            daoParticipante.save(participante, evento.getId());
        }
    }
}
