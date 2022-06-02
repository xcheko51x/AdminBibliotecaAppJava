package com.xcheko51x.adminbibliotecaapp.Adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xcheko51x.adminbibliotecaapp.Clases.Autor;
import com.xcheko51x.adminbibliotecaapp.Clases.Editorial;
import com.xcheko51x.adminbibliotecaapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AdaptadorEditoriales extends RecyclerView.Adapter<AdaptadorEditoriales.ViewHolder> {

    Context context;
    List<Editorial> listaEditoriales;

    public AdaptadorEditoriales(Context context, List<Editorial> listaEditoriales) {
        this.context = context;
        this.listaEditoriales = listaEditoriales;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_editoriales, null, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvEditorial.setText(listaEditoriales.get(position).getNomEditorial().toUpperCase());

        holder.ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogEdit(position);
            }
        });

        holder.ibtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrarEditorial(listaEditoriales.get(position).getIdEditorial(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaEditoriales.size();
    }

    private void alertDialogEdit(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View vista = layoutInflater.inflate(R.layout.alert_dialog_edit_editorial, null);

        EditText etIdEditorial = vista.findViewById(R.id.etIdEditorial);
        etIdEditorial.setEnabled(false);
        etIdEditorial.setFocusable(false);
        etIdEditorial.setText(listaEditoriales.get(position).getIdEditorial());
        EditText etNomEditorial = vista.findViewById(R.id.etNomEditorial);
        etNomEditorial.setText(listaEditoriales.get(position).getNomEditorial());

        builder.setView(vista);
        builder.create();
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(etIdEditorial.getText().toString().equals("") || etNomEditorial.getText().toString().equals("")) {
                    Toast.makeText(context, "Se deben de llenar todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    editarEditorial(etIdEditorial.getText().toString(), etNomEditorial.getText().toString(), position);
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

    private void editarEditorial(String idEditorial, String nomEditorial, int position) {
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
                                listaEditoriales.get(position).setNomEditorial(nomEditorial);
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
                parametros.put("accion", "404");
                parametros.put("id_editorial", idEditorial);
                parametros.put("nom_editorial", nomEditorial);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void borrarEditorial(String idEditorial, int position) {
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
                                listaEditoriales.remove(position);
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
                parametros.put("accion", "405");
                parametros.put("id_editorial", idEditorial);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEditorial;
        ImageButton ibtnEdit, ibtnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEditorial = itemView.findViewById(R.id.tvEditorial);
            ibtnEdit = itemView.findViewById(R.id.ibtnEditar);
            ibtnRemove = itemView.findViewById(R.id.ibtnEliminar);
        }
    }
}
