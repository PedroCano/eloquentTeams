package com.example.eloquentequipos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eloquentequipos.model.data.Jugador;
import com.example.eloquentequipos.model.Repository;
import com.example.eloquentequipos.view.MainViewModel;

public class VerJugador extends AppCompatActivity {

    public MainViewModel viewModel;
    private static final String ID = "idJugador";
    private TextView tvNombre, tvApellidos;
    private ImageView ivImagen;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_jugador);
        init();
    }

    private void init() {
        Repository repository = new Repository();
        url = repository.getUrl();

        ivImagen = findViewById(R.id.ivJugador);
        tvNombre = findViewById(R.id.tvNombre);
        tvApellidos = findViewById(R.id.tvApellidos);
        final long id = getIntent().getLongExtra(ID, 0);
        viewModel = Plantilla.viewModel;
        final Jugador[] j = {new Jugador()};
        viewModel.jugadoresLiveData(id).observe(VerJugador.this, new Observer<Jugador>() {
            @Override
            public void onChanged(Jugador jugador) {
                j[0] = jugador;
                String nombre = jugador.getNombre();
                String apellidos = jugador.getApellidos();

                tvNombre.setText(nombre);
                tvApellidos.setText(apellidos);

                Glide.with(VerJugador.this)
                        .load(url+"/upload/"+ jugador.getFoto())
                        .override(500, 500)// prueba de escalado
                        .into(ivImagen);
            }
        });



    }
}
