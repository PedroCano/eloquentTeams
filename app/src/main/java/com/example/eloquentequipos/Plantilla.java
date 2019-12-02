package com.example.eloquentequipos;

import android.content.Intent;
import android.os.Bundle;

import com.example.eloquentequipos.model.data.Jugador;
import com.example.eloquentequipos.view.JugadoresAdapter;
import com.example.eloquentequipos.view.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;

public class Plantilla extends AppCompatActivity {

    private static final String ID = "idEquipo";

    public static MainViewModel viewModel;
    private JugadoresAdapter jugadoresAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla);

        Intent i = getIntent();

        final long id = i.getLongExtra(ID, 0);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Plantilla.this, AddJugador.class);
                intent.putExtra(ID, id);
                startActivity(intent);
            }
        });

        RecyclerView rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        jugadoresAdapter = new JugadoresAdapter(this, id);
        rvList.setAdapter(jugadoresAdapter);

        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLiveJugadoresList().observe(this, new Observer<List<Jugador>>() {
            @Override
            public void onChanged(List<Jugador> jugadores) {
                jugadoresAdapter.setData(jugadores);
            }
        });
    }
}
