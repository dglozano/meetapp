package com.retigalo.dglozano.meetapp.actividades;

import android.os.Bundle;
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
import com.retigalo.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.retigalo.dglozano.meetapp.dao.SQLiteDaoParticipante;
import com.retigalo.dglozano.meetapp.dao.SQLiteDaoTarea;
import com.retigalo.dglozano.meetapp.fragments.EventosPageFragment;

public class MainActivity extends AppCompatActivity {


    private static final int FRAGMENT_ID_LISTA_EVENTOS = 0;

    private Fragment fragmentBeingDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_main);
        setSupportActionBar(myToolbar);


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        ab.setDisplayUseLogoEnabled(true);
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
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        SQLiteDaoEvento daoEvento = new SQLiteDaoEvento(this);
        SQLiteDaoParticipante daoParticipante = new SQLiteDaoParticipante(this);
        SQLiteDaoTarea daoTarea = new SQLiteDaoTarea(this);
        if (daoEvento.getAll().isEmpty()) {
            daoEvento.createMockData();
            daoParticipante.createMockData(daoEvento.getAll());
            daoTarea.createMockData(daoEvento.getAll());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search_main:
                // User chose the Search option.
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            switch (position) {
                case FRAGMENT_ID_LISTA_EVENTOS:
                    fragmentBeingDisplayed = EventosPageFragment.newInstance();
                    break;
            }
            return fragmentBeingDisplayed;
        }

        @Override
        public int getCount() {
            // Show 1 total pages.
            return 1;
        }
    }
}
