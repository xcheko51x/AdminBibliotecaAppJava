package com.xcheko51x.adminbibliotecaapp.Adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.xcheko51x.adminbibliotecaapp.Clases.Editorial;
import com.xcheko51x.adminbibliotecaapp.Clases.Usuario;
import com.xcheko51x.adminbibliotecaapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder> {

    Context context;
    List<Usuario> listaUsuarios;

    public AdaptadorUsuarios(Context context, List<Usuario> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_usuarios, null, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(
                listaUsuarios.get(position).getEstadoUsuario().toLowerCase().equals("null") ||
                        listaUsuarios.get(position).getEstadoUsuario().equals("")
        ) {
            holder.cvUsuario.setCardBackgroundColor(context.getResources().getColor(R.color.verde_oscuro));
        } else if(listaUsuarios.get(position).getEstadoUsuario().toLowerCase().equals("deudor")) {
            holder.cvUsuario.setCardBackgroundColor(context.getResources().getColor(R.color.rojo_oscuro));
        }

        holder.tvNomUsuario.setText(listaUsuarios.get(position).getNomUsuario().toUpperCase());

        holder.ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogEdit(position);
            }
        });

        holder.ibtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarEditorial(listaUsuarios.get(position).getIdUsuario(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    private void alertDialogEdit(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View vista = layoutInflater.inflate(R.layout.alert_dialog_edit_usuario, null);

        EditText etIdUsuario = vista.findViewById(R.id.etIdUsuario);
        etIdUsuario.setEnabled(false);
        etIdUsuario.setFocusable(false);
        etIdUsuario.setText(listaUsuarios.get(position).getIdUsuario());
        EditText etNomUsuario = vista.findViewById(R.id.etNomUsuario);
        etNomUsuario.setText(listaUsuarios.get(position).getNomUsuario());
        EditText etContrasena = vista.findViewById(R.id.etContrasena);

        builder.setView(vista);
        builder.create();
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(
                        etIdUsuario.getText().toString().equals("") ||
                        etNomUsuario.getText().toString().equals("") ||
                        etContrasena.getText().toString().equals("")
                ) {
                    Toast.makeText(context, "Se deben de llenar todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    editarUsuario(
                            etIdUsuario.getText().toString(),
                            etNomUsuario.getText().toString(),
                            etContrasena.getText().toString(),
                            position
                    );
                }
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

    private void editarUsuario(String idUsuario, String nomUsuario, String contrasena, int position) {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, context.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                                listaUsuarios.get(position).setNomUsuario(nomUsuario);
                                listaUsuarios.get(position).setContrasena(contrasena);
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
                parametros.put("accion", "204");
                parametros.put("id_usuario", idUsuario);
                parametros.put("nom_usuario", nomUsuario);
                parametros.put("contrasena", contrasena);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void borrarEditorial(String idUsuario, int position) {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, context.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                                listaUsuarios.remove(position);
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
                parametros.put("accion", "205");
                parametros.put("id_usuario", idUsuario);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cvUsuario;
        TextView tvNomUsuario;
        ImageButton ibtnEdit, ibtnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvUsuario = itemView.findViewById(R.id.cvUsuario);
            tvNomUsuario = itemView.findViewById(R.id.tvNomUsuario);
            ibtnEdit = itemView.findViewById(R.id.ibtnEditar);
            ibtnRemove = itemView.findViewById(R.id.ibtnEliminar);
        }
    }
}
