package com.xcheko51x.adminbibliotecaapp.Vistas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorAutores;
import com.xcheko51x.adminbibliotecaapp.Clases.Autor;
import com.xcheko51x.adminbibliotecaapp.Clases.Usuario;
import com.xcheko51x.adminbibliotecaapp.LoginScreen;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityVistaAutoresBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class VistaAutores extends AppCompatActivity {

    private ActivityVistaAutoresBinding binding;

    List<Autor> listaAutores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaAutoresBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        binding.rvAutores.setLayoutManager(new GridLayoutManager(this, 1));

        obtenerAutores();

        binding.ibtnAutorAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAdd();
            }
        });
    }

    private void alertDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VistaAutores.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();

        View vista = layoutInflater.inflate(R.layout.alert_dialog_add_autor, null);

        EditText etIdAutor = vista.findViewById(R.id.etIdAutor);
        EditText etNomAutor = vista.findViewById(R.id.etNomAutor);

        builder.setView(vista);
        builder.create();
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(etIdAutor.getText().toString().equals("") || etNomAutor.getText().toString().equals("")) {
                    Toast.makeText(VistaAutores.this, "Se deben de llenar todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    agregarAutor(etIdAutor.getText().toString(), etNomAutor.getText().toString());
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

    private void agregarAutor(String idAutor, String nomAutor) {
        listaAutores.clear();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaAutores.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(VistaAutores.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                                obtenerAutores();

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(VistaAutores.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaAutores.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "303");
                parametros.put("id_autor", idAutor);
                parametros.put("nom_autor", nomAutor);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VistaAutores.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerAutores() {
        listaAutores = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaAutores.this.getString(R.string.url_api),
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
                                Toast.makeText(VistaAutores.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        binding.rvAutores.setAdapter(new AdaptadorAutores(VistaAutores.this, listaAutores));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaAutores.this, "ERROR", Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(VistaAutores.this);
        requestQueue.add(stringRequest);
    }
}