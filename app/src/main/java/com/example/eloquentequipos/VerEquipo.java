package com.example.eloquentequipos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eloquentequipos.model.data.Equipo;
import com.example.eloquentequipos.model.Repository;
import com.example.eloquentequipos.view.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VerEquipo extends AppCompatActivity {

    private static final String ID = "idEquipo";

    public MainViewModel viewModel;
    private TextView tvNombre;
    private TextView tvCiudad;
    private TextView tvAfoto;
    private TextView tvEstadio;
    private FloatingActionButton btJugadores;
    private ImageView ivImagen;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_equipo);
        init();
    }

    private void init() {

        Repository repository = new Repository();
        url = repository.getUrl();

        final long id = getIntent().getLongExtra(ID, 0);
        viewModel = MainActivity.viewModel;
        ivImagen = findViewById(R.id.ivJugador);

        viewModel.equipoLiveData(id).observe(VerEquipo.this, new Observer<Equipo>() {
            @Override
            public void onChanged(Equipo equipo) {
                String nombre = equipo.getNombre();
                String ciudad = equipo.getCiudad();
                String aforo = ""+equipo.getAforo();
                String estadio = equipo.getEstadio();

                tvNombre = findViewById(R.id.tvNombre);
                tvCiudad = findViewById(R.id.tvCiudad);
                tvAfoto = findViewById(R.id.tvAforo);
                tvEstadio = findViewById(R.id.tvEstadio);
                btJugadores = findViewById(R.id.btJugadores);

                tvNombre.setText(nombre);
                tvAfoto.setText(aforo);
                tvCiudad.setText(ciudad);
                tvEstadio.setText(estadio);

                btJugadores.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VerEquipo.this, Plantilla.class);
                        intent.putExtra(ID, id);
                        startActivity(intent);
                    }
                });

                Glide.with(VerEquipo.this)
                        .load(url+"/upload/"+equipo.getEscudo())
                        .override(500, 500)// prueba de escalado
                        .into(ivImagen);
            }
        });
    }
}
