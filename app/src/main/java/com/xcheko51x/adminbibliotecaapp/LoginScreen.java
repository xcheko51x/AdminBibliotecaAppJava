package com.xcheko51x.adminbibliotecaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xcheko51x.adminbibliotecaapp.Clases.Usuario;
import com.xcheko51x.adminbibliotecaapp.Vistas.VistaNavegacion;
import com.xcheko51x.adminbibliotecaapp.databinding.ActivityLoginScreenBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {

    private ActivityLoginScreenBinding binding;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        View vista = binding.getRoot();
        setContentView(vista);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login(
                        binding.etUsuario.getText().toString(),
                        binding.etContrasena.getText().toString()
                );

            }
        });
    }

    public void login(String etUsuario, String etContrasena) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, LoginScreen.this.getString(R.string.url_api),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        //System.out.println("RESPONSE: "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            //System.out.println(""+jsonObject.getString("codigo"));
                            if(jsonObject.getString("codigo").equals("OK")) {
                                //System.out.println(""+jsonObject.getString("usuarios"));
                                JSONArray jsonArray = jsonObject.getJSONArray("usuarios");

                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                                usuario = new Usuario();
                                usuario.setIdUsuario(jsonObject1.getString("id_usuario"));
                                usuario.setNomUsuario(jsonObject1.getString("nom_usuario"));

                                guardarShared(usuario);

                                Intent intent = new Intent(LoginScreen.this, VistaNavegacion.class);
                                startActivity(intent);
                                finish();

                            }else if(jsonObject.getString("codigo").equals("ERROR")) {
                                Toast.makeText(LoginScreen.this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(LoginScreen.this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("accion", "200");
                parametros.put("usuario", etUsuario);
                parametros.put("contrasena", etContrasena);
                //parametros.put("usuario", "adm0001");
                //parametros.put("contrasena", "sergiop");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginScreen.this);
        requestQueue.add(stringRequest);
    }

    public void guardarShared(Usuario usuario) {
        SharedPreferences preferences = getSharedPreferences("data_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idUsuario", usuario.getIdUsuario());
        editor.putString("nomUsuario", usuario.getNomUsuario());
        editor.apply();
    }
}