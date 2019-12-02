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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eloquentequipos.EditarEquipo;
import com.example.eloquentequipos.MainActivity;
import com.example.eloquentequipos.model.data.Equipo;
import com.example.eloquentequipos.model.Repository;
import com.example.eloquentequipos.R;
import com.example.eloquentequipos.VerEquipo;

import java.util.List;

public class EquiposAdapter extends RecyclerView.Adapter <EquiposAdapter.ItemHolder>{

    private LayoutInflater inflater;
    private List<Equipo> equipoList;
    public MainViewModel viewModel;
    private Context context;
    private static final String ID = "idEquipo";
    private static final String STORAGE = "/upload/";

    public EquiposAdapter(Context context) {
        inflater=LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public EquiposAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.item_equipo,parent,false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EquiposAdapter.ItemHolder holder, int position) {

        if(equipoList !=null){

            final int posicion = position;
            final Equipo current = equipoList.get(position);
            holder.tvNombre.setText(current.getNombre());
            holder.tvCiudad.setText(current.getCiudad());

            Repository repository = new Repository();

            Glide.with(context)
                    .load(repository.getUrl()+"/upload/"+current.getEscudo())
                    .override(500, 500)
                    .into(holder.ivJugador);

            holder.cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VerEquipo.class);
                    intent.putExtra(ID, current.getId());
                    context.startActivity(intent);
                }
            });

            viewModel = MainActivity.viewModel;

            holder.btBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.dialogoMensaje)
                            .setTitle(R.string.dialogoTitulo);
                    builder.setPositiveButton(R.string.confirmacion, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            viewModel.deleteEquipo(current.getId());
                            notifyItemRemoved(posicion);
                            equipoList.remove(posicion);
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
                    Intent intent = new Intent(context, EditarEquipo.class);
                    intent.putExtra(ID, current.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int elements=0;
        if(equipoList !=null){
            elements= equipoList.size();
        }
        return elements;
    }

    public void setData(List<Equipo> equipoList){
        this.equipoList = equipoList;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private final TextView tvNombre, tvCiudad;
        private final Button btBorrar, btEditar;
        private ConstraintLayout cl;
        private ImageView ivJugador;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre=itemView.findViewById(R.id.tvNombre);
            tvCiudad=itemView.findViewById(R.id.etCiudad);
            btBorrar=itemView.findViewById(R.id.btBorrar);
            btEditar=itemView.findViewById(R.id.btEditar);
            cl = itemView.findViewById(R.id.cl);
            ivJugador = itemView.findViewById(R.id.ivJugador);
        }
    }
}
