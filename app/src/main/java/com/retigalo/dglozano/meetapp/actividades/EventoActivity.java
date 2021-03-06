package com.retigalo.dglozano.meetapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.retigalo.dglozano.meetapp.R;
import com.retigalo.dglozano.meetapp.adapters.EventoItemAdapter;
import com.retigalo.dglozano.meetapp.dao.DaoEvento;
import com.retigalo.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.retigalo.dglozano.meetapp.fragments.DivisionGastosPageFragment;
import com.retigalo.dglozano.meetapp.fragments.ParticipantesPageFragment;
import com.retigalo.dglozano.meetapp.fragments.TareasPageFragment;
import com.retigalo.dglozano.meetapp.modelo.Evento;


public class EventoActivity extends AppCompatActivity {

    private static final int FRAGMENT_ID_LISTA_TAREAS = 0;
    private static final int FRAGMENT_ID_LISTA_PARTICIPANTES = 1;
    private static final int FRAGMENT_ID_LISTA_PAGOS = 2;

    private Fragment fragmentBeingDisplayed;

    private Evento evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        Toolbar myToolbar = findViewById(R.id.my_toolbar_evento_act);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        DaoEvento daoEvento = new SQLiteDaoEvento(this);

        Intent intentOrigen = getIntent();
        int idEventoClickeado = intentOrigen.getExtras().getInt(EventoItemAdapter.EXTRA_EVENTO_ID);
        evento = daoEvento.getById(idEventoClickeado);
        setTitle(evento.getNombre());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout_evento);
        tabLayout.setupWithViewPager(mViewPager, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search:
                // User chose the Search option.
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                System.out.println("Hizo click en buscar");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_evento, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private String[] titles = {"TAREAS", "PARTICIPANTES", "PAGOS"};

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case FRAGMENT_ID_LISTA_TAREAS:
                    fragmentBeingDisplayed = TareasPageFragment.newInstance(evento.getId());
                    break;
                case FRAGMENT_ID_LISTA_PARTICIPANTES:
                    fragmentBeingDisplayed = ParticipantesPageFragment.newInstance(evento.getId());
                    break;
                case FRAGMENT_ID_LISTA_PAGOS:
                    fragmentBeingDisplayed = DivisionGastosPageFragment.newInstance(evento.getId());
                    break;
            }
            return fragmentBeingDisplayed;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
