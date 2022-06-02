package com.xcheko51x.adminbibliotecaapp.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xcheko51x.adminbibliotecaapp.Clases.Autor;
import com.xcheko51x.adminbibliotecaapp.Clases.Categoria;
import com.xcheko51x.adminbibliotecaapp.Clases.Editorial;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityFormAgregarLibroBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FormAgregarLibro extends AppCompatActivity {

    private ActivityFormAgregarLibroBinding binding;

    List<Autor> listaAutores;
    List<Editorial> listaEditoriales;
    List<Categoria> listaCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormAgregarLibroBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        obtenerAutores();
        obtenerEditoriales();
        obtenerCategorias();

        binding.btnAddLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });

    }

    private void validarCampos() {
        if (
                binding.etISBN.getText().toString().trim().equals("") ||
                binding.etNomLibro.getText().toString().trim().equals("") ||
                binding.spiAutores.getText().toString().trim().equals("") ||
                binding.spiEditoriales.getText().toString().trim().equals("") ||
                binding.spiCategorias.getText().toString().trim().equals("") ||
                binding.etAnioPublicacion.getText().toString().trim().equals("") ||
                binding.etEdicion.getText().toString().trim().equals("") ||
                binding.etExistencias.getText().toString().trim().equals("")
        ) {
            Toast.makeText(FormAgregarLibro.this, "Tienes que llenar los campos marcados con *.", Toast.LENGTH_LONG).show();
        } else {

            String[] arrAutor = binding.spiAutores.getText().toString().trim().split(",");
            String nomAutor = arrAutor[1];

            String[] arrEditorial = binding.spiEditoriales.getText().toString().trim().split(",");
            String nomEditorial = arrEditorial[1];

            String[] arrCategoria = binding.spiCategorias.getText().toString().trim().split(",");
            String nomCategoria = arrCategoria[1];

            agregarLibro(
                    binding.etISBN.getText().toString().trim(),
                    binding.etNomLibro.getText().toString().trim(),
                    nomAutor,
                    binding.etDescripcion.getText().toString().trim(),
                    nomEditorial,
                    nomCategoria,
                    binding.etAnioPublicacion.getText().toString().trim(),
                    binding.etEdicion.getText().toString().trim(),
                    binding.etExistencias.getText().toString().trim()
            );
        }
    }

    private void agregarLibro(
            String isbn,
            String nomLibro,
            String nomAutor,
            String descripcion,
            String nomEditorial,
            String nomCategoria,
            String anioPublicacion,
            String edicion,
            String existencias
    ) {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, FormAgregarLibro.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(FormAgregarLibro.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                                finish();

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(FormAgregarLibro.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(FormAgregarLibro.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "603");
                parametros.put("isbn", isbn);
                parametros.put("nom_libro", nomLibro);
                parametros.put("nom_autor", nomAutor);
                parametros.put("descripcion", descripcion);
                parametros.put("nom_editorial", nomEditorial);
                parametros.put("nom_categoria", nomCategoria);
                parametros.put("anio_publicacion", anioPublicacion);
                parametros.put("edicion", edicion);
                parametros.put("existencias", existencias);


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FormAgregarLibro.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerAutores() {
        listaAutores = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, FormAgregarLibro.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(VistaAutores.this, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                //System.out.println(""+jsonObject.getString("usuarios"));
                                JSONArray jsonArray = jsonObject.getJSONArray("autores");

                                for (int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    listaAutores.add(
                                            new Autor(
                                                    item.getString("id_autor"),
                                                    item.getString("nom_autor")
                                            )
                                    );
                                }

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(FormAgregarLibro.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<Autor> adapter = new ArrayAdapter<>(FormAgregarLibro.this, android.R.layout.simple_spinner_item, listaAutores);
                        binding.spiAutores.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(FormAgregarLibro.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "301");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FormAgregarLibro.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerEditoriales() {
        listaEditoriales = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, FormAgregarLibro.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(VistaEditoriales.this, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                //System.out.println(""+jsonObject.getString("editoriales"));
                                JSONArray jsonArray = jsonObject.getJSONArray("editoriales");

                                for (int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    listaEditoriales.add(
                                            new Editorial(
                                                    item.getString("id_editorial"),
                                                    item.getString("nom_editorial")
                                            )
                                    );
                                }

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(FormAgregarLibro.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<Editorial> adapter = new ArrayAdapter<>(FormAgregarLibro.this, android.R.layout.simple_spinner_item, listaEditoriales);
                        binding.spiEditoriales.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(FormAgregarLibro.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "401");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FormAgregarLibro.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerCategorias() {
        listaCategorias = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, FormAgregarLibro.this.getString(R.string.url_api),
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
                                Toast.makeText(FormAgregarLibro.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(FormAgregarLibro.this, android.R.layout.simple_spinner_item, listaCategorias);
                        binding.spiCategorias.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(FormAgregarLibro.this, "ERROR", Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(FormAgregarLibro.this);
        requestQueue.add(stringRequest);
    }
}