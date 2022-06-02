package com.xcheko51x.adminbibliotecaapp.Vistas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorEditoriales;
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorLibros;
import com.xcheko51x.adminbibliotecaapp.Clases.Editorial;
import com.xcheko51x.adminbibliotecaapp.Clases.Libro;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityVistaLibrosBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VistaLibros extends AppCompatActivity {

    private ActivityVistaLibrosBinding binding;

    List<Libro> listaLibros;
    AdaptadorLibros adaptadorLibros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaLibrosBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        binding.rvLibros.setLayoutManager(new GridLayoutManager(this, 2));

        obtenerLibros();

        binding.etLibroBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filtrar(editable.toString());
            }
        });

        binding.ibtnLibroAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VistaLibros.this, FormAgregarLibro.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                obtenerLibros();
            }
        }

    }

    private void filtrar(String texto) {
        List<Libro> filtrarLibros = new ArrayList<>();

        for (Libro libro : listaLibros) {
            if(
                    libro.getIsbn().toLowerCase().contains(texto.toLowerCase()) ||
                    libro.getNom_libro().toLowerCase().contains(texto.toLowerCase()) ||
                    libro.getAutor().toLowerCase().contains(texto.toLowerCase())
            ) {
                filtrarLibros.add(libro);
            }
        }

        adaptadorLibros.filtrar(filtrarLibros);
    }

    private void obtenerLibros() {
        listaLibros = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaLibros.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(VistaLibros.this, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                //System.out.println(""+jsonObject.getString("libros"));
                                JSONArray jsonArray = jsonObject.getJSONArray("libros");

                                for (int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    listaLibros.add(
                                            new Libro(
                                                    item.getString("isbn"),
                                                    item.getString("portada"),
                                                    item.getString("nom_libro"),
                                                    item.getString("autor"),
                                                    item.getString("descripcion"),
                                                    item.getString("editorial"),
                                                    item.getString("anio_publicacion"),
                                                    item.getString("edicion"),
                                                    item.getInt("existencias"),
                                                    item.getString("categoria")
                                            )
                                    );
                                }

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(VistaLibros.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adaptadorLibros = new AdaptadorLibros(VistaLibros.this, listaLibros);
                        binding.rvLibros.setAdapter(adaptadorLibros);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaLibros.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "601");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VistaLibros.this);
        requestQueue.add(stringRequest);
    }
}