package com.xcheko51x.adminbibliotecaapp.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorLibros;
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorUsuarios;
import com.xcheko51x.adminbibliotecaapp.Clases.Autor;
import com.xcheko51x.adminbibliotecaapp.Clases.Libro;
import com.xcheko51x.adminbibliotecaapp.Clases.Usuario;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityFormPrestamoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FormPrestamo extends AppCompatActivity {

    private ActivityFormPrestamoBinding binding;

    List<Usuario> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormPrestamoBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        Intent intent = getIntent();
        String isbn = intent.getStringExtra("isbn");
        String nomLibro = intent.getStringExtra("nomLibro");
        String nomAutor = intent.getStringExtra("nomAutor");
        String nomCategoria = intent.getStringExtra("nomCategoria");
        String descripcion = intent.getStringExtra("descripcion");
        String nomEditorial = intent.getStringExtra("nomEditorial");
        String anioPublicacion = intent.getStringExtra("anioPublicacion");
        String edicion = intent.getStringExtra("edicion");
        int existencias = intent.getIntExtra("existencias",0);

        binding.tvISBN.setText("ISBN: "+isbn);
        binding.tvNomLibro.setText("LIBRO: "+nomLibro);
        binding.tvNomAutor.setText("AUTOR: "+nomAutor);
        binding.tvCategoria.setText("CATEGORIA: "+nomCategoria);
        binding.tvDescripcion.setText("DESCRIPCIÓN: "+descripcion);
        binding.tvEditorial.setText("EDITORIAL: "+nomEditorial);
        binding.tvAnioPublicacion.setText("AÑO DE PUBLICACIÓN: "+anioPublicacion);
        binding.tvEdicion.setText("EDICIÓN: "+edicion);
        binding.tvExistencias.setText("EXISTENCIAS: "+existencias);

        obtenerUsuarios();

        binding.btnPrestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.spiUsuarios.getText().toString().trim().equals("")) {
                    Toast.makeText(FormPrestamo.this, "Debes seleccionar un usuario", Toast.LENGTH_LONG).show();
                } else {

                    String[] arrUsuario = binding.spiUsuarios.getText().toString().trim().split(",");
                    String idUsuario = arrUsuario[0].trim();

                    String[] arrIsbn = binding.tvISBN.getText().toString().trim().split(" ");
                    String isbn = arrIsbn[1].trim();

                    agregarPrestamo(
                            isbn,
                            idUsuario
                    );
                }
            }
        });
    }

    private void agregarPrestamo(String isbn, String idUsuario) {
        //Toast.makeText(this, isbn+", "+idUsuario, Toast.LENGTH_LONG).show();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, FormPrestamo.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(FormPrestamo.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                                finish();

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(FormPrestamo.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(FormPrestamo.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "703");
                parametros.put("isbn", isbn);
                parametros.put("id_usuario", idUsuario);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FormPrestamo.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerUsuarios() {
        listaUsuarios = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, FormPrestamo.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(VistaUsuarios.this, response, Toast.LENGTH_LONG).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                System.out.println(""+jsonObject.getString("usuarios"));
                                JSONArray jsonArray = jsonObject.getJSONArray("usuarios");

                                for (int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);

                                    if(!item.getString("estado_usuario").equals("deudor")) {
                                        listaUsuarios.add(
                                                new Usuario(
                                                        item.getString("id_usuario"),
                                                        item.getString("nom_usuario"),
                                                        item.getString("estado_usuario")
                                                )
                                        );
                                    }
                                }

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(FormPrestamo.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(FormPrestamo.this, android.R.layout.simple_spinner_item, listaUsuarios);
                        binding.spiUsuarios.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(FormPrestamo.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "201");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FormPrestamo.this);
        requestQueue.add(stringRequest);

    }
}