package com.xcheko51x.adminbibliotecaapp.Vistas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorAutores;
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorCategorias;
import com.xcheko51x.adminbibliotecaapp.Clases.Autor;
import com.xcheko51x.adminbibliotecaapp.Clases.Categoria;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityVistaCategoriasBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class VistaCategorias extends AppCompatActivity {

    private ActivityVistaCategoriasBinding binding;

    List<Categoria> listaCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaCategoriasBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        binding.rvCategorias.setLayoutManager(new GridLayoutManager(this, 1));

        obtenerCategorias();

        binding.ibtnCategoriaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAdd();
            }
        });
    }

    private void alertDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VistaCategorias.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();

        View vista = layoutInflater.inflate(R.layout.alert_dialog_add_categoria, null);

        EditText etIdCategoria = vista.findViewById(R.id.etIdCategoria);
        EditText etNomCategoria = vista.findViewById(R.id.etNomCategoria);

        builder.setView(vista);
        builder.create();
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(etIdCategoria.getText().toString().equals("") || etNomCategoria.getText().toString().equals("")) {
                    Toast.makeText(VistaCategorias.this, "Se deben de llenar todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    agregarCategoria(etIdCategoria.getText().toString(), etNomCategoria.getText().toString());
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

    private void agregarCategoria(String idCategoria, String nomCategoria) {
        listaCategorias.clear();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaCategorias.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(VistaCategorias.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                                obtenerCategorias();

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(VistaCategorias.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaCategorias.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "503");
                parametros.put("id_categoria", idCategoria);
                parametros.put("nom_categoria", nomCategoria);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VistaCategorias.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerCategorias() {
        listaCategorias = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaCategorias.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(VistaCategorias.this, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                //System.out.println(""+jsonObject.getString("categorias"));
                                JSONArray jsonArray = jsonObject.getJSONArray("categorias");

                                for (int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    listaCategorias.add(
                                            new Categoria(
                                                    item.getString("id_categoria"),
                                                    item.getString("nom_categoria")
                                            )
                                    );
                                }

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(VistaCategorias.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        binding.rvCategorias.setAdapter(new AdaptadorCategorias(VistaCategorias.this, listaCategorias));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaCategorias.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "501");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VistaCategorias.this);
        requestQueue.add(stringRequest);
    }
}