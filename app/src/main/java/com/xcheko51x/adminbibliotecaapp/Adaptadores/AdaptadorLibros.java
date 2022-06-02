package com.xcheko51x.adminbibliotecaapp.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.xcheko51x.adminbibliotecaapp.Clases.Autor;
import com.xcheko51x.adminbibliotecaapp.Clases.Libro;
import com.xcheko51x.adminbibliotecaapp.Clases.Usuario;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.Vistas.FormAgregarLibro;
import com.xcheko51x.adminbibliotecaapp.Vistas.FormPrestamo;
import com.xcheko51x.adminbibliotecaapp.Vistas.VistaUsuarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolder> {

    Context context;
    List<Libro> listaLibros = new ArrayList<>();

    public AdaptadorLibros(Context context, List<Libro> listaLibros) {
        this.context = context;
        this.listaLibros = listaLibros;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_libros, null, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(listaLibros.get(position).getExistencias() == 0) {
            holder.cvLibro.setCardBackgroundColor(context.getResources().getColor(R.color.rojo_oscuro));
            holder.btnPrestar.setEnabled(false);
            holder.btnPrestar.setBackgroundColor(context.getResources().getColor(R.color.gris_oscuro));
        } else {
            holder.cvLibro.setCardBackgroundColor(context.getResources().getColor(R.color.verde_oscuro));
        }

        Glide
                .with(context)
                .load(context.getResources().getString(R.string.url_portadas)+listaLibros.get(position).getPortada())
                .placeholder(context.getResources().getDrawable(R.drawable.icon_falta_foto))
                .into(holder.ivPortada);

        //holder.tvISBN.setText(listaLibros.get(position).getIsbn());
        holder.tvNomLibro.setText(listaLibros.get(position).getNom_libro());
        holder.tvAutor.setText(listaLibros.get(position).getAutor());

      holder.btnInformacion.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              alertDialogInformacion(position);
          }
      });

      holder.btnPrestar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              alertDialogPrestarLibro(position);
          }
      });

    }

    private void alertDialogPrestarLibro(int position) {
        Intent intent = new Intent(context, FormPrestamo.class);
        intent.putExtra("isbn", listaLibros.get(position).getIsbn());
        intent.putExtra("nomLibro", listaLibros.get(position).getNom_libro());
        intent.putExtra("nomAutor", listaLibros.get(position).getAutor());
        intent.putExtra("nomCategoria", listaLibros.get(position).getCategoria());
        intent.putExtra("descrpcion", listaLibros.get(position).getDescripcion());
        intent.putExtra("nomEditorial", listaLibros.get(position).getEditorial());
        intent.putExtra("anioPublicacion", listaLibros.get(position).getAnio_publicacion());
        intent.putExtra("edicion", listaLibros.get(position).getEdicion());
        intent.putExtra("existencias", listaLibros.get(position).getExistencias());
        //context.startActivity(intent);

        ((Activity) context).startActivityForResult(intent,1);

    }

    private void alertDialogInformacion(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View vista = layoutInflater.inflate(R.layout.alert_dialog_info_libro, null);

        TextView tvISBN = vista.findViewById(R.id.tvISBN);
        TextView tvNomLibro = vista.findViewById(R.id.tvNomLibro);
        TextView tvNomAutor = vista.findViewById(R.id.tvNomAutor);
        TextView tvCategoria = vista.findViewById(R.id.tvCategoria);
        TextView tvDescripcion = vista.findViewById(R.id.tvDescripcion);
        TextView tvEditorial = vista.findViewById(R.id.tvEditorial);
        TextView tvAnioPublicacion = vista.findViewById(R.id.tvAnioPublicacion);
        TextView tvEdicion = vista.findViewById(R.id.tvEdicion);
        TextView tvExistencias = vista.findViewById(R.id.tvExistencias);

        tvISBN.setText("ISBN: "+listaLibros.get(position).getIsbn());
        tvNomLibro.setText("LIBRO: "+listaLibros.get(position).getNom_libro());
        tvNomAutor.setText("AUTOR: "+listaLibros.get(position).getAutor());
        tvCategoria.setText("CATEGORIA: "+listaLibros.get(position).getCategoria());
        tvDescripcion.setText("DESCRIPCIÓN: "+listaLibros.get(position).getDescripcion());
        tvEditorial.setText("EDITORIAL: "+listaLibros.get(position).getEditorial());
        tvAnioPublicacion.setText("AÑO DE PUBLICACIÓN: "+listaLibros.get(position).getAnio_publicacion());
        tvEdicion.setText("EDICIÓN: "+listaLibros.get(position).getEdicion());
        tvExistencias.setText("EXISTENCIAS: "+listaLibros.get(position).getExistencias());

        builder.setView(vista);
        builder.create();
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cvLibro;
        ImageView ivPortada;
        TextView tvISBN, tvNomLibro, tvAutor;
        Button btnInformacion, btnPrestar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvLibro = itemView.findViewById(R.id.cvLibro);
            ivPortada = itemView.findViewById(R.id.ivPortada);
            tvISBN = itemView.findViewById(R.id.tvISBN);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvNomLibro = itemView.findViewById(R.id.tvNomLibro);
            btnInformacion = itemView.findViewById(R.id.btnInformacion);
            btnPrestar = itemView.findViewById(R.id.btnPrestar);

        }
    }

    public void filtrar(List<Libro> filtroLibros) {
        this.listaLibros = filtroLibros;
        notifyDataSetChanged();
    }
}
