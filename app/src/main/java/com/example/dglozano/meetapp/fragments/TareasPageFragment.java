package com.example.dglozano.meetapp.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dglozano.meetapp.R;
import com.example.dglozano.meetapp.actividades.TareaForm;
import com.example.dglozano.meetapp.adapters.TareaItemAdapter;
import com.example.dglozano.meetapp.dao.DaoEvento;
import com.example.dglozano.meetapp.dao.DaoEventoMember;
import com.example.dglozano.meetapp.dao.SQLiteDaoEvento;
import com.example.dglozano.meetapp.dao.SQLiteDaoPago;
import com.example.dglozano.meetapp.dao.SQLiteDaoTarea;
import com.example.dglozano.meetapp.modelo.EstadoTarea;
import com.example.dglozano.meetapp.modelo.Evento;
import com.example.dglozano.meetapp.modelo.Pago;
import com.example.dglozano.meetapp.modelo.Tarea;
import com.example.dglozano.meetapp.util.OnDismissListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TareasPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TareasPageFragment extends android.support.v4.app.Fragment
        implements DialogDeletePagos.NoticeDialogListener, OnDismissListener {

    private final int CREAR_TAREA = 1;
    private final int EDITAR_TAREA = 2;

    private List<Tarea> tareasListDisplayed = new ArrayList<>();
    private TareaItemAdapter mTareaAdapter;
    private LinearLayout mLayoutEmptyMsg;

    private static final String EVENTO_ID = "EVENTO_ID";

    private DaoEventoMember<Tarea> daoTarea;
    private DaoEventoMember<Pago> daoPagos;
    private DaoEvento daoEvento;
    private Integer eventoId;
    private List<Tarea> tareasListDelEvento;

    public TareasPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment TareasPageFragment.
     */
    public static TareasPageFragment newInstance(int eventoId) {
        TareasPageFragment fragment = new TareasPageFragment();
        Bundle args = new Bundle();
        args.putInt(EVENTO_ID, eventoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        daoEvento = new SQLiteDaoEvento(getActivity());
        eventoId = getArguments().getInt(EVENTO_ID);
        daoTarea = new SQLiteDaoTarea(getActivity());
        daoPagos = new SQLiteDaoPago(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tareas_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tareasListDelEvento = daoTarea.getAllDelEvento(eventoId);
        mLayoutEmptyMsg = view.findViewById(R.id.empty_msg_layout_tareas);
        mLayoutEmptyMsg.setVisibility(View.INVISIBLE);
        RecyclerView mTareasRecyclerview = view.findViewById(R.id.recvw_tareas_list);
        mTareaAdapter = new TareaItemAdapter(tareasListDisplayed, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getActivity().getApplicationContext());
        mTareasRecyclerview.setLayoutManager(mLayoutManager);
        mTareasRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mTareasRecyclerview.addItemDecoration(new DividerItemDecoration(
                getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        mTareasRecyclerview.setAdapter(mTareaAdapter);
        tareasListDisplayed.clear();
        tareasListDisplayed.addAll(tareasListDelEvento);
        mTareaAdapter.notifyDataSetChanged();
        if (tareasListDelEvento.isEmpty()) {
            mLayoutEmptyMsg.setVisibility(View.VISIBLE);
        }
        FloatingActionButton fab = view.findViewById(R.id.fab_btn_crear_tarea);
        fab.setOnClickListener(new MyFabIconOnClickListener(this));
    }

    private void search(String query) {
        List<Tarea> result = new ArrayList<>();
        for (Tarea t : tareasListDelEvento) {
            if (t.matches(query)) {
                result.add(t);
            }
        }
        tareasListDisplayed.clear();
        tareasListDisplayed.addAll(result);
        System.out.println(tareasListDisplayed);
        mTareaAdapter.notifyDataSetChanged();
    }

    private void restoreOriginalTareasList() {
        tareasListDisplayed.clear();
        tareasListDisplayed.addAll(tareasListDelEvento);
        mTareaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        final SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new MyOnQueryTextListener());
        searchView.setOnCloseListener(new MyOnCloseListener());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Evento evento = daoEvento.getById(eventoId);
        Integer pos = item.getItemId();
        // El dialogo llama a los metodos onDialogPositiveClick o onDialogNeativeClick
        // con el id del elemento del context menu clickeado.
        if (pos > 0 && pos <= 6) {
            Tarea tarea = tareasListDisplayed.get(item.getGroupId());
            //si el ID del item del menu clickeado es del 1 al 6, pertence a este fragment
            if (evento.isDivisionGastosYaHecha() && (pos <= 3 || pos == 6)) {
                DialogFragment df = DialogDeletePagos.newInstance(pos, tarea.getId());
                df.setTargetFragment(this, 1);
                df.show(getFragmentManager(), "tag");
            } else {
                accionesContextMenu(pos, tarea);
            }
            return true;
        }
        // si no devuelve false para que lo maneje el siguiente fragmento
        return false;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int idAccion, int idTarea) {
        Evento evento = daoEvento.getById(eventoId);
        Tarea tarea = daoTarea.getById(idTarea);
        for (Pago p : daoPagos.getAllDelEvento(evento.getId())) {
            daoPagos.delete(p);
        }
        evento.setGastosPorParticipante(0.0);
        evento.setGastosTotales(0.0);
        evento.setDivisionGastosYaHecha(false);
        daoEvento.update(evento);
        if (idAccion == -1) {
            Intent i = new Intent(getActivity(), TareaForm.class);
            i.putExtra(TareaForm.KEY_EVENTO_ID, eventoId);
            i.putExtra(TareaForm.KEY_TAREA_NUEVA_FLAG, true);
            startActivityForResult(i, CREAR_TAREA);
        } else {
            accionesContextMenu(idAccion, tarea);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, int idAccion, int idTarea) {
        switch (idAccion) {
            case -1:
                Toast.makeText(this.getContext(), R.string.tarea_no_creada, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this.getContext(), R.string.tarea_no_editada, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this.getContext(), R.string.tarea_no_borrada, Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this.getContext(), R.string.no_se_agrego_gasto, Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(this.getContext(), R.string.no_se_asigno_encargado, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void accionesContextMenu(int idAccion, Tarea tarea) {
        switch (idAccion) {
            case 1:
                editarTarea(tarea);
                tareasListDelEvento = daoTarea.getAllDelEvento(eventoId);
                restoreOriginalTareasList();
                break;
            case 2:
                Toast.makeText(this.getContext(), R.string.tarea_borrada, Toast.LENGTH_SHORT).show();
                daoTarea.delete(tarea);
                tareasListDelEvento = daoTarea.getAllDelEvento(eventoId);
                restoreOriginalTareasList();
                if (tareasListDelEvento.isEmpty()) {
                    mLayoutEmptyMsg.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                agregarGasto(tarea);
                break;
            case 4:
                darPorFinalizada(tarea);
                break;
            case 5:
                verFoto(tarea);
                break;
            case 6:
                asignarEncargado(tarea);
                break;
        }
    }

    private void asignarEncargado(Tarea tarea) {
        DialogFragment df = DialogSelectParticipante.newInstance(eventoId, tarea.getId());
        df.setTargetFragment(this, 1);
        df.show(getFragmentManager(), "tag");
    }

    private void verFoto(Tarea tarea) {
        DialogFragment df = DialogVerFotoTarea.newInstance(eventoId, tarea.getId(), tarea.getTitulo());
        df.setTargetFragment(this, 3);
        df.show(getFragmentManager(), "tag");
    }

    @Override
    public void onDismiss() {
        tareasListDelEvento = daoTarea.getAllDelEvento(eventoId);
        restoreOriginalTareasList();
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
                restoreOriginalTareasList();
            }
            return false;
        }
    }

    private class MyOnCloseListener implements SearchView.OnCloseListener {
        @Override
        public boolean onClose() {
            restoreOriginalTareasList();
            return false;
        }
    }

    private void agregarGasto(Tarea tarea) {
        DialogFragment f = AgregarGastoFragment.newInstance(tarea.getId());
        f.setTargetFragment(this, 2);
        f.show(getFragmentManager(), "dialog");
    }

    private void darPorFinalizada(Tarea tarea) {
        if (tarea.getEstadoTarea().equals(EstadoTarea.SIN_ASIGNAR)) {
            Toast.makeText(getContext(), R.string.tarea_no_asignada, Toast.LENGTH_LONG).show();
        } else if (tarea.getEstadoTarea().equals(EstadoTarea.FINALIZADA)) {
            Toast.makeText(getContext(), R.string.tarea_ya_finalizada, Toast.LENGTH_LONG).show();
        } else {
            tarea.setEstadoTarea(EstadoTarea.FINALIZADA);
            daoTarea.update(tarea);
            tareasListDelEvento = daoTarea.getAllDelEvento(eventoId);
            restoreOriginalTareasList();
        }
    }

    public void editarTarea(Tarea tarea) {
        Intent i = new Intent(getActivity(), TareaForm.class);
        i.putExtra(TareaForm.KEY_TAREA_ID, tarea.getId());
        i.putExtra(TareaForm.KEY_EVENTO_ID, eventoId);
        i.putExtra(TareaForm.KEY_TAREA_NUEVA_FLAG, false);
        startActivityForResult(i, EDITAR_TAREA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CREAR_TAREA: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this.getContext(), R.string.tarea_creada, Toast.LENGTH_SHORT).show();
                    tareasListDelEvento = daoTarea.getAllDelEvento(eventoId);
                    mLayoutEmptyMsg.setVisibility(View.INVISIBLE);
                    restoreOriginalTareasList();
                }
                break;
            }
            case EDITAR_TAREA: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this.getContext(), R.string.tarea_editada, Toast.LENGTH_SHORT).show();
                    tareasListDelEvento = daoTarea.getAllDelEvento(eventoId);
                    restoreOriginalTareasList();
                }
                break;
            }
        }
    }

    private class MyFabIconOnClickListener implements View.OnClickListener {

        android.support.v4.app.Fragment fragment;

        public MyFabIconOnClickListener(android.support.v4.app.Fragment f) {
            this.fragment = f;
        }

        @Override
        public void onClick(View view) {
            Evento evento = daoEvento.getById(eventoId);
            if (evento.isDivisionGastosYaHecha()) {
                DialogFragment df = DialogDeletePagos.newInstance(-1, -1);
                df.setTargetFragment(fragment, 1);
                df.show(getFragmentManager(), "tag");
            } else {
                Intent i = new Intent(getActivity(), TareaForm.class);
                i.putExtra(TareaForm.KEY_EVENTO_ID, eventoId);
                i.putExtra(TareaForm.KEY_TAREA_NUEVA_FLAG, true);
                startActivityForResult(i, CREAR_TAREA);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            if (eventoId != null) {
                tareasListDelEvento = daoTarea.getAllDelEvento(eventoId);
                restoreOriginalTareasList();
                if (tareasListDelEvento.isEmpty()) {
                    mLayoutEmptyMsg.setVisibility(View.VISIBLE);
                }
            }
        } else {
            // fragment is no longer visible
        }
    }
}