package com.example.dglozano.meetapp.actividades;

import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.fragments.DivisionGastosPageFragment;
import com.example.dglozano.meetapp.fragments.ParticipantesPageFragment;
import com.example.dglozano.meetapp.fragments.TareasPageFragment;


public class EventoActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static final int FRAGMENT_ID_LISTA_TAREAS = 0;
    private static final int FRAGMENT_ID_LISTA_PARTICIPANTES = 1;
    private static final int FRAGMENT_ID_LISTA_PAGOS = 2;

    private Fragment fragmentBeingDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /**
         * Esto es para que aparezcan los puntito abajo que indican en que pagina se esta
         */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDotsTareasParticipantesGastos);
        tabLayout.setupWithViewPager(mViewPager, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                // User chose the "Settings" item, show the app settings UI...
                System.out.println("Hizo click en settings");
                return true;

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

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case FRAGMENT_ID_LISTA_TAREAS:
                    fragmentBeingDisplayed = TareasPageFragment.newInstance();
                    break;
                case FRAGMENT_ID_LISTA_PARTICIPANTES:
                    fragmentBeingDisplayed = ParticipantesPageFragment.newInstance();
                    break;
                case FRAGMENT_ID_LISTA_PAGOS:
                    fragmentBeingDisplayed = DivisionGastosPageFragment.newInstance();
                    break;
            }
            return fragmentBeingDisplayed;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
