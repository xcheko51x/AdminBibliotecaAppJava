package com.xcheko51x.adminbibliotecaapp.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorPrestamos;
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorUsuarios;
import com.xcheko51x.adminbibliotecaapp.Clases.Autor;
import com.xcheko51x.adminbibliotecaapp.Clases.Libro;
import com.xcheko51x.adminbibliotecaapp.Clases.Prestamo;
import com.xcheko51x.adminbibliotecaapp.Clases.Usuario;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityVistaPrestamosBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class VistaPrestamos extends AppCompatActivity {

    private ActivityVistaPrestamosBinding binding;

    List<Usuario> listaUsuarios;
    List<Prestamo> listaPrestamos;

    AdaptadorPrestamos adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaPrestamosBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        binding.rvPrestamos.setLayoutManager(new GridLayoutManager(this, 1));

        obtenerUsuariosAdeudos();
        obtenerPrestamos();

        binding.spiUsuarios.addTextChangedListener(new TextWatcher() {
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
    }

    private void filtrar(String texto) {
        List<Prestamo> filtrarPrestamo = new ArrayList<>();

        for (Prestamo prestamo : listaPrestamos) {
            if(
                    prestamo.getIsbn().toLowerCase().contains(texto.toLowerCase()) ||
                    prestamo.getNomLibro().toLowerCase().contains(texto.toLowerCase()) ||
                    prestamo.getIdUsuario().toLowerCase().contains(texto.toLowerCase()) ||
                    prestamo.getNomUsuario().toLowerCase().contains(texto.toLowerCase())
            ) {
                filtrarPrestamo.add(prestamo);
            }
        }

        adaptador.filtrar(filtrarPrestamo);
    }

    private void obtenerUsuariosAdeudos() {
        listaUsuarios = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaPrestamos.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(VistaPrestamos.this, response, Toast.LENGTH_LONG).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                //System.out.println(""+jsonObject.getString("usuarios"));
                                JSONArray jsonArray = jsonObject.getJSONArray("usuarios_deudores");

                                for (int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    listaUsuarios.add(
                                            new Usuario(
                                                    item.getString("id_usuario"),
                                                    item.getString("nom_usuario"),
                                                    item.getString("estado_usuario")
                                            )
                                    );
                                }

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(VistaPrestamos.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(VistaPrestamos.this, android.R.layout.simple_spinner_item, listaUsuarios);
                        binding.spiUsuarios.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaPrestamos.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "206");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VistaPrestamos.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerPrestamos() {
        listaPrestamos = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaPrestamos.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(VistaPrestamos.this, response, Toast.LENGTH_LONG).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                //System.out.println(""+jsonObject.getString("prestamos"));
                                JSONArray jsonArray = jsonObject.getJSONArray("prestamos");

                                for (int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    listaPrestamos.add(
                                            new Prestamo(
                                                    item.getString("isbn"),
                                                    item.getString("nom_libro"),
                                                    item.getString("autor"),
                                                    item.getString("editorial"),
                                                    item.getString("anio_publicacion"),
                                                    item.getString("edicion"),
                                                    item.getString("id_usuario"),
                                                    item.getString("nom_usuario"),
                                                    item.getString("fecha_prestamo"),
                                                    item.getString("fecha_devolucion")
                                            )
                                    );
                                }

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(VistaPrestamos.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adaptador = new AdaptadorPrestamos(VistaPrestamos.this, listaPrestamos);
                        binding.rvPrestamos.setAdapter(adaptador);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaPrestamos.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "701");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VistaPrestamos.this);
        requestQueue.add(stringRequest);
    }
}