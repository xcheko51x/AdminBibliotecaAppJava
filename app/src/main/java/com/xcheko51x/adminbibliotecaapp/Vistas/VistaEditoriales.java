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
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorEditoriales;
import com.xcheko51x.adminbibliotecaapp.Clases.Autor;
import com.xcheko51x.adminbibliotecaapp.Clases.Editorial;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityVistaEditorialesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class VistaEditoriales extends AppCompatActivity {

    private ActivityVistaEditorialesBinding binding;

    List<Editorial> listaEditoriales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaEditorialesBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        binding.rvEditoriales.setLayoutManager(new GridLayoutManager(this, 1));

        obtenerEditoriales();

        binding.ibtnEditorialAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAdd();
            }
        });
    }

    private void alertDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VistaEditoriales.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();

        View vista = layoutInflater.inflate(R.layout.alert_dialog_add_editorial, null);

        EditText etIdEditorial = vista.findViewById(R.id.etIdEditorial);
        EditText etNomEditorial = vista.findViewById(R.id.etNomEditorial);

        builder.setView(vista);
        builder.create();
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(etIdEditorial.getText().toString().equals("") || etNomEditorial.getText().toString().equals("")) {
                    Toast.makeText(VistaEditoriales.this, "Se deben de llenar todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    agregarEditorial(etIdEditorial.getText().toString(), etNomEditorial.getText().toString());
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

    private void agregarEditorial(String idEitorial, String nomEditorial) {
        listaEditoriales.clear();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaEditoriales.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(VistaEditoriales.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                                obtenerEditoriales();

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(VistaEditoriales.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaEditoriales.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "403");
                parametros.put("id_editorial", idEitorial);
                parametros.put("nom_editorial", nomEditorial);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VistaEditoriales.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerEditoriales() {
        listaEditoriales = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaEditoriales.this.getString(R.string.url_api),
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
                                Toast.makeText(VistaEditoriales.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        binding.rvEditoriales.setAdapter(new AdaptadorEditoriales(VistaEditoriales.this, listaEditoriales));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaEditoriales.this, "ERROR", Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(VistaEditoriales.this);
        requestQueue.add(stringRequest);

    }
}