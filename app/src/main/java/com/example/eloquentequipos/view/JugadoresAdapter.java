package com.example.eloquentequipos.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eloquentequipos.EditarJugador;
import com.example.eloquentequipos.model.data.Jugador;
import com.example.eloquentequipos.model.Repository;
import com.example.eloquentequipos.Plantilla;
import com.example.eloquentequipos.R;
import com.example.eloquentequipos.VerJugador;

import java.util.List;

public class JugadoresAdapter extends RecyclerView.Adapter <JugadoresAdapter.ItemHolder>{

    private LayoutInflater inflater;
    private List<Jugador> jugadoresList;
    public MainViewModel viewModel;
    private Context context;
    private static final String ID = "Jugador.ID";
    private static final String IDEQUIPO = "Jugador.IDEQUIPO";
    private static final String STORAGE = "/upload/";
    private long idEquipo;

    public JugadoresAdapter(Context context, long id) {
        inflater=LayoutInflater.from(context);
        this.context = context;
        idEquipo = id;
    }

    @NonNull
    @Override
    public JugadoresAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.item_jugador,parent,false);
        return new JugadoresAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JugadoresAdapter.ItemHolder holder, int position) {
        if(jugadoresList !=null){
            final int posicion = position;
            final Jugador current = jugadoresList.get(position);
            if(current.getIdequipo() == idEquipo){
                holder.tvNombre.setText(current.getNombre());
                holder.tvApellidos.setText(current.getApellidos());

                Repository repository = new Repository();
                Glide.with(context)
                        .load(repository.getUrl()+STORAGE+current.getFoto())
                        .override(500, 500)// prueba de escalado
                        .into(holder.ivImagen);

                holder.cl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VerJugador.class);
                        intent.putExtra(ID, current.getId());
                        context.startActivity(intent);
                    }
                });

                viewModel = Plantilla.viewModel;

                holder.btBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(R.string.dialogoMensaje)
                                .setTitle(R.string.dialogoTitulo);
                        builder.setPositiveButton(R.string.confirmacion, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewModel.deleteJugador(current.getId());
                                notifyItemRemoved(posicion);
                                jugadoresList.remove(posicion);
                            }
                        });
                        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                holder.btEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, EditarJugador.class);
                        intent.putExtra(ID, current.getId());
                        intent.putExtra(IDEQUIPO, idEquipo);
                        context.startActivity(intent);
                    }
                });

            }else{
                holder.cv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        int elements=0;
        if(jugadoresList !=null){
            elements= jugadoresList.size();
        }
        return elements;
    }

    public void setData(List<Jugador> jugadorList){
        this.jugadoresList = jugadorList;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private final TextView tvNombre, tvApellidos;
        private final Button btBorrar, btEditar;
        private ConstraintLayout cl;
        private CardView cv;
        private ImageView ivImagen;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre=itemView.findViewById(R.id.tvNombre);
            tvApellidos=itemView.findViewById(R.id.tvApellidos);
            btBorrar=itemView.findViewById(R.id.btBorrar);
            btEditar=itemView.findViewById(R.id.btEditar);
            cl = itemView.findViewById(R.id.cl);
            cv = itemView.findViewById(R.id.cvContainer);
            ivImagen = itemView.findViewById(R.id.ivJugador);
        }
    }
}
