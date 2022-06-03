package com.xcheko51x.adminbibliotecaapp.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.xcheko51x.adminbibliotecaapp.Clases.Libro;
import com.xcheko51x.adminbibliotecaapp.Clases.Prestamo;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.Vistas.FormAgregarLibro;
import com.xcheko51x.adminbibliotecaapp.Vistas.FormPrestamo;
import com.xcheko51x.adminbibliotecaapp.Vistas.VistaPrestamos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AdaptadorPrestamos extends RecyclerView.Adapter<AdaptadorPrestamos.ViewHolder> {

    Context context;
    List<Prestamo> listaPrestamos = new ArrayList<>();

    public AdaptadorPrestamos(Context context, List<Prestamo> listaPrestamos) {
        this.context = context;
        this.listaPrestamos = listaPrestamos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_prestamo, null, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvIdUsuario.setText(listaPrestamos.get(position).getIdUsuario());
        holder.tvNomUsuario.setText(listaPrestamos.get(position).getNomUsuario());
        holder.tvLibroPrestado.setText(listaPrestamos.get(position).getNomLibro());

        holder.btnDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogDevolucion(position);
            }
        });

    }

    private void alertDialogDevolucion(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View vista = layoutInflater.inflate(R.layout.alert_dialog_devolucion_libro, null);

        TextView tvFechaPrestamo = vista.findViewById(R.id.tvFechaPrestamo);
        TextView tvIdUsuario = vista.findViewById(R.id.tvIdUsuario);
        TextView tvNomUsuario = vista.findViewById(R.id.tvNomUsuario);

        TextView tvISBN = vista.findViewById(R.id.tvISBN);
        TextView tvNomLibro = vista.findViewById(R.id.tvNomLibro);
        TextView tvNomAutor = vista.findViewById(R.id.tvNomAutor);
        TextView tvEditorial = vista.findViewById(R.id.tvEditorial);
        TextView tvAnioPublicacion = vista.findViewById(R.id.tvAnioPublicacion);
        TextView tvEdicion = vista.findViewById(R.id.tvEdicion);

        tvFechaPrestamo.setText("FECHA PRESTAMO: "+listaPrestamos.get(position).getFechaPrestamo());
        tvIdUsuario.setText("ID USUARIO: "+listaPrestamos.get(position).getIdUsuario());
        tvNomUsuario.setText("USUARIO: "+listaPrestamos.get(position).getNomUsuario());

        tvISBN.setText("ISBN: "+listaPrestamos.get(position).getIsbn());
        tvNomLibro.setText("LIBRO: "+listaPrestamos.get(position).getNomLibro());
        tvNomAutor.setText("AUTOR: "+listaPrestamos.get(position).getNomAutor());
        tvEditorial.setText("EDITORIAL: "+listaPrestamos.get(position).getNomEditorial());
        tvAnioPublicacion.setText("AÑO DE PUBLICACIÓN: "+listaPrestamos.get(position).getAnioPublicacion());
        tvEdicion.setText("EDICIÓN: "+listaPrestamos.get(position).getEdicion());

        builder.setView(vista);
        builder.create();
        builder.setPositiveButton("DEVOLUCIÓN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String[] arrIdUsuario = tvIdUsuario.getText().toString().split(" ");
                String idUsuario = arrIdUsuario[2];

                String[] arrIsbn = tvISBN.getText().toString().split(" ");
                String isbn = arrIsbn[1];

                System.out.println(idUsuario+" , "+isbn);

                devolverPrestamo(idUsuario, isbn, position);
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

    private void devolverPrestamo(String idUsuario, String isbn, int position) {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, context.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                                listaPrestamos.remove(position);
                                notifyDataSetChanged();

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "706");
                parametros.put("id_usuario", idUsuario);
                parametros.put("isbn", isbn);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return listaPrestamos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdUsuario, tvNomUsuario, tvLibroPrestado;
        Button btnDevolucion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIdUsuario = itemView.findViewById(R.id.tvIdUsuario);
            tvNomUsuario = itemView.findViewById(R.id.tvNomUsuario);
            tvLibroPrestado = itemView.findViewById(R.id.tvLibroPrestado);
            btnDevolucion = itemView.findViewById(R.id.btnDevolucion);
        }
    }

    public void filtrar(List<Prestamo> filtroPrestamos) {
        this.listaPrestamos = filtroPrestamos;
        notifyDataSetChanged();
    }
}
