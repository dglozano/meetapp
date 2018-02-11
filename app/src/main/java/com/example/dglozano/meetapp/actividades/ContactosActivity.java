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
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import java.util.concurrent.ThreadLocalRandom;
import android.widget.ListView;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.dao.Dao;
import com.example.dglozano.meetapp.dao.MockDaoParticipante;
import com.example.dglozano.meetapp.modelo.Participante;

public class ContactosActivity extends AppCompatActivity {

    ListView list;
    Intent intentOrigen;
    private Participante participante;
    ArrayAdapter<String> adapter;
    ArrayList<String> contacts;
    ArrayList<String> contactsDisplayed;
    private Dao<Participante> dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contactos_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_contactos);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        dao = MockDaoParticipante.getInstance();

        intentOrigen = getIntent();
        Bundle extras = intentOrigen.getExtras();

        list = (ListView) findViewById(R.id.listView1);

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        LoadContactsAsycn lca = new LoadContactsAsycn();
        lca.execute();


    }

//   private static final String SELECTION =
//            ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET + " LIKE " +37;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contactos, menu);
        return super.onCreateOptionsMenu(menu);
    }


    class LoadContactsAsycn extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            contacts = new ArrayList<String>();

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
//                String cuenta = c
//                        .getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET));

                contacts.add(contactName + System.lineSeparator() + phNumber);

            }
            c.close();

            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> contacts) {

            super.onPostExecute(contacts);
            contactsDisplayed = contacts;

            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, contactsDisplayed);

            list.setAdapter(adapter);


        }

    }

    public void guardar(ArrayList<String> check){
        for (String s : check){
            String[] partes = s.split(System.lineSeparator());
            String Nombre = partes[0];
            String Numero = partes[1];

            int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);

            participante = new Participante();

            participante.setNombreApellido(Nombre);
            participante.setPictureId(randomNum);

            dao.save(participante);
        }
    }

}
