package com.example.eloquentequipos;

import android.content.Intent;
import android.os.Bundle;

import com.example.eloquentequipos.model.data.Equipo;
import com.example.eloquentequipos.view.EquiposAdapter;
import com.example.eloquentequipos.view.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static MainViewModel viewModel;
    private EquiposAdapter equiposAdapter;
    private static MainActivity contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contexto = this;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEquipo.class);
                startActivity(intent);
            }
        });

        RecyclerView rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        equiposAdapter = new EquiposAdapter(this);
        rvList.setAdapter(equiposAdapter);

        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLiveEquipoList().observe(this, new Observer<List<Equipo>>() {
            @Override
            public void onChanged(List<Equipo> equipos) {
                equiposAdapter.setData(equipos);
            }
        });
    }

    public static MainActivity getContext() {
        return contexto;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.select_ip) {
            Intent intent = new Intent(MainActivity.this, Conexion.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
