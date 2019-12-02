package com.example.eloquentequipos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eloquentequipos.model.Repository;
import com.example.eloquentequipos.model.data.Equipo;
import com.example.eloquentequipos.view.MainViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class AddEquipo extends AppCompatActivity {

    private static final String ID = "idEquipo";
    private static final int SELECT_IMAGE_TO_UPLOAD = 1;
    public static final String DATA = "012UV34BCDEFGHIMNOPQ567JKL89ARSTWXYZ";

    public MainViewModel viewModel;
    private EditText etNombre;
    private EditText etCiudad;
    private EditText etAforo;
    private EditText etEstadio;
    private Button btCrear;
    private Button btEditar;
    private ImageView ivEscudo;

    private Uri uri;
    private String escudo = "";
    public static Random RANDOM = new Random();

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipo);
        init();
    }

    private void init() {

        Repository repository = new Repository();
        url = repository.getUrl();

        final Equipo[] equipos = {new Equipo()};
        final long id = getIntent().getLongExtra(ID, 0);

        viewModel = MainActivity.viewModel;
        etNombre = findViewById(R.id.etNombre);
        etCiudad = findViewById(R.id.etCiudad);
        etAforo = findViewById(R.id.etAforo);
        etEstadio = findViewById(R.id.etEstadio);
        btCrear = findViewById(R.id.btCrear);
        ivEscudo = findViewById(R.id.ivEscudo);
        btEditar = findViewById(R.id.btEditar);

        /*viewModel.equipoLiveData(id).observe(AddEquipo.this, new Observer<Equipo>() {
            @Override
            public void onChanged(Equipo equipo) {

                equipos[0] = equipo;
                String nombre = equipos[0].getNombre();
                String ciudad = equipos[0].getCiudad();
                String aforo = ""+equipos[0].getAforo();
                String estadio = equipos[0].getEstadio();

                etNombre.setText(nombre);
                etEstadio.setText(estadio);
                etCiudad.setText(ciudad);
                etAforo.setText(aforo);

                btEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(uri != null){
                            saveSelectedImageInFile(uri);
                        }
                        Equipo e = new Equipo();
                        e.setNombre(etNombre.getText().toString());
                        e.setCiudad(etCiudad.getText().toString());
                        e.setAforo(Integer.parseInt(etAforo.getText().toString()));
                        e.setEstadio(etEstadio.getText().toString());
                        if(!escudo.equalsIgnoreCase("")){
                            e.setEscudo(escudo);
                        }
                        viewModel.updateEquipo(id,e);
                        finish();
                    }
                });

                Glide.with(AddEquipo.this)
                        .load(url+"/upload/"+equipo.getEscudo())
                        .override(500, 500)// prueba de escalado
                        .into(ivEscudo);
            }
        });*/

        btCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombre.getText().toString().equalsIgnoreCase("") ||
                        etAforo.getText().toString().equalsIgnoreCase("") ||
                        etCiudad.getText().toString().equalsIgnoreCase("") ||
                        etEstadio.getText().toString().equalsIgnoreCase("") ||
                        uri == null){
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }else {
                    saveSelectedImageInFile(uri);
                    Equipo e = new Equipo();
                    e.setEstadio(etEstadio.getText().toString());
                    e.setNombre(etNombre.getText().toString());
                    e.setAforo(Integer.parseInt(etAforo.getText().toString()));
                    e.setEscudo(escudo);
                    e.setCiudad(etCiudad.getText().toString());
                    viewModel.addEquipo(e);
                    finish();
                    btCrear.setVisibility(View.INVISIBLE);
                }
            }
        });

        ivEscudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cogerImagen();
            }
        });
    }

    private void cogerImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        String[] types = {"image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
        startActivityForResult(intent, SELECT_IMAGE_TO_UPLOAD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_TO_UPLOAD && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();
            this.uri = uri;
            Glide.with(this)
                    .load(uri)
                    .override(500, 500)
                    .into(ivEscudo);
        }
    }

    private void saveSelectedImageInFile(Uri uri) {
        Bitmap bitmap = null;
        if(Build.VERSION.SDK_INT < 28) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                bitmap = null;
            }
        } else {
            try {
                final InputStream in = this.getContentResolver().openInputStream(uri);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            } catch (IOException e) {
                bitmap = null;
            }
        }
        if(bitmap != null) {
            File file = saveBitmapInFile(bitmap);
            if(file != null) {
                viewModel.upload(file);
            }
        }
    }

    private File saveBitmapInFile(Bitmap bitmap) {
        escudo = randomString(15)+".jpg";
        File file = new File(getFilesDir(), escudo);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            file = null;
        }
        return file;
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }

}
