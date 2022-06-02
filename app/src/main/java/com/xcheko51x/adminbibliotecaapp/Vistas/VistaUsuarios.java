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
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorEditoriales;
import com.xcheko51x.adminbibliotecaapp.Adaptadores.AdaptadorUsuarios;
import com.xcheko51x.adminbibliotecaapp.Clases.Editorial;
import com.xcheko51x.adminbibliotecaapp.Clases.Usuario;
import com.xcheko51x.adminbibliotecaapp.R;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityVistaUsuariosBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class VistaUsuarios extends AppCompatActivity {

    private ActivityVistaUsuariosBinding binding;

    List<Usuario> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVistaUsuariosBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        binding.rvUsuarios.setLayoutManager(new GridLayoutManager(this, 1));

        obtenerUsuarios();
        binding.ibtnUsuarioAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAdd();
            }
        });
    }

    private void alertDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VistaUsuarios.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();

        View vista = layoutInflater.inflate(R.layout.alert_dialog_add_usuario, null);

        EditText etIdUsuario = vista.findViewById(R.id.etIdUsuario);
        EditText etNomUsuario = vista.findViewById(R.id.etNomUsuario);
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
                    Toast.makeText(VistaUsuarios.this, "Se deben de llenar todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    agregarUsuario(
                            etIdUsuario.getText().toString(),
                            etNomUsuario.getText().toString(),
                            etContrasena.getText().toString()
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

    private void agregarUsuario(String idUsuario, String nomUsuario, String contrasena) {
        listaUsuarios.clear();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaUsuarios.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("codigo").equals("OK")) {
                                Toast.makeText(VistaUsuarios.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                                obtenerUsuarios();

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(VistaUsuarios.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaUsuarios.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "203");
                parametros.put("id_usuario", idUsuario);
                parametros.put("nom_usuario", nomUsuario);
                parametros.put("contrasena", contrasena);

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(VistaUsuarios.this);
        requestQueue.add(stringRequest);
    }

    private void obtenerUsuarios() {
        listaUsuarios = new ArrayList<>();

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, VistaUsuarios.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(VistaUsuarios.this, response, Toast.LENGTH_LONG).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                //System.out.println(""+jsonObject.getString("usuarios"));
                                JSONArray jsonArray = jsonObject.getJSONArray("usuarios");

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
                                Toast.makeText(VistaUsuarios.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        binding.rvUsuarios.setAdapter(new AdaptadorUsuarios(VistaUsuarios.this, listaUsuarios));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(VistaUsuarios.this, "ERROR", Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(VistaUsuarios.this);
        requestQueue.add(stringRequest);

    }
}